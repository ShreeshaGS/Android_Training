package com.example.shrisha.firsttask;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class StudentInfoActivity extends AppCompatActivity {
    //private String TAG = StudentInfoActivity.class.getName();
    private TextInputEditText name;
    private TextInputEditText department;
    private TextInputEditText year;
    StudentDB studentDB;
    StudentDisplayAdapter listAdapter;

    public void setName(TextInputEditText name) {
        this.name = name;
    }

    public void setDepartment(TextInputEditText department) {
        this.department = department;
    }

    public void setYear(TextInputEditText year) {
        this.year = year;
    }

    private final String TAG = StudentInfoActivity.class.getName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        getSupportActionBar().setTitle("Student Information");
        studentDB = new StudentDB(StudentInfoActivity.this, StudentDB.getTableStudents(), null, StudentDB.getDatabaseVersion());

        name = (TextInputEditText) findViewById(R.id.name_field);
        department = (TextInputEditText) findViewById(R.id.dept_field);
        year = (TextInputEditText) findViewById(R.id.year_field);

        final int id = getIntent().getIntExtra("ID", -1);
        //listAdapter = (StudentDisplayAdapter) getIntent().getCharSequenceExtra("Adapter");

        if(id != -1) {

            Student student = studentDB.getStudentByID(id);
            if (student != null) {

                name.setText(student.getName());
                department.setText(student.getDept());
                year.setText(String.format(Locale.getDefault(), "%04d", student.getYear()));
            }
        }

        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FormIsValid()) {
                    if (id == -1) {

                        try {
                            studentDB.add_student(new Student(name.getText().toString(), department.getText().toString(), Integer.parseInt(year.getText().toString())));

                        } catch (SQLiteConstraintException e) {
                            //e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error inserting student information: Another student with same name exists. Try editing the student information with that name or insert student with different name.",
                                    Toast.LENGTH_LONG)
                                .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finish();

                    } else {

                        studentDB.updateByID(id, name.getText().toString(), department.getText().toString(), Integer.parseInt(year.getText().toString()));
                        finish();

                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean FormIsValid() {

        if (name.getText().toString().length() == 0 ) {
            Toast.makeText(StudentInfoActivity.this, "Name field cannot be empty", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return false;
        } else if (!name.getText().toString().matches("^[\\p{L} .'-]+$")) {
            Toast.makeText(StudentInfoActivity.this, "Name field contains invalid characters", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return false;
        }

        if (department.getText().toString().length() == 0 ) {
            Toast.makeText(StudentInfoActivity.this, "Department field cannot be empty", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return false;
        } else if (!department.getText().toString().matches("^[\\p{L} .'-]+$")) {
            Toast.makeText(StudentInfoActivity.this, "Department field contains invalid characters", Toast.LENGTH_SHORT).show();
            department.requestFocus();
            return false;
        }

        if (year.getText().toString().length() == 0 ) {
            Toast.makeText(StudentInfoActivity.this, "Year field cannot be empty", Toast.LENGTH_SHORT).show();
            year.requestFocus();
            return false;
        } else if (!year.getText().toString().matches("^[0-9]{4,4}+$")) {
            Toast.makeText(StudentInfoActivity.this, "Year field contains invalid characters", Toast.LENGTH_SHORT).show();
            year.requestFocus();
            return false;
        }

        return true;
    }
}
