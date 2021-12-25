package ru.bmstu.lab6.zookeeper;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ZookeeperClient implements Watcher {
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
        client = new ZooKeeper(SERVER_ADDR, SESSION_TIMEOUT_MS, this);
        client.create(BASE_NODE_PATH + "/s",
                      (host + ":" + port).getBytes(),
                      ZooDefs.Ids.OPEN_ACL_UNSAFE,
                      CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("event fired " + event);

        List<String> children;
        ArrayList<String> newServers = new ArrayList<>();
        try {
            children = client.getChildren(BASE_NODE_PATH, true);
        for (String child: children) {
            byte[] bytes = client.getData(BASE_NODE_PATH + "/" + child, true, null);
            String server = new String(bytes, StandardCharsets.UTF_8);
            newServers.add(server);
        }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

        storageActor.tell(new CfgStorageActor.StoreServerMsg(newServers), ActorRef.noSender());
    }
}
