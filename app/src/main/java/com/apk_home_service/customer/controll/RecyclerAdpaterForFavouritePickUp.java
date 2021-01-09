package com.apk_home_service.customer.controll;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apk_home_service.customer.R;

import java.util.ArrayList;

public class RecyclerAdpaterForFavouritePickUp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<String> favouriteList;
    ArrayList<String> favouriteListLat;
    ArrayList<String> favouriteListLng;
    ArrayList<String> favouriteListId;
    Activity activity;
    Context context;
    int layout;
    int PICKUP = 111;
    int DROPOFF = 222;

/*

    public RecyclerAdapter(Activity activity, ArrayList<String> recyclerList) {
        this.RecyclerList = recyclerList;
        this.activity = activity;

    }
*/


    public RecyclerAdpaterForFavouritePickUp(Activity activity, ArrayList<String> favouriteList, ArrayList<String> favouriteListLat, ArrayList<String> favouriteListLng, ArrayList<String> favouriteListId) {
        this.favouriteList = favouriteList;
        this.favouriteListLat=favouriteListLat;
        this.favouriteListLng=favouriteListLng;
        this.favouriteListId=favouriteListId;
        this.activity = activity;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_favourite_location, parent, false);
        return new GenericViewHolder(v);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        GenericViewHolder genericViewHolder = (GenericViewHolder) holder;
        genericViewHolder.searched_Place.setText(favouriteList.get(position));
        genericViewHolder.searchedLat.setText(favouriteListLat.get(position));
        genericViewHolder.searchedLng.setText(favouriteListLng.get(position));
        genericViewHolder.llrowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent();

                i.putExtra("pickup", favouriteList.get(position));
                i.putExtra("lat_pickup", favouriteListLat.get(position));
                i.putExtra("lng_pickup", favouriteListLng.get(position));
                i.putExtra("id_pickup", favouriteListId.get(position));
                i.putExtra("from", "favourite_list");


                activity.setResult(PICKUP, i);
                activity.finish();

            }
        });

    }






    @Override
    public int getItemCount() {
        return (favouriteList.size());
    }


    class GenericViewHolder extends RecyclerView.ViewHolder {
        TextView searched_Place,searchedLat,searchedLng,favouriteListId;
        LinearLayout llrowList;


        public GenericViewHolder(View itemView) {
            super(itemView);
            this.searched_Place = (TextView) itemView.findViewById(R.id.searched_Place);
            this.searchedLat = (TextView) itemView.findViewById(R.id.searchedLat);
            this.searchedLng = (TextView) itemView.findViewById(R.id.searchedLng);
            this.favouriteListId = (TextView) itemView.findViewById(R.id.favouriteListId);
            this.llrowList = (LinearLayout) itemView.findViewById(R.id.llrowList);
            context = itemView.getContext();
        }

    }

}