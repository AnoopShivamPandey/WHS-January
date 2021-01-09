package com.apk_home_service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apk_home_service.customer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterForBanner extends PagerAdapter {

    private ArrayList<HashMap<String, String>> images;
    private LayoutInflater inflater;
    private Context context;
    List<BannerData> bannerData;



    public AdapterForBanner(FragmentActivity activity, List<BannerData> bannerData) {
        this.context = activity;
        this.bannerData = bannerData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.from(view.getContext()).inflate(R.layout.banner, view, false);
        final ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.img_banner);


      /*  Picasso.with(context).load(arrayList.get(position))
                .placeholder(R.drawable.image_uploading)
                .error(R.drawable.image_not_found).into(imageView);

        container.addView(itemView);*/

        //Picasso.load(images.get(position).into(myImage));

        // final String id = images.get(position).get("id");
       /* myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, id+"", Toast.LENGTH_SHORT).show();
            }
        });*/
        Picasso.with(context).load(bannerData.get(position).getImage()).fit()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo).into(myImage);
        /*Picasso.with(context).load(bannerData.get(position).getImage()).placeholder(R.drawable.placeholder).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                myImage.setImageBitmap(bitmap);
                myImage.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
        });*/
        view.addView(myImageLayout);
        // myImage.setImageResource(images.get(position));
        // view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return view.equals(o);
    }


}

