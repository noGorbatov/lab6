package ru.bmstu.lab6.zookeeper;

import org.apache.zookeeper.ZooKeeper;

public class ZookeeperClient {
    private static final String SERVER_ADDR = "localhost:2181";
    private String host;
    private int port;
    private ZooKeeper client;
    public ZookeeperClient(String host, int port) {
        this.host = host;
        this.port = port;
        client =


    }
}
