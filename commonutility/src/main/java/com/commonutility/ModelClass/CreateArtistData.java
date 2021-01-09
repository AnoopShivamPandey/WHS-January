
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateArtistData implements Serializable{

    @SerializedName("FirstName")
    @Expose
    private String FirstName;

    @SerializedName("LastName")
    @Expose
    private String LastName;

    @SerializedName("Email")
    @Expose
    private String Email;

    @SerializedName("Mobile")
    @Expose
    private String Mobile;

    @SerializedName("ProfileImage")
    @Expose
    private String ProfileImage;

    @SerializedName("Address")
    @Expose
    private String Address;


    @SerializedName("SpecialityID")
    @Expose
    private String SpecialityID;

    @SerializedName("ServiceIDs")
    @Expose
    private String BankName;

    @SerializedName("BankName")
    @Expose
    private String SalonID;

    @SerializedName("BankAddress")
    @Expose
    private String BankAddress;

    @SerializedName("BankCode")
    @Expose
    private String BankCode;

    @SerializedName("BankAccount")
    @Expose
    private String BankAccount;

    @SerializedName("BankAccountName")
    @Expose
    private String BankAccountName;

    @SerializedName("CreatedBy")
    @Expose
    private String CreatedBy;

    public CreateArtistData(String firstName, String lastName, String email, String mobile, String profileImage, String address, String specialityID, String bankName, String salonID, String bankAddress, String bankCode, String bankAccount, String bankAccountName, String createdBy, String serviceIDs) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Mobile = mobile;
        ProfileImage = profileImage;
        Address = address;
        SpecialityID = specialityID;
        BankName = bankName;
        SalonID = salonID;
        BankAddress = bankAddress;
        BankCode = bankCode;
        BankAccount = bankAccount;
        BankAccountName = bankAccountName;
        CreatedBy = createdBy;
        ServiceIDs = serviceIDs;
    }

    @SerializedName("ServiceIDs")
    @Expose
    private String ServiceIDs;



}
