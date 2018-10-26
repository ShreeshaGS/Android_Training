package com.example.shrisha.dummyserviceclient;

import android.app.Activity;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.IInterface;


public class MainActivity extends Activity {

    Button btnSendMsg;
    EditText inputMessage;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendMsg = findViewById(R.id.send_message);
        inputMessage = findViewById(R.id.editText);

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(0 == inputMessage.getText().toString().length()) {
                    inputMessage.setError("Field cannot be empty");
                } else {
                    try {

                    } catch (Exception e) {
                        Log.i(TAG, "Exception while calling service: " + e.getMessage());
                    }
                }
            }
        });
    }
}
