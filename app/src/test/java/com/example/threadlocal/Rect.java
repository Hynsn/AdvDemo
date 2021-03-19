package com.example.threadlocal;

public class Rect {
    public int w;
    public int h;

    public Rect(int w, int h) {
        this.w = w;
        this.h = h;
    }

    @Override
    public String toString() {
        return "Rect{" +
                "w=" + w +
                ", h=" + h +
                '}';
    }
}
