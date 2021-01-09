package com.apk_home_service;

public class Appotment_Data_List {
    private String name, mobile_no, scheduled_time, scheduled_date, purpose,status;
    public Appotment_Data_List(String name, String mobile_no, String scheduled_time, String scheduled_date,
                     String purpose,String status) {
        this.name=name;
        this.mobile_no=mobile_no;
        this.scheduled_time=scheduled_time;
        this.scheduled_date=scheduled_date;
        this.purpose=purpose;
        this.status=status;
    }


    public String getName()
    {
        return name;
    }
    public String getMobile_no()
    {
        return mobile_no;
    }
    public String getScheduled_time(){
        return scheduled_time;
    }
    public String getScheduled_date() {
        return scheduled_date;
    }
    public String getPurpose(){
        return purpose;
    }
    public String getStatus(){
        return status;
    }

}

