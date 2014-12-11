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
import com.example.jeff.move4admin.Library.ServerLoader;
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
        ServerLoader sl =  ServerLoader.getInstance(getApplicationContext());
        sl.setFromSplash(true);
        sl.setPb(progressBar);
        sl.setActivity(this);
        sl.loadUsers();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

}
