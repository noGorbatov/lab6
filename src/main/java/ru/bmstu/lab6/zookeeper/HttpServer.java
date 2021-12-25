package ru.bmstu.lab6.zookeeper;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;

public class HttpServer extends AllDirectives {
    private static final String URL_PARAM = "url";
    private static final String COUNT_PARAM = "count";

    private final Http http;
    private final String host;
    private final int port;

    public Route createRoute() {
        return concat(
                get( () -> parameter(URL_PARAM, url -> {
                    parameter(COUNT_PARAM, countString -> {
                        int count = Integer.parseInt(countString);
                        if (count == 0) {

                        }
                    })
                }))
        )
    }

    public HttpServer(String host, int port) {
        this.http = host;
        this.port = port;
        http = Http.get(context)
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
    }
}
