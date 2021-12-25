package ru.bmstu.lab6.zookeeper;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

public class ZookeeperClient {
    private static final String SERVER_ADDR = "localhost:2181";
    private static final int SESSION_TIMEOUT_MS = 5000;
    private static final String BASE_NODE_PATH = "/servers";

    private final String host;
    private final int port;
    private final ZooKeeper client;
    private final ActorRef storageActor;

    public ZookeeperClient(String host, Integer port, ActorRef storageActor) throws IOException, InterruptedException, KeeperException {
        this.host = host;
        this.port = port;
        this.storageActor = storageActor;
        client = new ZooKeeper(SERVER_ADDR, SESSION_TIMEOUT_MS, event -> {
            System.out.println("event fired " + event);
        });
        client.create(BASE_NODE_PATH + "/s",
                      (host + port).getBytes(),
                      ZooDefs.Ids.OPEN_ACL_UNSAFE,
                      CreateMode.EPHEMERAL_SEQUENTIAL);
    }
    private void updateServers(Watcher.Event event) throws InterruptedException, KeeperException {
        List<String> children = client.getChildren(BASE_NODE_PATH, true);
        for (String child: children) {
            
        }
    }
}
