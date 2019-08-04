package com.globaledge.mapboxdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mapbox.android.gestures.Utils;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;

import com.globaledge.mapboxdemo.Utils.Tokens;

public class MainActivity extends AppCompatActivity {

    MapboxNavigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = new MapboxNavigation(MainActivity.this, Tokens.getDefault_public_token());
    }
}
