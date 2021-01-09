package com.apk_home_service.customer.UI.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.OrderTrackActivity;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.controll.FragmentListener;
import com.apk_home_service.customer.controll.OrderListAdapter;
import com.apk_home_service.customer.controll.PaginationScrollListener;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.OrderModel;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.utill.CommonUtill;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;

import static com.apk_home_service.customer.Wallet.API_Config.getApiClient_ByPost;
import static com.apk_home_service.customer.controll.FragmentListener.callType.MENU_CLICK;


/**
 * Created by Rajnish on 6/30/2017.
 */

public class OrderListFragment extends Fragment {

    View mainView;
    FragmentListener fragmentListener;
    UserData userData;
    ProgressDialog pDialog;
    Activity mActivity;
    int pageNo = 1;
    boolean isLast = false;
    boolean isLoad = false;

    RecyclerView orderList;
    ArrayList<OrderModel> orderArrayList = new ArrayList<>();
    OrderListAdapter orderListAdapter;

    OrderListAdapter.FoodSubCatAdapterListener foodSubCatAdapterListener = new OrderListAdapter.FoodSubCatAdapterListener() {
        @Override
        public void onSelect(OrderModel orderItem, int index) {
            Intent intent = new Intent(getActivity(), OrderTrackActivity.class);
            intent.putExtra("orderItem", orderItem);
            startActivity(intent);
        }
    };



    public void setFragmentListener(FragmentListener fragmentListener1) {
        fragmentListener = fragmentListener1;
    }
TextView noRecordsText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mActivity = getActivity();
        pDialog = CommonUtill.showProgressDialog(getActivity(), "Please wait...");

        try {
            userData = (UserData) PrefrenceHandler.getPreferences(getActivity()).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mainView = inflater.inflate(R.layout.fragment_orders, container, false);

        orderList = (RecyclerView) mainView.findViewById(R.id.orderList);
        noRecordsText = mainView.findViewById(R.id.noRecordsText);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        orderList.setLayoutManager(linearLayoutManager);
        showNoRecords(true);

        orderListAdapter = new OrderListAdapter(getActivity(), orderArrayList);
        orderListAdapter.setAdapterListener(foodSubCatAdapterListener);
        orderList.setAdapter(orderListAdapter);

//        implementPagination(linearLayoutManager);

        mainView.findViewById(R.id.menuImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListener != null) {
                    Bundle data = new Bundle();
                    fragmentListener.fragmentCallBack(data, OrderListFragment.class.getName(), MENU_CLICK);
                }
            }
        });

        getOrderList();

        return mainView;
    }




    private void implementPagination(LinearLayoutManager linearLayoutManager) {
        orderList.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoad = true;
                pageNo++;
                CommonUtill.print("loadMoreItems ==" + pageNo);
                getOrderList();
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return isLast;
            }

            @Override
            public boolean isLoading() {
                return isLoad;
            }
        });
    }

    public void getOrderList() {
        if(userData.getUserId()==null || userData.getUserId().length()==0)
            return;
        String otp = new GlobalAppApis().getBookingList(userData.getUserId());
        ApiService client = getApiClient_ByPost();
        Call<String> call = client.getBookingList(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("status")) {
                    if (jsonObject.has("data") && jsonObject.getJSONArray("data").length() > 0) {
                        showNoRecords(false);
                      
                            Gson gson = new Gson();

                            ArrayList<OrderModel> orderArrayListTemp = new ArrayList<>();
                        orderArrayListTemp.clear();
                        orderArrayList.clear();
                            JsonElement element = gson.fromJson(jsonObject.getJSONArray("data").toString(), JsonElement.class);

                            Type listType = new TypeToken<ArrayList<OrderModel>>() {
                            }.getType();

                            orderArrayListTemp = gson.fromJson(element, listType);

                        orderArrayList.clear();
                            orderArrayList.addAll(orderArrayListTemp);
                            orderListAdapter.refreshList(orderArrayList);



                        } else {

                        showNoRecords(true);
                    }
                } else {
                    showNoRecords(true);
                }
            }

        }, mActivity, call, "", true);


    }
    private void showNoRecords(boolean show) {
        if (show) {
            noRecordsText.setVisibility(View.VISIBLE);
            orderList.setVisibility(View.GONE);
        } else {
            noRecordsText.setVisibility(View.GONE);
            orderList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderList();

//        if (fragmentListener != null) {
//            Bundle data = new Bundle();
//            data.putString("fragment", ProfileFragment.class.getName());
//            data.putSerializable("action", INIT_HEADER);
//            fragmentListener.fragmentCallBack(data);
//        }
    }

}
