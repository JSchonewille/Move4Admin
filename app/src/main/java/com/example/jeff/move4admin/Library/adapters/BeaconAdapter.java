package com.example.jeff.move4admin.Library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jeff.move4admin.Library.Beacon;
import com.example.jeff.move4admin.R;

import java.util.ArrayList;

/**
 * Created by Jeff on 18-12-2014.
 */
public class BeaconAdapter extends BaseAdapter {
    ArrayList<Beacon> beacons = new ArrayList<Beacon>();
    Context mContext;
    private LayoutInflater mInflater;

    public BeaconAdapter(Context c,ArrayList<Beacon> b)
    {
        beacons = b;
        mInflater = LayoutInflater.from(c);
        mContext = c;
    }

    @Override
    public int getCount() {
        return beacons.size();
    }

    @Override
    public Object getItem(int i) {
        return beacons.get(i);
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

        holder.name.setText(Integer.toString(beacons.get(i).getMinor()));

        return view;
    }

    private class ViewHolder {
        public TextView name;
    }

}
