package com.apk_home_service.customer.controll;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apk_home_service.customer.R;


/**
 * Created by Rajnish on 6/29/2017.
 */

public class QuantityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Activity context;
    QuantityAdapterListener adapterListener;

    int maxQnty=1;


    public QuantityAdapter(Activity context, int maxQnty) {
        this.context = context;
        this.maxQnty = maxQnty;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.qnty_slot_view, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DateHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return maxQnty;
    }

    public void setAdapterListener(QuantityAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }


    class DateHolder extends RecyclerView.ViewHolder {

        TextView qntyTxt;

        public DateHolder(View itemView) {
            super(itemView);
            qntyTxt = (TextView) itemView.findViewById(R.id.qntyTxt);
        }

        public void onBind(final int position) {
            qntyTxt.setText(""+(position+1));

            qntyTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterListener.onSelect(position+1);
                }
            });
        }
    }

    public interface QuantityAdapterListener {
        public void onSelect(int qnty);
    }

}
