package com.example.shrisha.activitylifecycleandnavigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity {
    private String TAG = "*****" + MainActivity.class.getName() + "*****";
    private final int REQUEST_SECOND_ACTIVITY = 1;
    private EditText editText;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("MainActivity");
        Log.d(TAG, "Called onCreate");

        Button button = findViewById(R.id.button_first);
        Button bundleDataButton = findViewById(R.id.bundle_data_button);
        Button parcelDataButton = findViewById(R.id.parcel_data_button);

        editText = findViewById(R.id.text_input);
        textView = findViewById(R.id.textView_two);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().length() > 0) {
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra("MainActivityInput", editText.getText().toString());
                    intent.putExtra("requestCode", REQUEST_SECOND_ACTIVITY);
                    startActivityForResult(intent, REQUEST_SECOND_ACTIVITY);
                } else {
                    Toast.makeText(MainActivity.this, "Field cannot be empty", Toast.LENGTH_LONG).show();
                    editText.requestFocus();
                }
            }
        });

        bundleDataButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(editText.getText().length()>0){
                    Intent intent = new Intent(MainActivity.this, DataActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("bundle_data", editText.getText().toString());
                    intent.putExtra("data","bundle");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    editText.setError("Field cannot be empty");
                }
            }
        });

        parcelDataButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(editText.getText().length()>0){
                    Intent intent = new Intent(MainActivity.this, DataActivity.class);
                    Bundle parcelable = new Bundle();
                    parcelable.putParcelable("parcel_data", (Parcelable) editText.getText());
                    //intent.putExtra(, parcelable);
                    intent.putExtra("data","parcel");
                    intent.putExtras(parcelable);
                    startActivity(intent);
                } else {
                    editText.setError("Field cannot be empty");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final int REQUEST_NEXT_ACTIVITY = 2;
        switch (requestCode) {
            case REQUEST_SECOND_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {

                    if (data != null) {
                        textView.setText(data.getStringExtra("ReturnString"));

                        Button quit_button = findViewById(R.id.button_quit);
                        if (quit_button.getVisibility() == Button.INVISIBLE) {
                            quit_button.setText("Quit");
                            quit_button.setVisibility(Button.VISIBLE);
                            quit_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MainActivity.this.finish();
                                }
                            });
                        }

                    } else {
                        textView.setText("Sample text");

                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {

                    textView.setText("Second Activity returned nothing");
                    textView.setText(getIntent().getStringExtra("ReturnString"));
                } else {

                    textView.setText("Something else happened");
                }
                break;

            case REQUEST_NEXT_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {

                    textView.setText(getIntent().getStringExtra("IndirectReturn"));

                } else if (resultCode == Activity.RESULT_CANCELED) {

                    textView.setText("Second Activity returned nothing");
                } else {

                    textView.setText("Something else happened");
                }
                break;
        }
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
