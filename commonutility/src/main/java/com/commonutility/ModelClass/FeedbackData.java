
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackData {

    @SerializedName("UserID")
    @Expose
    private String UserID;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private String type;


    public FeedbackData(String userID, String email, String subject,
                        String message, String type) {
        UserID = userID;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.type = type;
    }
}
