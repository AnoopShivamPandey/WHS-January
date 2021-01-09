package com.apk_home_service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
public class BannerData implements Serializable {
            String id,
            image,
            banner_type,
            bannertitle,
            banner_description,
            status,
            banner_link,
            banner_link_status,
            created_at,
            updated_at,
            ordering,image_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }





    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBanner_type() {
        return banner_type;
    }

    public void setBanner_type(String banner_type) {
        this.banner_type = banner_type;
    }

    public String getBannertitle() {
        return bannertitle;
    }

    public void setBannertitle(String bannertitle) {
        this.bannertitle = bannertitle;
    }

    public String getBanner_description() {
        return banner_description;
    }

    public void setBanner_description(String banner_description) {
        this.banner_description = banner_description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBanner_link() {
        return banner_link;
    }

    public void setBanner_link(String banner_link) {
        this.banner_link = banner_link;
    }

    public String getBanner_link_status() {
        return banner_link_status;
    }

    public void setBanner_link_status(String banner_link_status) {
        this.banner_link_status = banner_link_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }
    public static List<BannerData> createJsonInList(String str){
        Gson gson=new Gson();
        Type type=new TypeToken<List<BannerData>>(){
        }.getType();
        return gson.fromJson(str,type);
    }

}
