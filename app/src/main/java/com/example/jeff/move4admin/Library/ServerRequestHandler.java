package com.example.jeff.move4admin.Library;


import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sander on 11-11-2014.
 */
public class ServerRequestHandler {
    ArrayList<User> list;



    ////////////////
    //REQUESTS
    ////////////////
    public static void getPresentUsers(Response.Listener<JSONArray> l, Response.ErrorListener el, Context c) {
        String URL = Config.GETPRESENTUSERS;

        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);

        RequestController.getInstance(c).addToRequestQueue(req);
    }

    public static void getAllLikes(Response.Listener<JSONArray> l, Response.ErrorListener el, Context c) {
        String URL = Config.GETALLLIKES;

        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);

        RequestController.getInstance(c).addToRequestQueue(req);
    }

    public static void getAllUsers(Response.Listener<JSONArray> l, Response.ErrorListener el, Context c) {
        String URL = Config.GETALLUSERS;

        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);
        RequestController.getInstance(c).addToRequestQueue(req);
    }

    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeToString(imageByteArray, 1);
    }

    public static void getUserImages(Response.Listener<JSONArray> l, Response.ErrorListener el, Context c) {
        String URL = Config.GETUSERIMAGES;

        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);

        RequestController.getInstance(c).addToRequestQueue(req);
    }

    public static void getAllCategories(Response.Listener<JSONArray> l, Response.ErrorListener el, Context c) {
        String URL = Config.GETALLCATEGORIES;

        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);

        RequestController.getInstance(c).addToRequestQueue(req);
    }

    public static void getAllOffers(Response.Listener<JSONArray> l, Response.ErrorListener el, Context c) {
        String URL = Config.GETALLOFFERS;

        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);

        RequestController.getInstance(c).addToRequestQueue(req);
    }

    public static void getAllProducts(Response.Listener<JSONArray> l, Response.ErrorListener el, Context c) {
        String URL = Config.GETALLPRODUCTS;

        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);

        RequestController.getInstance(c).addToRequestQueue(req);
    }

    public static void getAllBeacons(Response.Listener<JSONArray> l, Response.ErrorListener el, Context c) {
        String URL = Config.GETALLBEACONS;

        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);

        RequestController.getInstance(c).addToRequestQueue(req);
    }

    public static void uploadProduct(Response.Listener<JSONObject> l, Response.ErrorListener el, final String productName, final int categoryID, final String image, final String description, Context c){
        String URL = Config.INSERTPRODUCT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", productName);
        params.put("categoryID",Integer.toString(categoryID));
        params.put("image", image);
        params.put("description", description);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance(c).addToRequestQueue(request);
    }

    public static void uploadOffer(Response.Listener<JSONObject> l, Response.ErrorListener el, final int categoryID, final String image, final String description, Context c){
        String URL = Config.INSERTOFFER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("categoryID",Integer.toString(categoryID));
        params.put("image", image);
        params.put("description", description);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance(c).addToRequestQueue(request);
    }

    public static void uploadOffer(Response.Listener<JSONObject> l, Response.ErrorListener el,final int productID, final int categoryID, final String image, final String description, Context c){
        String URL = Config.INSERTOFFER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("offerID",Integer.toString(productID));
        params.put("categoryID",Integer.toString(categoryID));
        params.put("image", image);
        params.put("description", description);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance(c).addToRequestQueue(request);
    }

    public static void uploadBeacon(Response.Listener<JSONObject> l, Response.ErrorListener el,final int productID, final int offerID, final int major, final int minor, Context c){
        String URL = Config.INSERTBEACON;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("productID",Integer.toString(productID));
        params.put("offerID",Integer.toString(offerID));
        params.put("major", Integer.toString(major));
        params.put("minor", Integer.toString(minor));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance(c).addToRequestQueue(request);
    }

    public static void editBeacon(Response.Listener<JSONObject> l, Response.ErrorListener el,final int beaconID,final int productID, final int offerID, final int major, final int minor, Context c){
        String URL = Config.INSERTBEACON;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("beaconID",Integer.toString(beaconID));
        params.put("productID",Integer.toString(productID));
        params.put("offerID",Integer.toString(offerID));
        params.put("major", Integer.toString(major));
        params.put("minor", Integer.toString(minor));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance(c).addToRequestQueue(request);
    }



    public static void uploadCategory(Response.Listener<JSONObject> l, Response.ErrorListener el, final String categoryName,Context c){
        String URL = Config.INSERTCATEGORY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("categoryName", categoryName);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance(c).addToRequestQueue(request);
    }

    public static void DeleteCategory(Response.Listener<JSONObject> l, Response.ErrorListener el, final String categoryName,Context c){
        String URL = Config.DELETECATEGORY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("categoryName", categoryName);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance(c).addToRequestQueue(request);
    }

    public static void DeleteProduct(Response.Listener<JSONObject> l, Response.ErrorListener el, final String name,Context c){
        String URL = Config.DELETEPRODUCT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance(c).addToRequestQueue(request);
    }
    public static void DeleteOffer(Response.Listener<JSONObject> l, Response.ErrorListener el, final String id,Context c){
        String URL = Config.DELETEOFFER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance(c).addToRequestQueue(request);
    }

    public static void DeleteBeacon(Response.Listener<JSONObject> l, Response.ErrorListener el, final String id,Context c){
        String URL = Config.DELETEBEACON;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance(c).addToRequestQueue(request);
    }



    public String saveImage(byte[] byteArray) {
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        File dir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + "/Move4Kassa");
        dir.mkdirs();

        File mypath = new File(dir, "ProfileImage.jpeg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return mypath.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /////////////////////////////
    //IMPLEMENTED REQUESTS
    /////////////////////////////



}

