package ru.bmstu.lab6.zookeeper;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;

public class CfgStorageActor extends AbstractActor {

    public static class StorePortsMsg {
        private final ArrayList<Integer> ports;
        public StorePortsMsg(ArrayList<Integer> ports) {
            this.ports = ports;
        }
    }

    public static class GetPortRandomMsg {
        private final int port;
        public GetPortRandomMsg(int port) {
            this.port = port;
        }
        public int getPorts() {
            return port;
        }
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(StorePortsMsg.class, msg -> {

                })
                .build();
    }
}
