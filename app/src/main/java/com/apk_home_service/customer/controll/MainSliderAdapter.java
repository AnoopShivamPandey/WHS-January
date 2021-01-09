//package com.apk_home_service.customer.controll;
//import org.json.JSONObject;
//import java.util.ArrayList;
//
//public class MainSliderAdapter extends Slider {
//    ArrayList<JSONObject> resultList;
//    public MainSliderAdapter(ArrayList<JSONObject> resultList) {
//        this.resultList = resultList;
//    }
//
//    @Override
//    public int getItemCount() {
//        return resultList.size();
//    }
//
//    @Override
//    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
//        try {
//            viewHolder.bindImageSlide(resultList.get(position).getString("bannerImage"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//}