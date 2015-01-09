package com.example.jeff.move4admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

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
        init();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean output = netInfo != null && netInfo.isConnectedOrConnecting();
        if (!output)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(Splashscreen.this);
            alert.setTitle("Geen connectie");
            alert.setMessage("Controleer uw verbinding");
            // Set an EditText view to get user input
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    init();
                }});
            alert.show();
        }
        return output;
    }

    public void init()
    {
        if (isOnline()) {
            db = DatabaseFunctions.getInstance(getApplicationContext());
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            ServerLoader sl = ServerLoader.getInstance(getApplicationContext());
            sl.setFromSplash(true);
            sl.setPb(progressBar);
            sl.setActivity(this);
            sl.loadUsers();
        }
    }

}
