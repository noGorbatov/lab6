package ru.bmstu.lab6.zookeeper;

import akka.actor.ActorRef;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZookeeperClient {
    private static final String SERVER_ADDR = "localhost:2181";
    private static final int SESSION_TIMEOUT_MS = 5000;
    private static final String BASE_NODE_PATH = "/servers";

    private String host;
    private int port;
    private ZooKeeper client;
    public ZookeeperClient(String host, int port, ActorRef storageActor) throws IOException {
        this.host = host;
        this.port = port;
        client = new ZooKeeper(SERVER_ADDR, SESSION_TIMEOUT_MS, event -> {
            System.out.println("event fired " + event);
        });
        client.create(BASE_NODE_PATH + "/s",
                      port.)
    }
}
