package com.example.shrisha.firsttask;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noData;
    private StudentDB studentDB;
    private StudentDisplayAdapter listAdapter;
    private String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate( final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "CALLED onCreate");
        studentDB = new StudentDB(MainActivity.this, StudentDB.getTableStudents(), null, StudentDB.getDatabaseVersion());
        //listAdapter = new StudentDisplayAdapter(MainActivity.this, studentDB.getStudentList());
        listAdapter = new StudentDisplayAdapter(MainActivity.this, studentDB);

        FloatingActionButton addStudent = (FloatingActionButton) findViewById(R.id.add_student);
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StudentInfoActivity.class);
                startActivity(intent);
            }
        });

        //listAdapter.hasStableIds();

        recyclerView = (RecyclerView) findViewById(R.id.full_list);
        noData = (TextView) findViewById(R.id.no_data);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);


    }

    @Override
    protected void onResume() {
        Log.d(TAG, "CALLED onResume");

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast toast = Toast.makeText(getApplicationContext(), "Back button pressed", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        studentDB.close();
        Log.d(TAG, "CALLED onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "CALLED onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "CALLED onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "CALLED onDestroy");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            Log.d(TAG, "Activity gained Focus");
            listAdapter.notifyDataSetChanged();
            //listAdapter.showAllOnLog();

            if (listAdapter.getItemCount() > 0) {

                recyclerView.setVisibility(RecyclerView.VISIBLE);
                noData.setVisibility(TextView.GONE);

            } else {

                recyclerView.setVisibility(RecyclerView.GONE);
                noData.setVisibility(TextView.VISIBLE);

            }

        } else {
            Log.d(TAG, "Activity lost Focus");
        }
    }
}
