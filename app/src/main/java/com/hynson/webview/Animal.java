package com.hynson.webview;

import android.webkit.JavascriptInterface;

public class Animal {
    private String name;
    private String img;
    private String introduce;
    private boolean sex;
    private int age;

    public Animal(String name, String img, String introduce, boolean sex, int age) {
        this.name = name;
        this.img = img;
        this.introduce = introduce;
        this.sex = sex;
        this.age = age;
    }

    @JavascriptInterface
    public String getName() {
        return name;
    }
    @JavascriptInterface
    public int getAge() {
        return age;
    }
    @JavascriptInterface
    public String getImg() {
        return img;
    }
    @JavascriptInterface
    public boolean isSex() {
        return sex;
    }
    @JavascriptInterface
    public String getIntroduce() {
        return introduce;
    }
}
