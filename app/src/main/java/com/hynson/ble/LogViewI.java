package com.hynson.ble;

/**
 * Created by A on 2017/7/4.
 */

public interface LogViewI {
    public void clearLog();
    public void addLog(String tag,String content);
}
