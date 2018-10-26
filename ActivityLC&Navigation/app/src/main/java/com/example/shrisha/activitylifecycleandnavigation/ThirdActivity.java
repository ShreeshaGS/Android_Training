package com.example.shrisha.activitylifecycleandnavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class ThirdActivity extends AppCompatActivity {
    private String TAG = "*****" + ThirdActivity.class.getName() + "*****";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        try {
            getSupportActionBar().setTitle("ThirdActivity");
        } catch (Exception e) {
            //e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        Log.d(TAG, "Called onCreate");
        TextView textView = findViewById(R.id.message_third);
        textView.setText(getIntent().getStringExtra("SecondString"));
        final EditText editText = findViewById(R.id.editText_third);
        Button button = findViewById(R.id.button_third);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editText.getText())) {
                    editText.setError("Field cannot be empty");
                } else {
                    Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
                    intent.putExtra("IndirectReturn", editText.getText().toString());
                    //intent.putExtra("IndirectReturn", new Bundle().putString("IndirectReturn", editText.getText().toString()));
                    setResult(RESULT_OK, intent);
                    //finishActivity(2);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Called onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Called onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Called onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Called onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Called onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "Called onRestart");
    }
}
