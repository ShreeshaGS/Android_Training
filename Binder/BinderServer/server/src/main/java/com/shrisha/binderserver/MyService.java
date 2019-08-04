package com.shrisha.binderserver;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.shrisha.common.IMyAidlInterface;

public class MyService extends Service {
    private static final String TAG = "MyAIDLService";
    private MyAidlImpl myAidl = new MyAidlImpl();

    private Context ctx;
    private View rootView;
    private TextView param_1;
    private TextView param_2;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        try {
            return myAidl;
        }catch (Exception e) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    private class MyAidlImpl extends IMyAidlInterface.Stub {

        @Override
        public int add(int num1, int num2) throws RemoteException {
            ctx = getApplicationContext();
            /*Log.d(TAG, "add: setting context");
            param_1 = (TextView) ((Activity)ctx).findViewById(R.id.param_1);
            param_2 = (TextView) ((Activity)ctx).findViewById(R.id.param_2);
            Log.d(TAG, "add: getting parameter fields");
            param_1.setText("Parameter 1: " + String.valueOf(num1));
            param_2.setText("Parameter 2: " + String.valueOf(num2));
            Log.d(TAG, "add: returning result");*/

            //rootView = View.inflate(ctx, R.layout.activity_main, null);
            /*rootView = LayoutInflater.from(ctx).inflate(R.layout.activity_main, null);

            param_1 = (TextView) rootView.findViewById(R.id.param_1);
            param_2 = (TextView) rootView.findViewById(R.id.param_2);
            Log.d(TAG, "add: getting parameter fields");
            param_1.setText("Parameter 1: " + String.valueOf(num1));
            param_2.setText("Parameter 2: " + String.valueOf(num2));
            Log.d(TAG, "add: returning result");*/
            return (num1 + num2);
        }
    }
}
