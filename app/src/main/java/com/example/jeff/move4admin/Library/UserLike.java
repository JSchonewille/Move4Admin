package com.example.jeff.move4admin.Library;

import java.util.ArrayList;

/**
 * Created by Jeff on 28-11-2014.
 */
public class UserLike {
    private int userID;
    private ArrayList<String> likes;

    public UserLike( int user)
    {
        userID = user;
        likes = new ArrayList<String>();
    }

    public int getUserID()
    {
        return  userID;
    }

    public ArrayList<String> getLikes()
    {
        return likes;
    }

    public void addLikes(String input)
    {
        likes.add(input);
    }

}
