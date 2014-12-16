package com.example.jeff.move4admin.Library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeff.move4admin.R;

import java.util.ArrayList;

/**
 * Created by Jeff on 10-12-2014.
 */
public class LikesAdapter extends BaseAdapter {
    ArrayList<String> likes = new ArrayList<String>();
    Context mContext;
    private LayoutInflater mInflater;

    public LikesAdapter(Context context,ArrayList<String> l)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        likes = l;
    }

    @Override
    public int getCount() {
        return likes.size();
    }

    @Override
    public Object getItem(int i) {
        return likes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View ConvertView, ViewGroup viewGroup) {
        View view;
        ViewHolder holder;

        if(ConvertView == null) {
            view = mInflater.inflate(R.layout.likesrow_layout, viewGroup, false);
            holder = new ViewHolder();
            holder.name = (TextView)view.findViewById(R.id.t_likesrow_name);
            view.setTag(holder);
        } else {
            view = ConvertView;
            holder = (ViewHolder)view.getTag();
        }

        holder.name.setText(likes.get(i));

        return view;
    }

    private class ViewHolder {
        public TextView name;
    }

}
