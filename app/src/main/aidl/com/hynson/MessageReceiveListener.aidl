package com.hynson;
import com.hynson.aidl.Message;
interface MessageReceiveListener {
    oneway void onReceive(in Message message);
}