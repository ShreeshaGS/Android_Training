package com.example.shrisha.firsttask;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noData;
    private StudentDB studentDB;
    private StudentDisplayAdapter listAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_main);
        studentDB = new StudentDB(MainActivity.this, StudentDB.getTableStudents(), null, StudentDB.getDatabaseVersion());
        listAdapter = new StudentDisplayAdapter(MainActivity.this, studentDB.getStudentList());

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

        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);
    }

    @Override
    protected void onResume() {
        listAdapter.refreshList();
        if (studentDB.getRowCount() > 0) {

            //recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //recyclerView.setAdapter(listOfStudent);
            recyclerView.setVisibility(RecyclerView.VISIBLE);
            noData.setVisibility(TextView.GONE);

        } else {

            recyclerView.setVisibility(RecyclerView.GONE);
            noData.setVisibility(TextView.VISIBLE);

        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "Back button pressed", Toast.LENGTH_SHORT).show();
    }
}
