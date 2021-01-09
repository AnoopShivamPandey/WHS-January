package com.apk_home_service.customer.Wallet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

import static com.apk_home_service.customer.Wallet.API_Config.getApiClient_ByPost;


public class MoneyTransferActivity extends AppCompatActivity implements View.OnClickListener {
    EditText paymentSum;
    EditText addNumber;
    double walletBalance = 0;
    ImageView menuImg;
    Activity mActivity;
    TextView scanCode;
    TextView confirmTxt;
    private IntentIntegrator qrScan;
    UserData userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer);
        mActivity = this;
        initialize();
        try {
            userData = (UserData) PrefrenceHandler.getPreferences(this).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        qrScan = new IntentIntegrator(this);

        qrScan.setPrompt("Scan the My Treat Code");

        qrScan.setOrientationLocked(false);
        walletBalance = Double.parseDouble(getIntent().getStringExtra("walletBalance"));
    }

    void initialize() {
        menuImg = findViewById(R.id.menuImg);
        paymentSum = findViewById(R.id.paymentSum);
        addNumber = findViewById(R.id.addNumber);
        scanCode = findViewById(R.id.scanCode);
        confirmTxt = findViewById(R.id.confirmTxt);
        menuImg = findViewById(R.id.menuImg);
        confirmTxt.setOnClickListener(this);
        menuImg.setOnClickListener(this);
        scanCode.setOnClickListener(this);

        paymentSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (s.length() > 0) {
                    double value = Double.parseDouble(s);
                    if (value > walletBalance) {
                        Toast.makeText(mActivity, "The sum cannot be greater than wallet balance.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    boolean checkIsValid(String value) {
        try {
            if (value.length() != 14) {
                return false;
            }
            for (int i = 0; i < value.length(); i++) {
                if (!Character.isDigit(value.charAt(i))) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void transferMoney() {
        String otp = new GlobalAppApis().transferDetails(userData.getqrcode(),addNumber.getText().toString().trim(), paymentSum.getText().toString().trim());
        ApiService client = getApiClient_ByPost();
        System.out.println("==API TOKEN IS "+ PrefrenceHandler.getPreferences(this).getAPI_TOKEN());
        Call<String> call = client.transferMoney( otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("status")) {
                    Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                    if (jsonObject.has("data") && jsonObject.getJSONObject("data").length() > 0) {
                        Intent intent = getIntent();

//                        startActivity(intent);
                    } else {

                    }
                } else {
                    try {
                        Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }, mActivity, call, "", true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
//                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    System.out.println("==result is " + result.getContents());
                    if (checkIsValid(result.getContents())) {
                        addNumber.setText(result.getContents());
                        addNumber.setSelection(addNumber.getText().toString().trim().length());
                    } else {
                        Toast.makeText(this, "Invalid Code.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    String errorMessage = "";

    boolean isValid() {
        if (paymentSum.getText().toString().trim().length() == 0) {
            errorMessage = "Please add the sum to transfer.";
            return false;
        } else if (paymentSum.getText().toString().trim().equalsIgnoreCase("0")) {
            errorMessage = "Transfer amount cannot be 0.";
            return false;
        } else if (addNumber.getText().toString().trim().length() == 0) {
            errorMessage = "Please add the code to transfer the sum.";
            return false;
        } else if (addNumber.getText().toString().trim().length() < 14) {
            errorMessage = "Please enter a valid code.";
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scanCode:
                qrScan.initiateScan();
                break; case R.id.menuImg:
                finish();
                break;
            case R.id.confirmTxt:
                if (isValid()) {
                    transferMoney();
                } else {
                    Toast.makeText(mActivity, errorMessage, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
