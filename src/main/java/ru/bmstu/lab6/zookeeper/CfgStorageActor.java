package ru.bmstu.lab6.zookeeper;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;

public class CfgStorageActor implements AbstractActor {

    public static class StorePortsMsg {
        private final ArrayList<Integer> ports;
        public StorePortsMsg(ArrayList<Integer> ports) {
            this.ports = ports;
        }
    }

    public static class GetPortsMsg {
        private final ArrayList<Integer> ports;
        public GetPortsMsg(ArrayList<Integer> ports) {
            this.ports = ports;
        }
        public ArrayList<Integer> getPorts() {
            return ports;
        }
    }

    @Override
    public Receive createReceive() {
        ReceiveBuilder.create()
                .match(StorePortsMsg.class, msg -> {

                })
                .build();
    }
}
