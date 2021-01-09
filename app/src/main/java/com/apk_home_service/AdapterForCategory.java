package com.apk_home_service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.RestaurantList;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.apk_home_service.customer.Wallet.Urls.BASE_URL;

public class AdapterForCategory extends RecyclerView.Adapter<AdapterForCategory.ViewHolder> {
    Context context;
    List<Category_data_cat> list = new ArrayList<>();
    GridLayoutManager gridLayoutManager;
     List<SubcategoryData> listSubctegory = new ArrayList<>();
     String url = "subcategory";
     public AdapterForCategory(Context context, List<Category_data_cat> list) {
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
        Picasso.with(context).load(list.get(i).getCategory_image())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo).into(holder.img_cat);
        holder.txt_catname.setText(list.get(i).getCategory_name());
        holder.iv_openmore.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, RestaurantList.class);
                intent.putExtra("lat", "11.654464");
                intent.putExtra("lon", "19.3554");
                intent.putExtra("cat_name",list.get(i).getCategory_name());
                intent.putExtra("id", list.get(i).getId());
                intent.putExtra("from", "cat");
                context.startActivity(intent);
//                if (NetworkConnectionHelper.isOnline(context)) {
//                    context.startActivity(new Intent(context, ActivityProduct.class).putExtra("id", list.get(i).getId()));
//                } else {
//                    Toast.makeText(context, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
//                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.iv_openmore.getRotation() == 0) {
                    final String categoryId = list.get(i).getId();
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.show();
                    progressDialog.setMessage("Loading");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"customer/subcategory", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("status")) {
                                    String jsonInString = jsonObject.getString("data");
                                    if (jsonInString != null && !jsonInString.equalsIgnoreCase("")) {
                                        listSubctegory = SubcategoryData.createJsonInList(jsonInString);
                                        if (listSubctegory != null && listSubctegory.size() > 0) {
                                            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
                                            AdapterForSubCategory myAdapter = new AdapterForSubCategory(context, listSubctegory);
                                            holder.rv_Subcategory.setLayoutManager(gridLayoutManager);
                                            holder.rv_Subcategory.setAdapter(myAdapter);
                                            holder.rv_Subcategory.setHasFixedSize(true);
                                            holder.rv_Subcategory.setVisibility(View.VISIBLE);
                                            holder.iv_openmore.setRotation(180);
                                            holder.txt_catname.setTypeface(Typeface.DEFAULT_BOLD);
                                        } else {
                                            holder.rv_Subcategory.setVisibility(View.GONE);
                                        }
                                    } else {
                                        holder.rv_Subcategory.setVisibility(View.GONE);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("category_id", categoryId);
                            return params;
                            // return super.getParams();
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);
                }else{
                    holder.iv_openmore.setRotation(0);
                    holder.txt_catname.setTypeface(Typeface.DEFAULT);
                    holder.rv_Subcategory.setVisibility(View.GONE);
                }
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cat = (ImageView) itemView.findViewById(R.id.img_cat);
            iv_openmore = (ImageView) itemView.findViewById(R.id.iv_openmore);
            txt_catname = (TextView) itemView.findViewById(R.id.txt_catname);
            rv_Subcategory = (RecyclerView) itemView.findViewById(R.id.rv_Subcategory);
        }
    }
}