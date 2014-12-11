package com.example.jeff.move4admin.Library;

/**
 * Created by Jeff on 10-12-2014.
 */
public class Like {
    private int categoryID;
    private String categoryName;

    public Like(int id, String d) {
        categoryName = d;
        categoryID = id;
    }

    public int getcategoryID() {
        return categoryID;
    }

    public void setcategoryID(int id) {
        this.categoryID = id;
    }

    public String getcategoryName() {
        return categoryName;
    }

    public void setcategoryName(String name) {
        this.categoryName = name;
    }


}
