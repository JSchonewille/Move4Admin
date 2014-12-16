package com.example.jeff.move4admin.Library;

/**
 * Created by Jeff on 16-12-2014.
 */
public class Product {
    private int productID;
    private String name;
    private int categoryID;
    private String image;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int cateogoryID) {
        this.categoryID = cateogoryID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
