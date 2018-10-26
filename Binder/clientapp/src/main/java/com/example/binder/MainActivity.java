package com.example.binder;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button add;
    EditText field1;
    EditText field2;
    TextView answer;

    IMyAidlInterface iMyAidlInterface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.btnAdd);
        field1 = findViewById(R.id.field1);
        field2 = findViewById(R.id.field2);
        answer = findViewById(R.id.textAnswer);

        Intent intent = new Intent("MyOwnAction");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int num1 = Integer.parseInt(field1.getText().toString());
                    int num2 = Integer.parseInt(field2.getText().toString());
                    int result = iMyAidlInterface.add(num1, num2);

                    answer.setText(String.format("%d", result));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iMyAidlInterface = null;
        }
    };
}
