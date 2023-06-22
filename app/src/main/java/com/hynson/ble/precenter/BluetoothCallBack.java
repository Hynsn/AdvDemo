package com.hynson.ble.precenter;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;


import com.hynson.ble.data.Wifi;
import com.hynson.ble.WifiviewI;
import com.hynson.ble.util.Constant;
import com.hynson.ble.util.LogUtil;
import com.google.gson.GsonBuilder;

import facotory.ThreadFactory;


/**
 * Created by A on 2017/7/3.
 */

//连接、读写的回调
public class BluetoothCallBack extends BluetoothGattServerCallback{

    private String TAG = "BluetoothCallBack";
    private WifiviewI wifiview;

    private ResponseI response;

    private StringBuilder reciveStr;//收到的数据
    private int byteCount;//收到信息的条数

    public BluetoothCallBack(WifiviewI wifiview){
        this.wifiview = wifiview;
        reciveStr = new StringBuilder();
    }

    public void setResponse(ResponseI response){
        this.response = response;
    }

    //添加一个服务之后的回调
    public void onServiceAdded(int status, BluetoothGattService service) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            LogUtil.getInstance().ilog(TAG,"添加服务成功！服务的uuid为"+service.getUuid().toString());
        } else {
            LogUtil.getInstance().ilog(TAG, "添加服务失败！");
        }
    }

    //连接状态改变之后的回调
    public void onConnectionStateChange(android.bluetooth.BluetoothDevice device, int status,
                                        int newState) {
        LogUtil.getInstance().ilog(TAG, "连接状态为" + newState);
    }

    //当客户端来读数据时的回调
    public void onCharacteristicReadRequest(android.bluetooth.BluetoothDevice device,
                                            int requestId, int offset, BluetoothGattCharacteristic characteristic) {
        LogUtil.getInstance().ilog(TAG, "客户端来读取数据");
    }

    //当有客户端来写数据时回调的回调
    @Override
    public void onCharacteristicWriteRequest(android.bluetooth.BluetoothDevice device,
                                             int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite,
                                             boolean responseNeeded, int offset, byte[] value) {
        final String str = new String(value);
        LogUtil.getInstance().ilog(TAG, "客户端来写数据，写的数据为"+str);
        if(response != null){
            response.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
        }

        ThreadFactory.getNormalPool().execute(new Runnable() {
            @Override
            public void run() {
                if(str.contains(Constant.begin)){//收到第一条数据
                    reciveStr.delete(0,reciveStr.length());//清空数据
                    try{
                        String count = str.substring(Constant.begin.length());
                        byteCount = Integer.parseInt(count);
                        byteCount --;
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else if(str.equals(Constant.end)){//收到最后一条数据
                    byteCount --;
                    if(byteCount == 0){
                        try {
                            Wifi wifi = new GsonBuilder().create().fromJson(reciveStr.toString(), Wifi.class);
                            wifiview.showWifi(wifi.getSsid(), wifi.getPwd());
                        }catch(Exception e){
                            LogUtil.getInstance().ilog(TAG,"接收到的数据："+reciveStr.toString()+"解析出现了错误！"+e);
                        }
                    }
                    reciveStr.delete(0,reciveStr.length());
                }else{//收到中间的数据
                    reciveStr.append(str);
                    byteCount--;
                }
            }
        });

    }
}
