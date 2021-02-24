package com.slq.r1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.slq.r1.adapter.MyArrayAdapter;
import com.slq.r1.pojo.News;

import java.util.ArrayList;

public class MyContactsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts_list);
        ListView lv = findViewById(R.id.testListView);
        ArrayList<News> objs = requestPermiIfNeed();
        MyArrayAdapter adapter = new MyArrayAdapter(getApplicationContext(), R.layout.newsitem, objs);
        lv.setAdapter(adapter);

        Button gotonews = (Button) findViewById(R.id.gotonews);
        gotonews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.startActivity0(MyContactsListActivity.this);
            }
        });
    }

    public ArrayList<News> requestPermiIfNeed() {
        int i = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (i != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 666);
            return null;
        } else {
            return readContacts();
        }
    }

    public ArrayList<News> readContacts() {
        ArrayList<News> objs = new ArrayList<News>();
        ContentResolver resolver = getContentResolver();
        int i = 1;
        Cursor query = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null);
        if (query != null) {
            while (query.moveToNext()) {
                String name = query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        + "\n\t\t" + query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //Log.e(TAG, "readContacts: " + name);
                objs.add(new News(name, i++));
            }
        }
        return objs;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 666) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts();
            } else {
                Toast.makeText(this, "你拒绝授权读写联系人", Toast.LENGTH_SHORT).show();
            }
        }
    }
}