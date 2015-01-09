
package com.example.jeff.move4admin.Library;




import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseFunctions {
    private static DatabaseFunctions _instance = null;

    //region Table and column names
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ibeaconkassa";
    // Table names
    private static final String TABLE_USERLIKES = " userlikes ";
    private static final String TABLE_ALLLIKES = " alllikes ";
    private static final String TABLE_ALLUSERS = " allusers ";
    private static final String TABLE_BEACONLOCATIONS = " beaconlocations ";
    private static final String TABLE_BEACONBACKGROUND = " beaconbackground ";



    private SQLiteOpenHelper sqLiteOpenHelper;
    // User likes Column names
    private static final String KEY_USERID = "id";
    private static final String KEY_CATEGORYNAME = "categoryname";
    // all users Column names
    private static final String KEY_USER = "customerID";
    private static final String KEY_PROFILEIMAGE = "profileImage";
    private static final String KEY_USERNAME = "name";
    private static final String KEY_USERLASTNAME = "lastName";
    private static final String KEY_USEREMAIL = "email";
    private static final String KEY_USERCREATED = "created";
    // all categories Column names
    private static final String KEY_ALLCATEGORYID = "categoryID";
    private static final String KEY_ALLCATEGORYNAME = "categoryName";

    //Beacon locations column names
    private static final String KEY_BEACONMAJOR = "major";
    private static final String KEY_BEACONMINOR = "minor";
    private static final String KEY_BEACONLOCATIONX = "x";
    private static final String KEY_BEACONLOCATIONY = "y";
    //Beacon locations column names
    private static final String KEY_BEACONBACKGROUND = "background";
    //endregion

    //create instance
    private synchronized static void createInstance (final Context context) {
        if (_instance == null) _instance = new DatabaseFunctions(context);

    }

    //get instance
    public static DatabaseFunctions getInstance (final Context context) {
        if (_instance == null) createInstance (context);
        return _instance;
    }

    private DatabaseFunctions(Context context ) {
        sqLiteOpenHelper = new SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
                @Override
                public void onCreate(SQLiteDatabase db) {
                    //createTables(db);
                }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERLIKES);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALLUSERS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALLLIKES);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACONLOCATIONS);
                // Create tables again
                onCreate(db);
            }
        };
