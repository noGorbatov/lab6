package ru.bmstu.lab6.zookeeper;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.pattern.PatternsCS;

import java.util.concurrent.CompletionStage;

public class HttpServer extends AllDirectives {
    private static final String URL_PARAM = "url";
    private static final String COUNT_PARAM = "count";
    private static final int ACTOR_TIMEOUT_MS = 4000;

    private final Http http;
    private final String host;
    private final int port;
    private final ActorRef storageActor;

    private CompletionStage<HttpResponse> fetch(String url) {
        return http.singleRequest(HttpRequest.create(url));
    }

    public Route createRoute() {
        return concat(
                get( () -> parameter(URL_PARAM, url -> {
                    parameter(COUNT_PARAM, countString -> {
                        int count = Integer.parseInt(countString);
                        if (count == 0) {
                            return completeWithFuture(fetch(url));
                        }

                        return completeWithFuture(PatternsCS.ask(storageActor,
                                    new CfgStorageActor.GetRandomServerMsg(),
                                    ACTOR_TIMEOUT_MS).thenCompose( resp -> {
                            CfgStorageActor.ResRandomServerMsg res = ResRandomServerMsg
                        })
                    })
                }))
        )
    }

    public HttpServer(String host, int port, ActorSystem system) {
        this.host = host;
        this.port = port;
        http = Http.get(system);
        storageActor = system.actorOf(Props.create(CfgStorageActor.class));
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
    }
}
