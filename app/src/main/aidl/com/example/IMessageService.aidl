package com.example;
import com.example.aidl.Message;
import com.example.MessageReceiveListener;

interface IMessageService {
    void send(in Message message);

    void registerListener(MessageReceiveListener listener);
    void unregisterListener(MessageReceiveListener listener);
}