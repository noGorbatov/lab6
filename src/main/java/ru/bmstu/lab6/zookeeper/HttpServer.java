package ru.bmstu.lab6.zookeeper;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;

public class HttpServer extends AllDirectives {
    private static final String URL_PARAM = "url";
    private static final String COUNT_PARAM = "count";
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

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
    }
}
