package com.example.shrisha.firsttask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class StudentDisplayAdapter extends RecyclerView.Adapter<StudentDisplayAdapter.StudentViewHolder> {

    String TAG = StudentDisplayAdapter.class.getSimpleName();
    private ArrayList<Student> studentList;
    private Context context;
    private StudentDB studentDB;

    public StudentDisplayAdapter(Context context, ArrayList<Student> studentList) {
        this.studentList = studentList;
        this.context = context;
        studentDB = new StudentDB(context, StudentDB.getTableStudents(), null, StudentDB.getDatabaseVersion());

        RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                refreshList();
            }
        };

        registerAdapterDataObserver(adapterDataObserver);
    }

    public StudentDisplayAdapter(Context context, StudentDB studentDB) {
        this.studentList = studentDB.getStudentList();
        this.context = context;
        this.studentDB = studentDB;

        RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                refreshList();
            }
        };

        registerAdapterDataObserver(adapterDataObserver);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View studentRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_info_row, parent, false);
        return new StudentViewHolder(studentRow);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.studentName.setText(student.getName());
        holder.department = student.getDept();
        holder.year = student.getYear();
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }



    public void refreshList() {
        studentList.clear();
        studentList.addAll(studentDB.getStudentList());

        //notifyDataSetChanged();

    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        String department;
        Integer year;

        public StudentViewHolder(final View view) {
            super(view);
            this.studentName = (TextView) view.findViewById(R.id.student_name);

            view.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(final View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                    alertDialog.setPositiveButton("Edit Student Information", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, StudentInfoActivity.class);
                            intent.putExtra("ID", studentDB.getIDByStudentInfo(studentName.getText().toString(), department, year));
                            //refreshList();
                            notifyDataSetChanged();
                            context.startActivity(intent);
                        }
                    });
                    alertDialog.setNegativeButton("Delete Student Information", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            studentDB.delete_student(studentDB.getStudentByID(studentDB.getIDByStudentInfo(studentName.getText().toString(), department, year)));
                            //refreshList();
                            notifyDataSetChanged();

                        }
                    });
                    alertDialog.setMessage("Alert dialog message");
                    alertDialog.setTitle("Edit / Delete");
                    alertDialog.show();
                    return true;
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewInfoActivity.class);
                    /*intent.putExtra("Name", student.getName());
                    intent.putExtra("Department", student.getDept());
                    intent.putExtra("Year", String.format("%04d", student.getYear()));*/
                    intent.putExtra("Name", studentName.getText().toString());
                    intent.putExtra("Department", department);
                    intent.putExtra("Year", String.format("%04d", year));
                    context.startActivity(intent);
                }
            });
        }
    }

    public void showAllOnLog() {
        Log.d(TAG, String.format(Locale.getDefault(), "%d", getItemCount()));
        for(Student student: studentList) {
            Log.d(TAG, student.toString());
        }
    }
}
