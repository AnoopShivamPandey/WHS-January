package com.apk_home_service.customer.controll;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.modal.FoodMenuItem;
import com.apk_home_service.customer.utill.CommonUtill;

import java.util.ArrayList;


/**
 * Created by Rajnish on 6/29/2017.
 */

public class FoodMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Activity context;
    ArrayList<FoodMenuItem> menuItems = new ArrayList<>();
    FoodMenuAdapterListener adapterListener;

    public FoodMenuAdapter(Activity context, ArrayList<FoodMenuItem> menuItems) {
        this.context = context;
        refreshList(menuItems);
    }

    public void refreshList(ArrayList<FoodMenuItem> menuItems) {
        this.menuItems = menuItems;
        CommonUtill.print("refreshList ==" + menuItems.size());
        notifyDataSetChanged();
    }

    public void refreshList(ArrayList<FoodMenuItem> menuItems, int index) {
        this.menuItems = menuItems;
        CommonUtill.print("refreshList ==" + index + "==" + menuItems.size());
        notifyItemChanged(index);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DateHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void setAdapterListener(FoodMenuAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }


    class DateHolder extends RecyclerView.ViewHolder {

        TextView itemTxt, priceTxt, qntyTxt;
        ImageView checkImg;
        RelativeLayout qntyBg;

        public DateHolder(View itemView) {
            super(itemView);
            checkImg = (ImageView) itemView.findViewById(R.id.checkImg);
            itemTxt = (TextView) itemView.findViewById(R.id.itemTxt);
            priceTxt = (TextView) itemView.findViewById(R.id.priceTxt);
            qntyTxt = (TextView) itemView.findViewById(R.id.qntyTxt);
            qntyBg = (RelativeLayout) itemView.findViewById(R.id.qntyBg);
        }

        public void onBind(final int position) {
            final FoodMenuItem data = menuItems.get(position);
            itemTxt.setText(data.getName().toString().trim());
            priceTxt.setText(context.getResources().getString(R.string.Rs) + data.getPrice().toString().trim());
            qntyTxt.setText(data.getQnty() + "");
//            data.getQuantity().toString().trim()

            if (data.isSelect()) {
                checkImg.setImageResource(R.drawable.check_menu_item);
            } else {
                checkImg.setImageResource(R.drawable.uncheck_menu_item);
            }

            checkImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(data.getQuantity()) > 0) {
                        adapterListener.onSelect(data, position);
                    } else {
                        CommonUtill.showTost(context, "Sorry! Item is unavailable.");
                    }
                }
            });

            itemTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(data.getQuantity()) > 0) {
                        adapterListener.onSelect(data, position);
                    } else {
                        CommonUtill.showTost(context, "Sorry! Item is unavailable.");
                    }
                }
            });

            qntyBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.isSelect()) {
                        adapterListener.onQuantityChange(data, position, Integer.parseInt(data.getQuantity().toString().trim()));
                    } else {
                        CommonUtill.showTost(context, "First select item.");
                    }
                }
            });
        }
    }

    public interface FoodMenuAdapterListener {
        public void onSelect(FoodMenuItem foodMenuItem, int index);

        public void onQuantityChange(FoodMenuItem foodMenuItem, int index, int qnty);
    }

}
