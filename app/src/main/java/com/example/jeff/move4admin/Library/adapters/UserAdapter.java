package com.example.jeff.move4admin.Library.adapters;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeff.move4admin.Library.DatabaseFunctions;
import com.example.jeff.move4admin.Library.User;
import com.example.jeff.move4admin.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Jeff on 3-12-2014.
 */
public class UserAdapter extends BaseAdapter {

    ArrayList<User> userlist = new ArrayList<User>();
    private LayoutInflater mInflater;
    private String savedPath;
    Context mContext;

    public UserAdapter(Context context,ArrayList<User> u)
    {
       mContext = context;
       userlist =u;
       mInflater = LayoutInflater.from(context);
       ContextWrapper cw = new ContextWrapper(context);
       savedPath = cw.getDir("imageDir", Context.MODE_PRIVATE).toString();
    }

    @Override
    public int getCount() {
       return userlist.size();
    }

    @Override
    public Object getItem(int i) {
        return userlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View ConvertView, ViewGroup viewGroup) {

        View view;
        ViewHolder holder;
        if(ConvertView == null) {
            view = mInflater.inflate(R.layout.row_layout, viewGroup, false);
            holder = new ViewHolder();
            holder.avatar = (ImageView)view.findViewById(R.id.avatar);
            holder.name = (TextView)view.findViewById(R.id.name);
            holder.latestMessage = (TextView)view.findViewById(R.id.latest_message);
            view.setTag(holder);
        } else {
            view = ConvertView;
            holder = (ViewHolder)view.getTag();
        }

        User u = userlist.get(i);
        holder.name.setText(u.getName());
        holder.latestMessage.setText(u.getLastName());

        try {
            File f = new File(savedPath, u.getFilePath().substring(7));
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            b = Bitmap.createScaledBitmap(b, 350, 350, true);
            holder.avatar.setImageBitmap(b);
        } catch (Exception e) {
            Bitmap noimg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nopic);
            noimg = Bitmap.createScaledBitmap(noimg, 350, 350, true);
            holder.avatar.setImageBitmap(noimg);
        }
        return view;
    }

    private class ViewHolder {
        public ImageView avatar;
        public TextView name, latestMessage;
    }
}
