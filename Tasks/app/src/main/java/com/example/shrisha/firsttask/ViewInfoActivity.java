package com.example.shrisha.firsttask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewInfoActivity extends AppCompatActivity {
    StudentDB studentDB;
    TextView name;
    TextView department;
    TextView year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info);
        studentDB = new StudentDB(ViewInfoActivity.this, StudentDB.getTableStudents(), null, StudentDB.getDatabaseVersion());

        name = findViewById(R.id.name);
        department = findViewById(R.id.dept);
        year = findViewById(R.id.year);

        name.setText(getIntent().getStringExtra("Name"));
        department.setText(getIntent().getStringExtra("Department"));
        year.setText(getIntent().getStringExtra("Year"));

    }
}
