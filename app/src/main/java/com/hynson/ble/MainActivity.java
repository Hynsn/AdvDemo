package com.hynson.ble;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.hynson.R;
import com.hynson.ble.precenter.BluetoothCallBack;
import com.hynson.ble.precenter.BluetoothDeviceController;
import com.hynson.ble.util.LogUtil;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,WifiviewI,LogViewI{

    private Button config_btn;
    private Button stop_btn;
    private Button connect_btn;
    private Button clear_btn;
    private EditText ssid_edit;
    private EditText pwd_edit;
    private ListView log_listview;

    private LogAdapter logAdapter;

    private BluetoothCallBack btcallback;

    private BluetoothDeviceController controller;

    private String tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        LogUtil.getInstance().setLogView(this);

        config_btn = (Button)findViewById(R.id.config_btn);
        stop_btn = (Button)findViewById(R.id.stop_btn);
        connect_btn = (Button)findViewById(R.id.connect_btn);
        clear_btn = (Button)findViewById(R.id.clear_btn);
        connect_btn.setOnClickListener(this);
        stop_btn.setOnClickListener(this);
        config_btn.setOnClickListener(this);
        clear_btn.setOnClickListener(this);

        ssid_edit = (EditText)findViewById(R.id.ssid_edit);
        pwd_edit = (EditText)findViewById(R.id.pwd_edit);

        btcallback = new BluetoothCallBack(this);

        controller = new BluetoothDeviceController(this);
        controller.setAdCallBack(adCallBack);//设置广告回调
        controller.setCallBack(btcallback);//设置数据回调
        controller.addTestService();//增加配网服务

        btcallback.setResponse(controller);

        log_listview = (ListView)findViewById(R.id.log_listview);
        logAdapter = new LogAdapter(this);
        log_listview.setAdapter(logAdapter);


    }

    @Override
    public void onClick(View view) {
        if(view == config_btn){
            if(controller.isStoped()){//如果之前停止过，则需要重新初始化
                controller.init();
                controller.setAdCallBack(adCallBack);//设置广告回调
                controller.setCallBack(btcallback);//设置数据回调
                controller.addTestService();
            }
            controller.send();
        }else if(view == stop_btn){
            controller.stop();
        }else if(view == connect_btn){

        }else if(clear_btn == view){
            this.clearLog();
        }
    }

    @Override
    public void showWifi(final String ssid, final String pwd) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ssid_edit.setText(ssid);
                pwd_edit.setText(pwd);
            }
        });
    }

    @Override
    public void clearWifi() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ssid_edit.setText("");
                pwd_edit.setText("");
            }
        });
    }

    private AdvertiseCallback adCallBack = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            LogUtil.getInstance().ilog(tag,"广告回调 onStartSuccess");
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            LogUtil.getInstance().ilog(tag,"广告回调 onStartFailure");
        }
    };

    @Override
    public void clearLog() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logAdapter.clearLog();
            }
        });
    }

    @Override
    public void addLog(final String tag, final String content) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logAdapter.addLog(tag,content);
            }
        });
    }
}
