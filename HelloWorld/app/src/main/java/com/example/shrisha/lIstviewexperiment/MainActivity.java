package com.example.shrisha.lIstviewexperiment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.shrisha.helloworld.R;
import com.example.shrisha.helloworld.Student;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView listView;

    private NewListAdapter contactsAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.exp_list);
        setContentView(R.layout.new_activity_main);

        ArrayList<Contacts> contacts = new ArrayList<>();
        contacts.add(new Contacts("Harish", 1234567890));
        contacts.add(new Contacts("Mahesh", 1876543210));
        contactsAdapter = new NewListAdapter(this, R.layout.new_activity_main, contacts);
        listView.setAdapter(contactsAdapter);
    }
}
