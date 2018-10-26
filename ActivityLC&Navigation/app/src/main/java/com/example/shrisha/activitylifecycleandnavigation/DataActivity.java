package com.example.shrisha.activitylifecycleandnavigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        TextView textView = findViewById(R.id.textView);

        switch (getIntent().getStringExtra("data")) {
            case "bundle":
                textView.setText(getIntent().getExtras().getString("bundle_data"));
                break;

            case "parcel":
                textView.setText(getIntent().getParcelableExtra("parcel_data").toString());
                break;

            case "serial":
                textView.setText(getIntent().getSerializableExtra("serial_data").toString());
                break;

            default:
                finish();
                break;
        }
    }
}
