package com.apk_home_service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.RestaurantList;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class AdapterForSubCategory extends RecyclerView.Adapter<AdapterForSubCategory.ViewHolder> {
    Context context;
    List<SubcategoryData> list = new ArrayList<>();

    public AdapterForSubCategory(Context context, List<SubcategoryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_for_category, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        Picasso.with(context).load(list.get(i).getCategory_image()).placeholder(R.drawable.logo).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.img_cat.setImageBitmap(bitmap);
                holder.img_cat.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
        });
        holder.rl_main.setBackgroundColor(context.getResources().getColor(R.color.greyLight));
        holder.txt_catname.setText(list.get(i).getCategory_name());
        holder.txt_catname.setTextColor(ContextCompat.getColor(context, R.color.black));
        holder.iv_openmore.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, RestaurantList.class);
                intent.putExtra("lat", "11.654464");
                intent.putExtra("lon", "19.3554");
                intent.putExtra("subcat_name",list.get(i).getCategory_name());
                intent.putExtra("id", list.get(i).getId());
                intent.putExtra("from", "subcat");
                context.startActivity(intent);
               // context.startActivity(new Intent(context, ActivityProduct.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id",list.get(i).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_cat, iv_openmore;
        TextView txt_catname;
        RecyclerView rv_Subcategory;
        RelativeLayout rl_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cat = (ImageView) itemView.findViewById(R.id.img_cat);
            iv_openmore = (ImageView) itemView.findViewById(R.id.iv_openmore);
            txt_catname = (TextView) itemView.findViewById(R.id.txt_catname);
            rv_Subcategory=(RecyclerView)itemView.findViewById(R.id.rv_Subcategory);
            rl_main=(RelativeLayout) itemView.findViewById(R.id.rl_main);
        }
    }
}
