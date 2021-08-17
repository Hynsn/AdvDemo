package com.example.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.base.base.BaseActivity;
import com.example.IConnectionInterface;
import com.example.IMessageService;
import com.example.IServiceManager;
import com.example.MessageReceiveListener;
import com.example.R;
import com.example.databinding.ActivityAidlBinding;

public class AidlActivity extends BaseActivity<ActivityAidlBinding> {
    private IServiceManager serviceManager;
    private IConnectionInterface connectProxy;
    private IMessageService messageProxy;

    @Override
    protected int getLayout() {
        return R.layout.activity_aidl;
    }

    @Override
    protected void bindView() {
        Intent intent = new Intent(this,RemoteService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                try {
                    serviceManager = IServiceManager.Stub.asInterface(iBinder);
                    connectProxy = IConnectionInterface.Stub.asInterface(serviceManager.getService(IConnectionInterface.class.getSimpleName()));
                    messageProxy = IMessageService.Stub.asInterface(serviceManager.getService(IMessageService.class.getSimpleName()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    private MessageReceiveListener receiveListener = new MessageReceiveListener.Stub() {
        @Override
        public void onReceive(final Message message) throws RemoteException {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AidlActivity.this,message.getContent(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public void click(View v){
        switch (v.getId()){
            case R.id.btn_connect:
                try {
                    connectProxy.connect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_disconnect:
                try {
                    connectProxy.disconnect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_isconnect:
                try {
                    connectProxy.isConnected();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_send:
                try {
                    Message msg = new Message();
                    msg.setContent("主线程调用");
                    messageProxy.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_register:
                try {
                    messageProxy.registerListener(receiveListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_unregister:
                try {
                    messageProxy.unregisterListener(receiveListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}