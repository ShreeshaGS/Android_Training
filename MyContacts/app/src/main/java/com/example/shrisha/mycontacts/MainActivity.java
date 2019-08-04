package com.example.shrisha.mycontacts;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView contactList;
    public static final int requestContactsPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = findViewById(R.id.contact_list);

        EnableRuntimePermission();
    }

    private void EnableRuntimePermission() {

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_CONTACTS},
                requestContactsPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case requestContactsPermissionCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,
                            "Permission Granted", Toast.LENGTH_SHORT)
                            .show();

                    ContactListAdapter contactListAdapter = new ContactListAdapter(getContacts());
                    contactList.setAdapter(contactListAdapter);
                    //contactList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    contactList.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, LinearLayoutManager.VERTICAL, false));
                    //contactList.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

                } else {
                    Toast.makeText(MainActivity.this,
                            "Permission Denied", Toast.LENGTH_SHORT)
                            .show();

                    Toast.makeText(MainActivity.this,
                            "Contact permission allows us to access contacts from your phone", Toast.LENGTH_SHORT)
                            .show();

                    Snackbar.make(findViewById(R.id.activity_main),
                            "Contacts permission is required for this app to run.", Snackbar.LENGTH_INDEFINITE)
                            .setAction("TRY AGAIN", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EnableRuntimePermission();
                                }
                            })
                            .show();
                }
                break;

        }
    }

    public ArrayList<ContactItem> getContacts() {
        ArrayList<ContactItem> contactItems = new ArrayList<>();

        Cursor cursor = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        while (cursor.moveToNext()) {
            contactItems.add(new ContactItem(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));

        }

        cursor.close();
        return contactItems;
    }
}
