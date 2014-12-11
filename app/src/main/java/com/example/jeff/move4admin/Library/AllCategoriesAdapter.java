package com.example.jeff.move4admin.Library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jeff.move4admin.R;

import java.util.ArrayList;

/**
 * Created by Jeff on 10-12-2014.
 */
public class AllCategoriesAdapter extends BaseAdapter {
    ArrayList<Like> likes = new ArrayList<Like>();
    Context mContext;
    private LayoutInflater mInflater;

    public AllCategoriesAdapter(Context c, ArrayList<Like> l)
    {
        mContext = c;
        mInflater = LayoutInflater.from(c);
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
            holder.title = (TextView)view.findViewById(R.id.t_likesrow_name);
            view.setTag(holder);
        } else {
            view = ConvertView;
            holder = (ViewHolder)view.getTag();
        }

        holder.title.setText(likes.get(i).getcategoryName());

        return view;

    }


    private class ViewHolder {
        public TextView title;
    }

}
