package com.hynson.ble.data;

/**
 * Created by A on 2017/7/4.
 */

public class Wifi {
    private String ssid;
    private String pwd;

    public Wifi(String ssid,String pwd){
        this.ssid = ssid;
        this.pwd = pwd;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