//       sqLiteOpenHelper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_USERLIKES);
//       sqLiteOpenHelper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_ALLUSERS);
//       sqLiteOpenHelper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_ALLLIKES);
       createTables(sqLiteOpenHelper.getWritableDatabase());

    }

    public void addUserLikes(int id, String categoryName) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        db.delete(TABLE_USERLIKES,KEY_USERID + " = " + id ,null );
        ContentValues values = new ContentValues();
        values.put(KEY_USERID, id); //
        values.put(KEY_CATEGORYNAME, categoryName); //
        db.insert(TABLE_USERLIKES, null, values);
        db.close(); // Closing database connection
    }
    public void addBeaconBackground(String bc) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        db.delete(TABLE_BEACONBACKGROUND,"1=1",null );
        ContentValues values = new ContentValues();
        values.put(KEY_BEACONBACKGROUND, bc); //
        db.insert(TABLE_BEACONBACKGROUND, null, values);
        db.close(); // Closing database connection
    }

    public void addBeaconLocation(int major, int minor, int x , int y) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        db.delete(TABLE_BEACONLOCATIONS,KEY_BEACONMAJOR + " = " + major + " AND " + KEY_BEACONMINOR + " = " + minor,null );
        ContentValues values = new ContentValues();
        values.put(KEY_BEACONLOCATIONX, x); //
        values.put(KEY_BEACONLOCATIONY, y); //
        values.put(KEY_BEACONMAJOR, major); //
        values.put(KEY_BEACONMINOR, minor); //
        db.insert(TABLE_BEACONLOCATIONS,null,values);
        db.close(); // Closing database connection
    }

    public void addUser(int id, String profileImage,String name,String lastName,String email,String created ) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete(TABLE_ALLUSERS,KEY_USER + " = " + id ,null );
        values.put(KEY_USER, id); //
        values.put(KEY_PROFILEIMAGE, profileImage); //
        values.put(KEY_USERNAME, name); //
        values.put(KEY_USERLASTNAME, lastName); //
        values.put(KEY_USEREMAIL, email); //
        values.put(KEY_USERCREATED, created); //
        db.insert(TABLE_ALLUSERS, null, values);
        db.close(); // Closing database connection
        //db.delete()
    }

    public ArrayList<User> getUsers(){

        ArrayList<User> users = new ArrayList<User>();

        String selectQuery = "SELECT * FROM " + TABLE_ALLUSERS;
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                User u = new User();
                u.setUserID(Integer.parseInt(cursor.getString(0)));
                u.setFilePath(cursor.getString(1));
                u.setName( cursor.getString(2));
                u.setLastName(cursor.getString(3));
                u.setEmail(cursor.getString(4));
                u.setCreated(cursor.getString(5));

              users.add(u);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return category list
        return users;
    }

    public String getBeaconBackground(){

        String output = "";
        String selectQuery = "SELECT * FROM " + TABLE_BEACONBACKGROUND;
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                output = cursor.getString(0);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return output;
    }

    public ArrayList<BeaconDrawable> getBeaconLocations(){

        ArrayList<BeaconDrawable> beaconLocations = new ArrayList<BeaconDrawable>();

        String selectQuery = "SELECT * FROM " + TABLE_BEACONLOCATIONS;
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                BeaconDrawable bd =  new BeaconDrawable();
                bd.setMajor(Integer.parseInt(cursor.getString(0)));
                bd.setMinor(Integer.parseInt(cursor.getString(1)));
                bd.setX(Integer.parseInt(cursor.getString(2)));
                bd.setY(Integer.parseInt(cursor.getString(3)));
                beaconLocations.add(bd);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return category list
        return beaconLocations;
    }


    public ArrayList<UserLike> getUserLikes(){
        ArrayList<UserLike> userlikes = new ArrayList<UserLike>();

        String selectQuery = "SELECT * FROM " + TABLE_USERLIKES;
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean empty = true;
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String like = cursor.getString(1);

                for (UserLike u : userlikes)
                {
                    if (u.getUserID() == id)
                    {
                        u.addLikes(like);
                        empty = false;
                        break;
                    }
                    else {
                        empty = true;
                    }
                }
                if (empty)
                {
                    UserLike ul = new UserLike(id);
                    ul.addLikes(like);
                    userlikes.add(ul);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return category list
        return userlikes;
    }

    public ArrayList<Like> getALLlikes(){
        ArrayList<Like> likes = new ArrayList<Like>();

        String selectQuery = "SELECT * FROM " + TABLE_ALLLIKES;
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean empty = true;
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);

                for (Like u : likes)
                {
                    if (u.getcategoryID() == id)
                    {
                        u.setcategoryName(name);
                        empty = false;
                        break;
                    }
                    else {
                        empty = true;
                    }
                }
                if (empty)
                {
                    Like l = new Like(id,name);
                    likes.add(l);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return category list
        return likes;
    }



/**
     * Empty tables
     * */

    public void resetUserLikes(){
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USERLIKES, null, null);
        db.close();
    }

    public void resetUsers(){
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ALLUSERS, null, null);
        db.close();
    }

    public void resetBeaconLocations(){
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_BEACONLOCATIONS, null, null);
        db.close();
    }

    public void resetBeaconBackground(){
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_BEACONBACKGROUND, null, null);
        db.close();
    }

    public void resetAllLikes(){
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ALLLIKES, null, null);
        db.close();
    }
    /**
     * delete entry
     * */

    public void deleteBeaconLocation(String major, String minor){
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_BEACONLOCATIONS, KEY_BEACONMAJOR + " = " + major + " and " + KEY_BEACONMINOR + " = " + minor , null);
        db.close();
    }

    /**
     * Create tables
     * */

    public void createTables(SQLiteDatabase db){
        String CREATE_LIKES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERLIKES + "("
                + KEY_USERID + " INTEGER, "
                + KEY_CATEGORYNAME + " TEXT " +")";
        db.execSQL(CREATE_LIKES_TABLE);

        String CREATE_BEACONBACKGROUND_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BEACONBACKGROUND + "("
                + KEY_BEACONBACKGROUND + " TEXT " +")";
        db.execSQL(CREATE_BEACONBACKGROUND_TABLE);

        String CREATE_BEACONLOCATIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BEACONLOCATIONS + "("
                + KEY_BEACONMAJOR + " INTEGER, "
                + KEY_BEACONMINOR + " INTEGER, "
                + KEY_BEACONLOCATIONX + " INTEGER, "
                + KEY_BEACONLOCATIONY + " INTEGER " +")";
        db.execSQL(CREATE_BEACONLOCATIONS_TABLE);


        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ALLUSERS + "("
                + KEY_USER + " INTEGER, "
                + KEY_PROFILEIMAGE + " TEXT, "
                + KEY_USERNAME + " TEXT, "
                + KEY_USERLASTNAME + " TEXT, "
                + KEY_USEREMAIL + " TEXT, "
                + KEY_USERCREATED + " TEXT " +")";
        db.execSQL(CREATE_USER_TABLE);


        String CREATE_ALLLIKES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ALLLIKES + "("
                + KEY_ALLCATEGORYID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ALLCATEGORYNAME + " TEXT " +")";
        db.execSQL(CREATE_ALLLIKES_TABLE);
    }
}
