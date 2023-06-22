package com.hynson.ble.precenter;

import android.bluetooth.BluetoothDevice;

/**
 * Created by A on 2017/7/5.
 */
//发送返回值
public interface ResponseI {
    public void sendResponse(BluetoothDevice device, int requestId, int status, int offset, byte[] value);
}
