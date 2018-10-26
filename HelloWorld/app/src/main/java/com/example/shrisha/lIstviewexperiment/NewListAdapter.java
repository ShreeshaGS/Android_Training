package com.example.shrisha.lIstviewexperiment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shrisha.helloworld.R;

import java.util.ArrayList;

public class NewListAdapter extends ArrayAdapter<Contacts> {

    ArrayList<Contacts> contacts;
    Context context;

    private class MyViewHolder {
        TextView name;
        TextView number;
    }

    public NewListAdapter(@NonNull Context context, int resource, ArrayList<Contacts> contacts) {
        super(context, resource);
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contacts contact = contacts.get(position);
        final View view;
        MyViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MyViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.new_activity_main, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.exp_contact_name);
            viewHolder.number = (TextView) convertView.findViewById(R.id.exp_contact_number);
            view = convertView;
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
            view = convertView;
        }

        viewHolder.name.setText(String.format("Name: %s", contact.contactName));
        viewHolder.number.setText(String.format("Name: %s", contact.contactNumber));
        return view;
    }
}
