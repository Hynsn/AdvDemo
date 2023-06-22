package com.hynson.ble.util;

import android.util.Log;

import com.hynson.ble.LogViewI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by A on 2017/4/13.
 */

public class LogUtil {
    private static LogUtil instance = new LogUtil();

    private String myTag = "myBleConfig";

    private boolean isShowE = true;

    private boolean isShowI = true;

    private boolean isShowW = true;

    private boolean isShow = true;

    private Map<String,String> notShowTagList;

    private LogViewI logView;

    private LogUtil(){
        notShowTagList = new HashMap<String,String>();
    }

    public static LogUtil getInstance(){
        return instance;
    }

    public void setLogView(LogViewI logView){
        this.logView = logView;
    }

    public void elog(String tag , String content){
        if(!isShow){
            return ;
        }
        if(notShowTagList.get(tag)!=null){
            return;
        }
        if(isShowE){
            Log.e(myTag,getTime()+content);
            showInLogView(tag,content);
        }
    }

    public void ilog(String tag , String content){
        if(!isShow){
            return ;
        }
        if(notShowTagList.get(tag)!=null){
            return;
        }
        if(isShowI){
            Log.i(myTag,getTime()+content);
            showInLogView(tag,content);
        }
    }

    public void wlog(String tag , String content){
        if(!isShow){
            return ;
        }
        if(notShowTagList.get(tag)!=null){
            return;
        }
        if(isShowW){
            Log.w(myTag,getTime()+content);
            showInLogView(tag,content);
        }
    }

    public boolean isShowE() {
        return isShowE;
    }

    public void setShowE(boolean showE) {
        isShowE = showE;
    }

    public boolean isShowI() {
        return isShowI;
    }

    public void setShowI(boolean showI) {
        isShowI = showI;
    }

    public String getMyTag() {
        return myTag;
    }

    public void setMyTag(String myTag) {
        this.myTag = myTag;
    }

    public boolean isShowW() {
        return isShowW;
    }

    public void setShowW(boolean showW) {
        isShowW = showW;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public void addNotShowTag(String tag){
        this.notShowTagList.put(tag,tag);
    }

    public String getTime(){
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
        return time;
    }

    public void showInLogView(String tag,String content){
        if(logView!=null){
            logView.addLog(tag,content);
        }
    }

}
