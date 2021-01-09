package com.apk_home_service.customer.controll;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.BookingDetails;
import com.apk_home_service.customer.modal.OrderModel;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.DateTimeConverter;

import java.util.ArrayList;


/**
 * Created by Rajnish on 6/29/2017.
 */

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Activity context;
    ArrayList<OrderModel> menuItems = new ArrayList<>();
    FoodSubCatAdapterListener adapterListener;

    public OrderListAdapter(Activity context, ArrayList<OrderModel> menuItems) {
        this.context = context;
        refreshList(menuItems);
    }

    public void refreshList(ArrayList<OrderModel> menuItems) {
        this.menuItems = menuItems;
        CommonUtill.print("refreshList ==" + menuItems.size());
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DateHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void setAdapterListener(FoodSubCatAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }


    class DateHolder extends RecyclerView.ViewHolder {

        TextView orderIdTxt, priceTxt, dateTxt, clickTxt,bookingStatus;
        ImageView subCatPic;
TextView invoiceTxt;
        public DateHolder(View itemView) {
            super(itemView);
            orderIdTxt = (TextView) itemView.findViewById(R.id.orderIdTxt);
            priceTxt = (TextView) itemView.findViewById(R.id.priceTxt);
            clickTxt = (TextView) itemView.findViewById(R.id.clickTxt);
            dateTxt = (TextView) itemView.findViewById(R.id.dateTxt);
            invoiceTxt = (TextView) itemView.findViewById(R.id.invoiceTxt);
            bookingStatus = (TextView) itemView.findViewById(R.id.bookingStatus);
            subCatPic = (ImageView) itemView.findViewById(R.id.subCatPic);
        }

        public void onBind(final int position) {
            final OrderModel data = menuItems.get(position);
            orderIdTxt.setText(data.getRestaurantName());
            bookingStatus.setText(data.getOrderstatus());
            invoiceTxt.setText(data.getInvoice());
            priceTxt.setText(context.getResources().getString(R.string.Rs) + "" + data.getTotal().toString().trim());
            dateTxt.setText(DateTimeConverter.convert(context, "yyyy-MM-dd", "dd-MM-yyyy",
                    data.getScheduledDate().toString().trim(), null));

            Glide.with(context)
                    .load(data.getRestaurantImage())
                    .error(R.drawable.logo)
                    .crossFade()
                    .centerCrop()
                    .transform(new CircleTransform(context))
                    .into(subCatPic);

            clickTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    adapterListener.onSelect(data,position);

                    context.startActivity(new Intent(context, BookingDetails.class)
                            .putExtra("order_id", data.getId()));
                }
            });
        }
    }

    public interface FoodSubCatAdapterListener {
        public void onSelect(OrderModel foodMenuItem, int index);
    }

}
