package com.example;

// 连接服务
interface IConnectionInterface {
    oneway void connect();
    void disconnect();
    boolean isConnected();
}