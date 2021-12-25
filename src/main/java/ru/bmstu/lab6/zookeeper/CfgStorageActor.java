package ru.bmstu.lab6.zookeeper;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;

public class CfgStorageActor implements AbstractActor {

    public static class StoreMsg {
        private final ArrayList<Integer> ports;
        public StoreMsg
    }

    @Override
    public Receive createReceive() {
        ReceiveBuilder.create()
                .match()
                .build();
    }
}
