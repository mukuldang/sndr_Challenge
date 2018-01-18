package com.example.the_apprentice.temp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        String path = intent.getStringExtra("path");
        String type = intent.getStringExtra("typedir");
        String size = intent.getStringExtra("size");

        TextView name_1=(TextView)findViewById(R.id.Name);
        name_1.setText("Filename : " +name);
        TextView path_1=(TextView)findViewById(R.id.Path);
        path_1.setText("Absolute Path : "+path);
        TextView type_1=(TextView)findViewById(R.id.Type);
        type_1.setText("File Type : "+type);
        TextView size_1=(TextView)findViewById(R.id.Size);
        size_1.setText("File Size : "+size);
    }

}

