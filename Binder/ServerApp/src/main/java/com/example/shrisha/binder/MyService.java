package com.example.shrisha.binder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service {

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return new ActualWorker();
    }

    class ActualWorker extends IMyAidlInterface.Stub {

        @Override
        public int add(int num1, int num2) throws RemoteException {
            try {
                return num1 + num2;
            } catch (Exception e) {
                throw (RemoteException) e;
            }
        }
    }
}
