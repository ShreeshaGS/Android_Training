package com.example.shrisha.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    EditText editText1,editText2,editText3;
    TextView textView1,textView2,textView3,result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editText1 = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText5);

        textView1 = (TextView) findViewById(R.id.textView2);
        textView2 = (TextView) findViewById(R.id.textView3);
        textView3 = (TextView) findViewById(R.id.textView4);
        result    = (TextView) findViewById(R.id.textView5);

    }
    public void show(View v){
        String name = editText1.getText().toString();
        String dept = editText2.getText().toString();
        String year = editText3.getText().toString();

        result.setText("student details:"+name+" "+dept+" "+year);
    }
}
