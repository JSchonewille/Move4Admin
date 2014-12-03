package com.example.jeff.move4admin;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeff.move4admin.Library.DatabaseFunctions;
import com.example.jeff.move4admin.Library.ServerRequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;


public class Splashscreen extends Activity {
    Bitmap[] output;
    DatabaseFunctions db;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        db = DatabaseFunctions.getInstance(getApplicationContext());
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadUsers();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splashscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUsers()
    {

        ServerRequestHandler.getAllUsers(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                int inc = (int) Math.ceil(((30.0 / jsonArray.length())));
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
                        if((progressBar.getProgress() + inc) < 30)
                        {
                            progressBar.incrementProgressBy(inc);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setProgress(30);
                Log.d("Parsing", "Parsing succesful");
                 loadLikes();

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
        }, getApplicationContext());


    }


    private void loadLikes()
    {
        ServerRequestHandler.getAllLikes(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                int inc = (int) Math.ceil(((20.0 / jsonArray.length())));

                Log.d("all likes", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Log.d("Parsing", "getting likes");
                        JSONObject o = jsonArray.getJSONObject(i);
                         int id =  o.getInt("customerID");
                         String like = o.getString("categoryName");
                          db.addUserLikes(id,like);

                        if((progressBar.getProgress() + inc) < 50)
                        {
                            progressBar.incrementProgressBy(inc);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressBar.setProgress(50);
                Log.d("Parsing", "parsed all entries");
                loadImages();
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
        },getApplicationContext());


    }

    private void loadImages()
    {
        ServerRequestHandler.getUserImages(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                int inc = (int) Math.ceil(((50.0 / jsonArray.length())));
                Log.d("Images array", jsonArray.toString());
                output = new Bitmap[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        Log.d("Parsing","getting image");
                        JSONObject o = jsonArray.getJSONObject(i);
                        //byte[] decoded = Base64.decode(o.getString("image"), Base64.DEFAULT);
                        String path = o.getString("path");
                        String image = o.getString("image");
                        byte[] decoded = Base64.decode(image, Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                        Log.d("Parsing","parsed image");
                        saveToInternalSorage(bmp, path);
                        Log.d("Parsing","saved");
                        if((progressBar.getProgress() + inc) < 100)
                        {
                            progressBar.incrementProgressBy(inc);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                progressBar.setProgress(100);
                Log.d("Parsing","parsed all entries");
                Intent i = new Intent(Splashscreen.this, HomeActivity.class);
                startActivity(i);
                // close this activity
                finish();

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
        },getApplicationContext());  }





    private void saveToInternalSorage(Bitmap bitmapImage , String filename){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir we remove the images/ by usign substring
        File mypath=new File(directory,filename.substring(7));

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
}
