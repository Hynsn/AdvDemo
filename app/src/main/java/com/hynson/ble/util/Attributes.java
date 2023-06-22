package com.hynson.ble.util;

import java.util.HashMap;

/**
 * Created by A on 2017/7/3.
 */

//uuid属性
public class Attributes {
    private static Attributes instance;

    private HashMap<String,String> attributeMap;

    public static String CONFIG_SERVICE_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    public static String CONFIG_SERVICE = "CONFIG_SERVICE";

    public static String CONFIG_CHARACTER_UUID = "00002902-0000-1000-8000-00805f9b34fc";
    public static String CONFIG_CHARACTER = "CONFIG_CHARACTER";

    public static Attributes getInstance(){
        if(instance == null){
            instance = new Attributes();
        }
        return instance;
    }

    private Attributes(){
        init();
    }

    private void init(){
        attributeMap = new HashMap<>();
        attributeMap.put(CONFIG_CHARACTER_UUID,CONFIG_CHARACTER);
        attributeMap.put(CONFIG_SERVICE_UUID,CONFIG_SERVICE);
    }

    public String getAttribute(String name){
        return attributeMap.get(name)==null?"":attributeMap.get(name);
    }

    public void putAttribute(String uuid,String name){
        this.attributeMap.put(uuid,name);
    }
}
