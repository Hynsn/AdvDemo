package com.example;

interface IServiceManager {
    IBinder getService(String name);
}