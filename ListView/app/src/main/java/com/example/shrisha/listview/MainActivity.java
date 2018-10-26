package com.example.shrisha.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    String[] str={"monday","tuesday","wednesday","thursday","friday","saturday","sunday"};
    //String weeks ="monday";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_item);

        ArrayAdapter<String> madapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2, str);
        listView.setAdapter(madapter);
    }
}
