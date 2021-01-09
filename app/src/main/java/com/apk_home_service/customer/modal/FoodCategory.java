package com.apk_home_service.customer.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodCategory implements Serializable{

    public String isIs_promotional() {
        return is_promotional;
    }

    public void setIs_promotional(String is_promotional) {
        this.is_promotional = is_promotional;
    }

    @SerializedName("is_promotional")
    @Expose
    public String is_promotional;


    @SerializedName("id")
    @Expose
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("category_name")
    @Expose
    public String name;

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    @SerializedName("categoryImage")
    @Expose
    public String category_image;


}