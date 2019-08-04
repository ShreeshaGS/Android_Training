package com.shrisha.binderclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shrisha.common.IMyAidlInterface;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();

    private EditText editText_num1;
    private EditText editText_num2;
    private Button btn_add;
    private TextView textView_answer;

    private ServiceConnection serviceConnection;
    private IMyAidlInterface iMyAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_num1 = (EditText) findViewById(R.id.text_num1);
        editText_num2 = (EditText) findViewById(R.id.text_num2);
        btn_add = (Button) findViewById(R.id.btn_add);
        textView_answer = (TextView) findViewById(R.id.textView_result);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        setVisibility(false);
        Log.d(TAG, "onCreate: Done Initialising objects..");
    }

    private void setVisibility(@NonNull boolean b) {
        editText_num1.setEnabled(b);
        editText_num2.setEnabled(b);
        btn_add.setEnabled(b);

        editText_num1.setClickable(b);
        editText_num2.setClickable(b);
        btn_add.setClickable(b);
    }

    public void bindToService(View view) {
        setVisibility(true);
        Intent intent = new Intent("com.shrisha.server.AIDL");
        bindService(convertImplictIntentToExplicit(intent), serviceConnection, BIND_AUTO_CREATE);
        Log.d(TAG, "bindToService: Binding to service: com.shrisha.server.AIDL");
    }

    public void invokeServiceMethod(View view) {
        if("".equals(editText_num1.getText().toString())) {
            editText_num1.requestFocus();
            editText_num1.setError("Field cannot be empty");
            return;
        }
        if("".equals(editText_num2.getText().toString())) {
            editText_num2.requestFocus();
            editText_num2.setError("Field cannot be empty");
            return;
        }

        Log.d(TAG, "invokeServiceMethod: Calling remote method");

        try {
            textView_answer.setText(
                    String.valueOf( iMyAidlInterface.add(
                            Integer.parseInt(editText_num1.getText().toString()),
                            Integer.parseInt(editText_num2.getText().toString()))));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "invokeServiceMethod: Finished calling remote method");
    }

    private Intent convertImplictIntentToExplicit(Intent implicit) {
        Intent explicitIntent;
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(implicit, 0);
        if (resolveInfoList == null || resolveInfoList.size() != 1) {
            return null;
        }

        ResolveInfo resolveInfo = resolveInfoList.get(0);
        ComponentName componentName = new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
        explicitIntent = new Intent(implicit).setComponent(componentName);
        return explicitIntent;
    }
}
