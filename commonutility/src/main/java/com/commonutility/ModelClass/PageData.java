
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageData {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("lang")
    @Expose
    private String lang;

    public PageData(String url, String lang) {
        this.url = url;
        this.lang = lang;
    }

}
