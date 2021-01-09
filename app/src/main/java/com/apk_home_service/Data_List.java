package com.apk_home_service;


public class Data_List {
    private String cat_id,category_name,sub_id, sub_category_name,sub_category_image;
    public Data_List(String cat_id, String category_name, String sub_id, String sub_category_name,
                     String sub_category_image) {
        this.cat_id=cat_id;
        this.category_name=category_name;
        this.sub_id=sub_id;
        this.sub_category_name=sub_category_name;
        this.sub_category_image=sub_category_image;
    }


    public String getCat_id()
    {
        return cat_id;
    }
    public String getCategory_name()
    {
        return category_name;
    }
    public String getSub_id(){
        return sub_id;
    }
    public String getSub_category_name() {
        return sub_category_name;
    }
    public String getSub_category_image(){
        return sub_category_image;
    }
}
