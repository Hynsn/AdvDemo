package com.hynson;

interface IServiceManager {
    IBinder getService(String name);
}