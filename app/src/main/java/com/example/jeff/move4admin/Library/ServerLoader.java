package com.example.jeff.move4admin.Library;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeff.move4admin.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Jeff on 10-12-2014.
 */
public class ServerLoader {
    private Bitmap[] output;
    private Context mContext;
    private DatabaseFunctions db;
    private ProgressBar pb;
    private Boolean fromSplash = false;
    private static ServerLoader mInstance;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private Activity activity;


    private ServerLoader(Context c) {
        mContext = c;
        db = DatabaseFunctions.getInstance(c);

    }

    public static synchronized ServerLoader getInstance( Context c) {
        if(mInstance == null) {
            mInstance = new ServerLoader(c);
        }
        return mInstance;
    }

    public void setPb (ProgressBar p)
    {
        pb = p;
    }

    public void setFromSplash(boolean b)
    {
       fromSplash = b;
    }

    public void loadUsers() {

        ServerRequestHandler.getAllUsers(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Log.d("Parsing", "getting users");
                        JSONObject o = jsonArray.getJSONObject(i);
                        int id = o.getInt("customerID");
                        String profileImage = o.getString("profileImage");
                        String email = o.getString("email");
                        String name = o.getString("name");
                        String lastName = o.getString("lastName");
                        String created = o.getString("created");
                        db.addUser(id, profileImage, name, lastName, email, created);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(fromSplash) {
                    loadLikes();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse != null)
                    Log.e("NETWORKERROR", volleyError.networkResponse.statusCode + " " + new String(volleyError.networkResponse.data));
                else {
                    if (volleyError.getMessage() == null)
                        Log.e("NETWORKERROR", "timeout");
                    else
                        Log.e("NETWORKERROR", volleyError.getMessage());
                }
            }
        }, mContext);
    }


    private void loadLikes() {
        ServerRequestHandler.getAllLikes(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                int inc = (int) Math.ceil(((20.0 / jsonArray.length())));

                Log.d("all likes", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Log.d("Parsing", "getting likes");
                        JSONObject o = jsonArray.getJSONObject(i);
                        int id = o.getInt("customerID");
                        String like = o.getString("categoryName");
                        db.addUserLikes(id, like);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("Parsing", "parsed all entries");
                if(fromSplash) {
                    if (pb != null) {
                        pb.setProgress(50);
                    }
                    loadImages();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse != null)
                    Log.e("NETWORKERROR", volleyError.networkResponse.statusCode + " " + new String(volleyError.networkResponse.data));
                else {
                    if (volleyError.getMessage() == null)
                        Log.e("NETWORKERROR", "timeout");
                    else
                        Log.e("NETWORKERROR", volleyError.getMessage());
                }
            }
        }, mContext);


    }

    public void loadImages() {
        ServerRequestHandler.getUserImages(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                int inc = (int) Math.ceil(((50.0 / jsonArray.length())));
                Log.d("Images array", jsonArray.toString());
                output = new Bitmap[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Log.d("Parsing", "getting image");
                        JSONObject o = jsonArray.getJSONObject(i);
                        //byte[] decoded = Base64.decode(o.getString("image"), Base64.DEFAULT);
                        String path = o.getString("path");
                        String image = o.getString("image");
                        byte[] decoded = Base64.decode(image, Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                        Log.d("Parsing", "parsed image");
                        saveToInternalSorage(bmp, path);
                        Log.d("Parsing", "saved");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if(fromSplash) {
                    if (pb != null) {
                        pb.setProgress(100);
                    }
                    showhome();
                }
                Log.d("Parsing", "parsed all entries");


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError.networkResponse != null)
                    Log.e("NETWORKERROR", volleyError.networkResponse.statusCode + " " + new String(volleyError.networkResponse.data));
                else {
                    if (volleyError.getMessage() == null)
                        Log.e("NETWORKERROR", "timeout");
                    else
                        Log.e("NETWORKERROR", volleyError.getMessage());
                }
            }
        }, mContext);
    }



    public void saveToInternalSorage(Bitmap bitmapImage, String filename) {
        ContextWrapper cw = new ContextWrapper(mContext);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir we remove the images/ by usign substring
        File mypath = new File(directory, filename.substring(7));

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showhome()
    {
        Intent i = new Intent(mContext,HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);

        if(activity != null)
        {
            activity.finish();
        }
    }
}



