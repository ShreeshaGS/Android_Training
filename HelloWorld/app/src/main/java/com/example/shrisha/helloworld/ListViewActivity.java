package com.example.shrisha.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import java.util.ArrayList;

public class ListViewActivity extends Activity {

    ListView mListView;
    ListAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        mListView = (ListView) findViewById(R.id.list_data);

       ArrayList<Student> students = new ArrayList<>();
       students.add(new Student("Ram","45567468789","ECE"));
        students.add(new Student("Rahim","4564564","ECE"));
        students.add(new Student("test","454646654","ECE"));
        students.add(new Student("test1","4525423234","ECE"));
        students.add(new Student("test2","4565645","ECE"));



        mAdapter = new ListAdapter(students,getApplicationContext());
        mListView.setAdapter(mAdapter);

    }
}
