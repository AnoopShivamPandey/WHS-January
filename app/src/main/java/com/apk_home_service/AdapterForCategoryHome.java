package com.apk_home_service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.RestaurantList;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class AdapterForCategoryHome extends RecyclerView.Adapter<AdapterForCategoryHome.ViewHolder> {
    Context context;
    List<String> list = new ArrayList<>();
    List<CategoryData> categoryData;

    public AdapterForCategoryHome(FragmentActivity activity, List<CategoryData> categoryData) {
        this.context = activity;
        this.categoryData = categoryData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_for_category_home, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tv_categoryName.setText(categoryData.get(i).getSubcategory_name());
        Picasso.with(context).load(categoryData.get(i).getCategory_image())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo).into(viewHolder.iv_category);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (NetworkConnectionHelper.isOnline(context)) {
                Intent intent=new Intent(context,RestaurantList.class);
                intent.putExtra("lat", "11.654464");
                intent.putExtra("lon", "19.3554");
                intent.putExtra("subcat_name",categoryData.get(i).getSubcategory_name());
                intent.putExtra("id", categoryData.get(i).getId());
                intent.putExtra("from", "subcat");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_category;
        TextView tv_categoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_category = (ImageView) itemView.findViewById(R.id.iv_category);
            tv_categoryName = (TextView) itemView.findViewById(R.id.tv_categoryName);
        }
    }
}
