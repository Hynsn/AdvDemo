package com.example;
import com.example.aidl.Message;
interface MessageReceiveListener {
    oneway void onReceive(in Message message);
}