package com.apk_home_service.customer.UI;

public class Doctor_List {
private String id,full_name,mobile,email,restaurant_owner,opening_time,
        closing_time,user_type,city_name,state_name,street,specialities,pin_code,restaurant_image,complete_address,distance,rating,profile_status;
    public Doctor_List(String id,String full_name,String mobile,String email,String restaurant_owner,String opening_time,String closing_time,
                       String user_type,String city_name,String state_name,String street,String specialities,String pin_code,String restaurant_image,
                       String complete_address,String distance,String rating,String profile_status){
        this.id=id;
        this.full_name=full_name;
        this.mobile=mobile;
        this.email=email;
        this.restaurant_owner=restaurant_owner;
        this.opening_time=opening_time;
        this.closing_time=closing_time;
        this.user_type=user_type;
        this.city_name=city_name;
        this.state_name=state_name;
        this.street=street;
        this.specialities=specialities;
        this.pin_code=pin_code;
        this.restaurant_image=restaurant_image;
        this.complete_address=complete_address;
        this.distance=distance;
        this.rating=rating;
        this.profile_status=profile_status;
    }
    public String getId()
    {
        return id;
    }
    public String getFull_name()
    {
        return full_name;
    }
    public String getMobile(){
        return mobile;
    }
    public String getEmail() {
        return email;
    }
    public String getRestaurant_owner(){
        return restaurant_owner;
    }
    public String getOpening_time()
    {
        return opening_time;
    }
    public String getClosing_time()
    {
        return closing_time;
    }
    public String getUser_type()
    {
        return user_type;
    }
    public String getCity_name()
    {
        return city_name;
    }
    public String getState_name()
    {
        return state_name;
    }
    public String getStreet()
    {
        return street;
    }
    public String getPin_code()
    {
        return pin_code;
    }

    public String getSpecialities()
    {
        return specialities;
    }

    public String getRestaurant_image()
    {
        return restaurant_image;
    }
    public String getComplete_address()
    {
        return complete_address;
    }
    public String getDistance()
    {
        return distance;
    }

    public String getRating()
    {
        return rating;
    }
    public String getProfile_status()
    {
        return profile_status;
    }
}

