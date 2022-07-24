package com.hynson;
import com.hynson.aidl.Message;
import com.hynson.MessageReceiveListener;

interface IMessageService {
    void send(in Message message);

    void registerListener(MessageReceiveListener listener);
    void unregisterListener(MessageReceiveListener listener);
}