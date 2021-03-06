package ru.bmstu.lab6.zookeeper;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
        private final String server;
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
                    int i = ThreadLocalRandom.current().nextInt(0, servers.size());
                    getSender().tell(new ResRandomServerMsg(servers.get(i)), ActorRef.noSender());
                })
                .build();
    }
}
