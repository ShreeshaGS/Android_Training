package com.example.shrisha.activitylifecycleandnavigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    private String TAG = "*****" + SecondActivity.class.getName() + "*****";
    private int requestCode;
    private EditText editText;
    private final int REQUEST_NEXT_ACTIVITY = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d(TAG, "Called onCreate");
        getSupportActionBar().setTitle("SecondActivity");


        String message = getIntent().getStringExtra("MainActivityInput");
        TextView textMessage = findViewById(R.id.message_second);
        textMessage.setText(message);

        editText = findViewById(R.id.editText_second);

        Button button = findViewById(R.id.button_second);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editText.getText())) {

                    Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                    intent.putExtra("SecondString", editText.getText().toString());

                    startActivityForResult(intent, REQUEST_NEXT_ACTIVITY);

                } else {
                    editText.setError("Input field cannot be empty");
                    editText.requestFocus();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        intent.putExtra("ReturnString", editText.getText().toString());
        setResult(Activity.RESULT_CANCELED, intent);
        //finishActivity(requestCode);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_NEXT_ACTIVITY:

                switch (resultCode) {
                    case Activity.RESULT_OK:

                        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                        //intent.putExtra("ReturnString", getIntent().getStringExtra("IndirectReturn"));
                        //data.putExtra("ReturnString", data.getStringExtra("IndirectReturn"));
                        if (data != null) {
                            intent.putExtra("ReturnString", data.getStringExtra("IndirectReturn"));
                            //intent.putExtras(getIntent().getExtras());
                        } else {
                            intent.putExtra("ReturnString", "Sample Data");
                        }
                        setResult(RESULT_OK, intent);
                        finish();
                        /*try {
                            finishActivity(REQUEST_NEXT_ACTIVITY);
                            if(!isFinishing()) {
                                throw new RuntimeException("finishActivity() not working. Proceeding to use finish()");
                            }

                        } catch (RuntimeException e) {
                            Toast.makeText(SecondActivity.this,
                                    e.getMessage() + "",
                                    Toast.LENGTH_LONG)
                                    .show();
                            finish();

                        } catch (Exception e) {
                            Toast.makeText(SecondActivity.this,
                                    e.getMessage(),
                                    Toast.LENGTH_SHORT)
                                    .show();

                        }*/
                        break;

                    case RESULT_CANCELED:

                        intent = new Intent(SecondActivity.this, MainActivity.class);
                        intent.putExtra("ReturnString", editText.getText().toString());

                        setResult(RESULT_OK, intent);

                        try {
                            finishActivity(REQUEST_NEXT_ACTIVITY);
                            if(!isFinishing()) {
                                throw new RuntimeException("finishActivity() not working. Proceeding to use finish()");
                            }
                        } catch (RuntimeException e) {
                            Toast.makeText(SecondActivity.this,
                                    e.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                            finish();
                        }
                        break;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Called onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Called onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Called onStop");
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
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "Called onRestart");
    }
}
