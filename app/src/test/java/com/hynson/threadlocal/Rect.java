package com.hynson.threadlocal;

public class Rect {
    // 实例变量
    private final int s = 5;
    
    // 类变量（静态变量）
    static final  int t = 0;
    
    private static final String ss = null;

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
