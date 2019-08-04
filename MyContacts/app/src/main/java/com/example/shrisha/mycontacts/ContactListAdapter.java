package com.example.shrisha.mycontacts;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>{
    private ArrayList<ContactItem> contactItems;
    private Random randomColor;

    public ContactListAdapter(ArrayList<ContactItem> contactItems) {
        this.contactItems = contactItems;
        randomColor = new Random();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactRow = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.contact_items_listview, parent, false);
        return new ContactViewHolder(contactRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactItem contactItem = contactItems.get(position);
        holder.contactName.setText(contactItem.getContactName());
        holder.contactNumber.setText(contactItem.getContactNumber());
        holder.itemView.setBackgroundColor(
                Color.argb (180,
                        randomColor.nextInt(256),
                        randomColor.nextInt(256),
                        randomColor.nextInt(256)));
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView contactNumber;

        public ContactViewHolder(View contactRow) {
            super(contactRow);
            this.contactName = (TextView) contactRow.findViewById(R.id.contact_name);
            this.contactNumber = (TextView) contactRow.findViewById(R.id.contact_number);
        }
    }
}
