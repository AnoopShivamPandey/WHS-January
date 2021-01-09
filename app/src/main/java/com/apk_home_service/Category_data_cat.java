package com.apk_home_service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class Category_data_cat  implements Serializable {
    String id,
            category_name,
            parent_category,
            category_image,
            is_featured,
            created_by,
            status;
    boolean checked;

    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getParent_category() {
        return parent_category;
    }
    public void setParent_category(String parent_category) {
        this.parent_category = parent_category;
    }

    public String getCategory_image() {
        return category_image;
    }
    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getIs_featured() {
        return is_featured;
    }
    public void setIs_featured(String is_featured) {
        this.is_featured = is_featured;
    }

    public String getCreated_by() {
        return created_by;
    }
    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public static List<Category_data_cat> createJsonInList(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Category_data_cat>>() {
        }.getType();
        return gson.fromJson(str, type);
    }
}
