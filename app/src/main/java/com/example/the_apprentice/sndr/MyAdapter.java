package com.example.the_apprentice.sndr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by the-apprentice on 1/17/18.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>implements View.OnClickListener {
    private List<String> m_item;
    private List<String> m_path;
    public ArrayList<Integer> m_selectedItem;
    private String m_root= Environment.getExternalStorageDirectory().getPath();
    private ArrayList<String> m_files,m_filesPath;
    private List<String> c_path;
    private String lastpath="";
    private String currdir="";
    private  Integer mode=0;
    Context m_context;
    Boolean m_isRoot;

    public MyAdapter(Context p_context, List<String> p_item, List<String> p_path, Boolean p_isRoot) {
        m_context=p_context;
        m_item=p_item;
        mode=mode;
        m_path=p_path;
        m_selectedItem=new ArrayList<Integer>();
        m_isRoot=p_isRoot;
    }




    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());



            View v = inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    public Object getItem(int position) {
        return m_item.get(position);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.filename.setText(m_item.get(position));
            holder.filedate.setText(getLastDate(position));

            holder.r1.setTag(position);


        try {
            holder.icon.setImageResource(setimage(new File(m_path.get(position))));
        } catch (IOException e) {
            e.printStackTrace();
        }


        holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    m_selectedItem.add(position);
                }
                else
                {
                    m_selectedItem.remove(m_selectedItem.indexOf(position));
                }
            }
        });

    }

    public String getLastDate(int p_pos)
    {
        File m_file=new File(m_path.get(p_pos));
        SimpleDateFormat m_dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return m_dateFormat.format(m_file.lastModified());
    }

    @Override
    public int getItemCount() {
        return m_item.size();
    }
    public int setimage(File m_file) throws IOException {
        int m_lastIndex=m_file.getAbsolutePath().lastIndexOf(".");

        String m_filepath=m_file.getAbsolutePath();

        if (m_file.isDirectory())
            return R.mipmap.ic_launcher_folder;
        else
        {   String mime= Files.probeContentType(m_file.toPath());
            if (mime!=null && mime.split("/")[0].equals("image")){

                return R.mipmap.ic_launcher_image;
            }

            else {
                return R.mipmap.ic_launcher_file;
            }}
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView filename,filedate;
        public ImageView icon;
        public RelativeLayout r1;
        public CheckBox check_box;
        public View layout;
        public ViewHolder(View v) {
            super(v);
            layout=v;
            r1=(RelativeLayout)v.findViewById(R.id.RelativeLayout1);
            filename=(TextView) v.findViewById(R.id.filename);
            filedate=(TextView)v.findViewById(R.id.filedate);
            icon=(ImageView)v.findViewById(R.id.icon);
            check_box=(CheckBox)v.findViewById(R.id.check_box);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    ((MainActivity)m_context).onclick(position);
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position=getAdapterPosition();
                    ((MainActivity)m_context).onlongclick(position);
                    return false;
                }
            });

        }

}}
