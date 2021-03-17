package com.example.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.IConnectionInterface;
import com.example.IMessageService;
import com.example.IServiceManager;
import com.example.MessageReceiveListener;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RemoteService extends Service {

    final static String TAG = RemoteService.class.getSimpleName();

    private boolean isConnect = false;

    private Handler handler = new Handler(Looper.getMainLooper());
    private RemoteCallbackList<MessageReceiveListener> receiveListeners = new RemoteCallbackList<>();

    private ScheduledThreadPoolExecutor poolExecutor;
    private ScheduledFuture schedule;

    private IConnectionInterface connectIF = new IConnectionInterface.Stub() {
        @Override
        public void connect() throws RemoteException {
            try {
                Thread.sleep(5000);
                isConnect = true;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RemoteService.this,"connected",Toast.LENGTH_SHORT).show();
                    }
                });
                //模拟收到数据
                schedule = poolExecutor.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        int size = receiveListeners.beginBroadcast();
                        for (int i = 0; i < size; i++) {
                            Message message = new Message();
                            message.setContent("模拟数据");
                            try {
                                receiveListeners.getBroadcastItem(i).onReceive(message);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        receiveListeners.finishBroadcast();
                    }
                },5,5, TimeUnit.SECONDS);
                Log.i(TAG, "connect: ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void disconnect() throws RemoteException {
            isConnect = false;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this,"disconnect",Toast.LENGTH_SHORT).show();
                }
            });
            schedule.cancel(true);
            Log.i(TAG, "disconnect: ");
        }

        @Override
        public boolean isConnected() throws RemoteException {
            Log.i(TAG, "isConnected: ");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this,String.valueOf(isConnect),Toast.LENGTH_SHORT).show();
                }
            });
            return isConnect;
        }
    };

    private IMessageService service = new IMessageService.Stub() {
        @Override
        public void send(Message message) throws RemoteException {
            Log.i(TAG, "send: "+message.getContent());
            message.setSendSuccess(isConnect);
        }

        @Override
        public void registerListener(MessageReceiveListener listener) throws RemoteException {
            if(receiveListeners != null){
                receiveListeners.register(listener);
            }
        }

        @Override
        public void unregisterListener(MessageReceiveListener listener) throws RemoteException {
            if(receiveListeners != null){
                receiveListeners.unregister(listener);
            }
        }
    };

    private IServiceManager serviceManager = new IServiceManager.Stub() {
        @Override
        public IBinder getService(String name) throws RemoteException {
            if(IMessageService.class.getSimpleName().equals(name)){
                return service.asBinder();
            }
            else if(IConnectionInterface.class.getSimpleName().equals(name)){
                return connectIF.asBinder();
            }
            else return null;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return serviceManager.asBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        poolExecutor = new ScheduledThreadPoolExecutor(1);
    }
}