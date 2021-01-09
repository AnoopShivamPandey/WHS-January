package com.apk_home_service.customer.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodSubCategory implements Serializable{

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

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("subcategory_image")
    @Expose
    public String subcategory_image;

    @SerializedName("category_id")
    @Expose
    public String category_id;

    public String getSubcategory_image() {
        return subcategory_image;
    }

    public void setSubcategory_image(String subcategory_image) {
        this.subcategory_image = subcategory_image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getParent_categroy() {
        return parent_categroy;
    }

    public void setParent_categroy(String parent_categroy) {
        this.parent_categroy = parent_categroy;
    }

    @SerializedName("parent_categroy")
    @Expose
    public String parent_categroy;

    public boolean isExplore() {
        return isExplore;
    }

    public void setExplore(boolean explore) {
        isExplore = explore;
    }

    @SerializedName("is_explore")
    @Expose
    public boolean isExplore = false;







}