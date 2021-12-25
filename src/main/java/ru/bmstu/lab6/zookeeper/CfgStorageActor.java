package ru.bmstu.lab6.zookeeper;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;

public class CfgStorageActor extends AbstractActor {
    private ArrayList<String> servers;
    public static class StoreServerMsg {
        private final ArrayList<String> servers;
        public StoreServerMsg(ArrayList<String> servers) {
            this.servers = servers;
        }
    }
    public static class GetRandomServerMsg {}
    public static class ResRandomServerMsg {
        private String server;
        public ResRandomServerMsg(String server) {
            this.server = server;
        }
        public String getServer() {
            return server;
        }
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(StoreServerMsg.class, msg -> {
                    servers = msg.servers;
                })
                .match(GetRandomServerMsg.class, msg -> {
                    String server = 
                    getSender().tell(new ResRandomServerMsg());
                })
                .build();
    }
}
