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

import com.example.jeff.move4admin.Library.Product;
import com.example.jeff.move4admin.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by Jeff on 16-12-2014.
 */
public class ProductAdapter extends BaseAdapter {
    ArrayList<Product> products = new ArrayList<Product>();
    private LayoutInflater mInflater;
    private String savedPath;
    Context mContext;

    public ProductAdapter(Context context,ArrayList<Product> p)
    {
        mContext = context;
        products =p;
        mInflater = LayoutInflater.from(context);
        ContextWrapper cw = new ContextWrapper(context);
        savedPath = cw.getDir("imageDir", Context.MODE_PRIVATE).toString();
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
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

        Product p = products.get(i);
        holder.name.setText(p.getName());
        //holder.latestMessage.setText(u.getLastName());

        try {
            File f = new File(savedPath, p.getImage().substring(7));
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            b = Bitmap.createScaledBitmap(b, 350, 350, true);
            holder.avatar.setImageBitmap(b);
        } catch (Exception e) {
            Bitmap noimg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.no_product);
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
