package com.example.aidl.service;


import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.aidl.IAIDLListener;
import com.example.aidl.IAIDLManager;
import com.example.aidl.MainActivity;

public class DemoIPCService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private Binder binder = new IAIDLManager.Stub() {

        @Override
        public void register() throws RemoteException {

        }

        @Override
        public void unregister() throws RemoteException {

        }

        @Override
        public Dto getDataFromIPC() throws RemoteException {
            Dto dto = new Dto();
            dto.setData1("我是一个来自aidl服务端提供的数据");
            return dto;
        }

        @Override
        public void setDataToIPC(Dto data1, IAIDLListener listener) throws RemoteException {
            Handler handler = new Handler(getMainLooper());
            if (listener != null) {
                listener.onSuccess("服务端已收到-" + data1.getData1());
            }
            handler.post(() -> Toast.makeText(getApplicationContext(), data1.getData1(), Toast.LENGTH_SHORT).show());
        }

        @Override
        public void startIPCActivity(int code) throws RemoteException {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };
}
