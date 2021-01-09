package com.apk_home_service.customer.controll;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.modal.FoodCategory;
import com.apk_home_service.customer.utill.CommonUtill;

import java.util.ArrayList;


/**
 * Created by Rajnish on 6/29/2017.
 */

public class FoodCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Activity context;
    ArrayList<FoodCategory> menuItems = new ArrayList<>();
    FoodCategoryAdapterListener adapterListener;

    public FoodCategoryAdapter(Activity context, ArrayList<FoodCategory> menuItems) {
        this.context = context;
        refreshList(menuItems);
    }

    public void refreshList(ArrayList<FoodCategory> menuItems) {
        this.menuItems = menuItems;
        CommonUtill.print("refreshList ==" + menuItems.size());
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.food_category_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DateHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void setAdapterListener(FoodCategoryAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }


    class DateHolder extends RecyclerView.ViewHolder {

        TextView catTxt;
        ImageView catImg;

        public DateHolder(View itemView) {
            super(itemView);
            catTxt = (TextView) itemView.findViewById(R.id.catTxt);
            catImg = (ImageView) itemView.findViewById(R.id.catImg);
        }

        public void onBind(final int position) {
            final FoodCategory data = menuItems.get(position);
            catTxt.setText(data.getName().toString().trim());

            Glide.with(context)
                    .load(data.getCategory_image())
                    .error(R.drawable.logo)
                    .crossFade()
                    .centerCrop()
                    .transform(new CircleTransform(context))
                    .into(catImg);

            catImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterListener.onSelect(data,position);
                }
            });
        }
    }

    public interface FoodCategoryAdapterListener {
        public void onSelect(FoodCategory artist, int index);
    }

}
