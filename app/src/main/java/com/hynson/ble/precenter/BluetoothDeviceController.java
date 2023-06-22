package com.hynson.ble.precenter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.hynson.ble.util.Attributes;
import com.hynson.ble.util.LogUtil;

import facotory.ThreadFactory;

import java.util.UUID;

import static android.content.Context.BLUETOOTH_SERVICE;

/**
 * Created by A on 2017/6/30.
 */

public class BluetoothDeviceController implements ResponseI{

    private String tag = "BluetoothController";
    private static final int REQUEST_ENABLE_BT = 1;

    private Activity context;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeAdvertiser mBluetoothAdvertiser;//用于向外发送广告

    private BluetoothGattServer mGattServer;//开启的服务

    private BluetoothGattServerCallback callBack;//数据读写的回调
    private AdvertiseCallback adCallBack;//广告发送的回调

    private boolean isStop;//是否停止了

    public BluetoothDeviceController(Activity context){
        this.context = context;
        isStop = false;
        init();
    }

    //初始化
    public boolean init(){
        String str = "设备不支持蓝牙低功耗通讯";
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            LogUtil.getInstance().ilog(tag,str);
            return false;
        }

        mBluetoothManager = (BluetoothManager) context.getSystemService(BLUETOOTH_SERVICE);
        if (mBluetoothManager == null) {
            LogUtil.getInstance().ilog(tag,str);
            return false;
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if(mBluetoothAdapter == null){
            LogUtil.getInstance().ilog(tag,str);
            return false;
        }

        mBluetoothAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        if (mBluetoothAdvertiser == null) {
            LogUtil.getInstance().ilog(tag,str);
            return false;
        }

        //打开蓝牙的套路
        if ((mBluetoothAdapter == null) || (!mBluetoothAdapter.isEnabled())) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        return true;
    }

    //设置读写的callback
    public void setCallBack(BluetoothGattServerCallback callBack){
        this.callBack = callBack;
    }

    //设置发送停止广告的callback
    public void setAdCallBack(AdvertiseCallback callBack){
        this.adCallBack = callBack;
    }

    //增加服务
    public void addService(BluetoothGattService gattService){
        this.mGattServer.addService(gattService);
    }

    public void addTestService(){
        if(callBack == null){
            return;
        }
        if(mGattServer == null) {
            mGattServer = mBluetoothManager.openGattServer(context, callBack);
        }
        //构造服务和特征值
        BluetoothGattService testService = new BluetoothGattService(UUID.fromString(Attributes.CONFIG_SERVICE_UUID),
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        BluetoothGattCharacteristic fcharacter = new BluetoothGattCharacteristic(
                UUID.fromString(Attributes.CONFIG_CHARACTER_UUID),
                BluetoothGattCharacteristic.PROPERTY_READ |BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_NOTIFY ,
                BluetoothGattCharacteristic.PERMISSION_READ |BluetoothGattCharacteristic.PERMISSION_WRITE);
        fcharacter.setValue("first");

        testService.addCharacteristic(fcharacter);

        if(mGattServer!=null && testService!=null)
            mGattServer.addService(testService);
    }

    //发送广告
    public void send(){
         //获取BluetoothLeAdvertiser，BLE发送BLE广播用的一个API
        if (mBluetoothAdvertiser == null) {
            LogUtil.getInstance().ilog(tag,"mBluetoothAdvertiser为空");
            return;
        }

        if(callBack == null){
            LogUtil.getInstance().ilog(tag,"callback为空");
            return;
        }

        if(adCallBack == null){
            LogUtil.getInstance().ilog(tag,"adCallBack为空");
            return;
        }

        if(mGattServer == null){
            LogUtil.getInstance().ilog(tag,"mGattServer为空");
            return;
        }

        try{
            mBluetoothAdvertiser.startAdvertising(createAdvSettings(true, 0), createFMPAdvertiseData(),adCallBack);
        }catch(Exception e){
            e.printStackTrace();
            LogUtil.getInstance().ilog(tag,"发送广告出现异常"+e);
        }
    }

    public boolean isStoped(){
        return isStop;
    }

    //停止发送广告
    public void stop(){
        ThreadFactory.getNormalPool().execute(new Runnable() {
            @Override
            public void run() {
                //关闭BluetoothLeAdvertiser，BluetoothAdapter，BluetoothGattServer
                if (mBluetoothAdvertiser != null) {
                    mBluetoothAdvertiser.stopAdvertising(adCallBack);
                    mBluetoothAdvertiser = null;
                }

                if(mBluetoothAdapter != null){
                    mBluetoothAdapter = null;
                }

                if (mGattServer != null) {
                    mGattServer.clearServices();
                    mGattServer.close();
                    mGattServer = null;
                }
                isStop = true;
                LogUtil.getInstance().ilog(tag,"结束服务成功");
            }
        });

    }

    public  AdvertiseSettings createAdvSettings(boolean connectable, int timeoutMillis) {
        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();
        //设置广播的模式,应该是跟功耗相关
        builder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        builder.setConnectable(connectable);
        builder.setTimeout(timeoutMillis);
        builder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        return builder.build();
    }

    //设置一下FMP广播数据
    public  AdvertiseData createFMPAdvertiseData() {
        AdvertiseData.Builder builder = new AdvertiseData.Builder();
        builder.setIncludeDeviceName(true);
        AdvertiseData adv = builder.build();
        return adv;
    }

    @Override
    public void sendResponse(BluetoothDevice device, int requestId, int status, int offset, byte[] value) {
        if(mGattServer!=null) {
            mGattServer.sendResponse(device, requestId, status, offset, value);
        }
    }
}
