package com.example.shrisha.widgetsdemo;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FirstFragment extends Fragment {
    private String TAG = FirstFragment.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Called onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Called onCreateView");
        return inflater.inflate(R.layout.first_fragment, container, false);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Called onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "Called onResume");
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "Called onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Called onDestroy");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "Called onPause");
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "Called onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "Called onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "Called onDetach");
        super.onDetach();
    }
}
