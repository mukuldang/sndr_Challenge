package com.example.the_apprentice.sndr;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private String current="";
    private RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
    private String m_root= Environment.getExternalStorageDirectory().getPath();
    private ArrayList<String> m_files,m_path,m_filesPath;
    final StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
    private String lastpath=m_root;
    private String currdir="";

    private int mode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Create File/Folder Function pending", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

                getDirFromRoot(m_root);




    }
    public void onlongclick(Integer position){
        File m_isFile = new File(m_path.get(position));
        String typedir = "";
        String size = "";
        if (m_isFile.isDirectory()) {
            typedir = "Directory";
            File[] files = m_isFile.listFiles(); // All files and subdirectories
            for (int i = 0; i < files.length; i++) {
                size += (files[i].length()); // Recursive call
            }
        }

        else{
            int m_lastIndex = m_isFile.getAbsolutePath().lastIndexOf(".");
            String m_filepath = m_isFile.getAbsolutePath();
            if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".png") || m_filepath.substring(m_lastIndex).equalsIgnoreCase(".jpg")) {
                typedir = "Image";
            } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".pdf")) {
                typedir = "PDF";

            } else {
                typedir = "File";
            }
            size = Long.toString(m_isFile.length());
        }
        String path = m_isFile.getAbsolutePath();
        String name = m_isFile.getName();


        Intent myIntent = new Intent(MainActivity.this, Profile.class);
        myIntent.putExtra("Name", name);
        myIntent.putExtra("path", path);
        myIntent.putExtra("size", size);
        myIntent.putExtra("typedir", typedir);//Optional parameters
        MainActivity.this.startActivity(myIntent);

    }
    public void onBackPressed() {
        if (currdir==m_root){
            finish();
        }
        Log.d("***********lastpath",lastpath);
        getDirFromRoot(lastpath);
    }

    public void onclick(Integer position){
        File m_isFile=new File(m_path.get(position));


        if(m_isFile.isDirectory())
        {
            current=m_isFile.toString();

                getDirFromRoot(m_isFile.toString());

        }
        else
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID, m_isFile);
            //String ext=FilenameUtils.getExtension(m_isFile.getAbsolutePath());

            intent.setDataAndType(uri, "application/*");
            PackageManager pm=MainActivity.this.getPackageManager();
            if (intent.resolveActivity(pm)!=null){
                startActivity(intent);
            }

            //String path=m_isFile.getAbsolutePath();
            //Uri ifile= FileProvider.getUriForFile(MainActivity.this,getString(R.string.file_provider_authority),m_isFile);
        }
    }

    public void getDirFromRoot(String p_rootPath)
    {
        current=p_rootPath;
        ArrayList<String> m_item = new ArrayList<String>();
        Boolean m_isRoot=true;
        m_path = new ArrayList<String>();
        m_files=new ArrayList<String>();
        m_filesPath=new ArrayList<String>();
        File m_file = new File(p_rootPath);
        Log.d("m_files", String.valueOf(m_file));
        File[] m_filesArray = m_file.listFiles();

        Log.d("filearray", String.valueOf(m_filesArray));
        setTitle(m_file.getName());
        if(!p_rootPath.equals(m_root))
        {
            m_item.add("../");
            m_path.add(m_file.getParent());
            lastpath=m_file.getParent();
            currdir=m_file.getAbsolutePath();
            m_isRoot=false;
        }

        String m_curDir = p_rootPath;
        //sorting file list in alphabetical order
        Arrays.sort(m_filesArray);
        for(int i=0; i < m_filesArray.length; i++)
        {
            File file = m_filesArray[i];
            if(file.isDirectory())
            {
                m_item.add(file.getName());
                m_path.add(file.getPath());
            }
            else
            {
                m_files.add(file.getName());
                m_filesPath.add(file.getPath());
            }
        }
        for(String m_AddFile:m_files)
        {
            m_item.add(m_AddFile);
        }
        for(String m_AddPath:m_filesPath)
        {
            m_path.add(m_AddPath);
        }
        mAdapter=new MyAdapter(this,m_item,m_path,m_isRoot);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id==R.id.checkable_menu) {

            if (!item.isChecked()) {
                ArrayList<String> m_item = new ArrayList<String>();
                Boolean m_isRoot = true;
                m_path = new ArrayList<String>();
                m_files = new ArrayList<String>();
                m_filesPath = new ArrayList<String>();
                File m_file = new File(current);
                Log.d("m_files", String.valueOf(m_file));
                File[] m_filesArray = m_file.listFiles();

                Log.d("filearray", String.valueOf(m_filesArray));
                setTitle(m_file.getName());
                if (!current.equals(m_root)) {
                    m_item.add("../");
                    m_path.add(m_file.getParent());
                    lastpath = m_file.getParent();
                    currdir = m_file.getAbsolutePath();
                    m_isRoot = false;
                }

                String m_curDir = current;
                //sorting file list in alphabetical order
                Arrays.sort(m_filesArray);
                for (int i = 0; i < m_filesArray.length; i++) {
                    File file = m_filesArray[i];
                    if (file.isDirectory()) {
                        m_item.add(file.getName());
                        m_path.add(file.getPath());
                    } else {
                        m_files.add(file.getName());
                        m_filesPath.add(file.getPath());
                    }
                }
                for (String m_AddFile : m_files) {
                    m_item.add(m_AddFile);
                }
                for (String m_AddPath : m_filesPath) {
                    m_path.add(m_AddPath);
                }

                recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                // use this setting to
                // improve performance if you know that changes
                // in content do not change the layout size
                // of the RecyclerView
                recyclerView.setHasFixedSize(true);
                // use a linear layout manager

                recyclerView.setLayoutManager(gridLayoutManager);

                mAdapter = new MyAdapter(this, m_item, m_path, m_isRoot);
                recyclerView.setAdapter(mAdapter);
                item.setChecked(true);

            } else {
                ArrayList<String> m_item = new ArrayList<String>();
                Boolean m_isRoot = true;
                m_path = new ArrayList<String>();
                m_files = new ArrayList<String>();
                m_filesPath = new ArrayList<String>();
                File m_file = new File(current);
                Log.d("m_files", String.valueOf(m_file));
                File[] m_filesArray = m_file.listFiles();

                Log.d("filearray", String.valueOf(m_filesArray));
                setTitle(m_file.getName());
                if (!current.equals(m_root)) {

                    m_item.add("../");
                    m_path.add(m_file.getParent());
                    lastpath = m_file.getParent();
                    currdir = m_file.getAbsolutePath();
                    m_isRoot = false;
                }
                String m_curDir = current;
                //sorting file list in alphabetical order
                Arrays.sort(m_filesArray);
                for (int i = 0; i < m_filesArray.length; i++) {
                    File file = m_filesArray[i];
                    if (file.isDirectory()) {
                        m_item.add(file.getName());
                        m_path.add(file.getPath());
                    } else {
                        m_files.add(file.getName());
                        m_filesPath.add(file.getPath());
                    }
                }
                for (String m_AddFile : m_files) {
                    m_item.add(m_AddFile);
                }
                for (String m_AddPath : m_filesPath) {
                    m_path.add(m_AddPath);
                }

                recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                // use this setting to
                // improve performance if you know that changes
                // in content do not change the layout size
                // of the RecyclerView
                recyclerView.setHasFixedSize(true);
                // use a linear layout manager

                recyclerView.setLayoutManager(layoutManager);

                mAdapter = new MyAdapter(this, m_item, m_path, m_isRoot);
                recyclerView.setAdapter(mAdapter);
                item.setChecked(false);
            }
        }
        return true;



    }
}

