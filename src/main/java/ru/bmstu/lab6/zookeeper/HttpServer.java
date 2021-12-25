package ru.bmstu.lab6.zookeeper;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;

public class HttpServer extends AllDirectives {
    private static final  
    public Route createRoute() {
        return concat(
                get( () -> parameter())
        )
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
    }
}
