package com.apk_home_service.customer.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pc on 27-12-2018.
 */

public class LandingModal {
    @SerializedName("parent_id")
    @Expose
    public String parent_id;

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    @SerializedName("category_id")
    @Expose
    public String categoryId;

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return parent_id;
    }

}
