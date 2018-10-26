package com.example.shrisha.repeatnotifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Button btnNotify;
    private Button btnEndNotify;
    private AlarmManager alarmManager = null;
    private PendingIntent pendingIntent;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNotify = findViewById(R.id.notify);
        btnEndNotify = findViewById(R.id.stopNotify);

        btnNotify.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick");

                if (alarmManager == null) {
                    Calendar calendar = Calendar.getInstance();

                    Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);

                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            100,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 10 * 1000, 10 * 1000, pendingIntent);
                    AlarmManager.AlarmClockInfo alarmClockInfo = alarmManager.getNextAlarmClock();
                    Log.d(TAG, "Trigger time in milli seconds " + alarmClockInfo.getTriggerTime());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Repeating notifications already set!!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                //alarmManager.cancel(pendingIntent);
            }
        });

        btnEndNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                } else {
                    Toast toast = Toast
                            .makeText(getApplicationContext(), "AlarmManager: " + alarmManager.toString(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    finish();
                }
            }
        });
    }
}
