package ru.bmstu.lab6.zookeeper;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.japi.Pair;
import akka.pattern.PatternsCS;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class HttpServer extends AllDirectives {
    private static final String URL_PARAM = "url";
    private static final String COUNT_PARAM = "count";
    private static final int ACTOR_TIMEOUT_MS = 4000;
    private static final String HOST = "localhost";

    private final Http http;
    private final String host;
    private final int port;
    private final ActorRef storageActor;
    private final ZookeeperClient client;

    private CompletionStage<HttpResponse> fetch(String url) {
        return http.singleRequest(HttpRequest.create(url));
    }

    private String createUrl(String server, String testUrl, Integer count) {
        return Uri.create(server).
                query(Query.create(new Pair<>(URL_PARAM, testUrl),
                                   new Pair<>(COUNT_PARAM, count.toString()))).toString();
    }

    public Route createRoute() {
        return concat(
                get( () -> parameter(URL_PARAM, url ->
                    parameter(COUNT_PARAM, countString -> {
                        int count = Integer.parseInt(countString);
                        if (count == 0) {
                            return completeWithFuture(fetch(url));
                        }

                        return completeWithFuture(PatternsCS.ask(storageActor,
                                    new CfgStorageActor.GetRandomServerMsg(),
                                    ACTOR_TIMEOUT_MS).thenCompose( resp -> {
                            CfgStorageActor.ResRandomServerMsg res =
                                    (CfgStorageActor.ResRandomServerMsg) resp;
                            String server = res.getServer();
                            System.out.println("request for " + server + " got count " + count);
                            String reqUrl = createUrl(server, url, count - 1);
                            return fetch(reqUrl);
                        }));
                    })
                ))
        );
    }

    public HttpServer(String host, int port, ActorSystem system) throws IOException, InterruptedException, KeeperException {
        this.host = host;
        this.port = port;
        http = Http.get(system);
        storageActor = system.actorOf(Props.create(CfgStorageActor.class));
        client = new ZookeeperClient(host, port, storageActor);
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        int port = Integer.parseInt(args[0]);
        ActorSystem system = ActorSystem.create();
        ActorMaterializer materializer = ActorMaterializer.create(system);
        Http http = Http.get(system);
        HttpServer server = new HttpServer(HOST, port, system);
        Flow<HttpRequest, HttpResponse, NotUsed> flow = server.createRoute().flow(system, materializer);

        CompletionStage<ServerBinding> binding = http.bindAndHandle(
                flow,
                ConnectHttp.toHost(HOST, port),
                materializer
        );

        System.in.read();

        binding.thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }
}
