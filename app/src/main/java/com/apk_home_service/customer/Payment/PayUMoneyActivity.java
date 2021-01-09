package com.apk_home_service.customer.Payment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.apk_home_service.customer.R;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PayUMoneyActivity extends AppCompatActivity {

    private TextView mTxvProductPrice, mTxvBuy;

    private void initViews() {
        setContentView(R.layout.activity_main_);
        mTxvProductPrice = findViewById(R.id.txv_product_price);
        mTxvBuy = findViewById(R.id.txt_buy_product);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            name = "arpit";
            email = "arpit@gmail.com";
            phoneNumber = "9936261569";
            trxID = "12345678";
//            merchantHash = dataJSON.getJSONObject("data").getJSONObject("payment").getString("order_hash");
            payableAmt = "100";

            String hashSequence = Constants.MERCHANT_KEY + "|" + trxID + "|" +
                    payableAmt + "|" +
                    "bat|" +
                    name + "|" + email + "|" +
                    "1" +
                    "|" + "2" +
                    "|" + "3" + "|" +
                    "4" + "|" +
                    "5" +
                    "||||||" + Constants.MERCHANT_SALT;
            merchantHash = hashCal("SHA-512", hashSequence);
            System.out.println("==MERCHANT HASH " + merchantHash);
            System.out.println("==HASH SEQUENCE " + hashSequence);
            merchantHash = "6e07149a062e545f7ac4c8286c9164804ded41ca29ab7e3f2567562d916aa20e5b235ea171cc917f4b427a78ee50545a0cd702786ef3c0a8689dde181d5d6626";
            System.out.println("==MERCHANT HASH " + merchantHash);

        } catch (Exception e) {
            e.printStackTrace();
        }

        initViews();

//        String priceNo = getResources().getString(R.string.txt_product_price);
        String price = getResources().getString(R.string.rupee) + payableAmt;
        mTxvProductPrice.setText(price);

        mTxvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxvBuy.setEnabled(false);
                launchPaymentFlow();
            }
        });
    }

    String name = "";
    String email = "";
    String phoneNumber = "";
    String trxID = "";
    String merchantHash = "";
    String payableAmt = "0";

    String key = "";
    String salt = "";
    String merchantId = "";
    String udf1 = "";
    String udf2 = "";
    String udf3 = "";
    String udf4 = "";
    String udf5 = "";

    public static String hashCal(String type, String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    private void launchPaymentFlow() {
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        payUmoneyConfig.setPayUmoneyActivityTitle("Buy" + "CrazyBilla");
        payUmoneyConfig.setDoneButtonText("Pay " + getResources().getString(R.string.rupee) + payableAmt);


        PayUmoneySdkInitializer.PaymentParam.Builder builder = new
                PayUmoneySdkInitializer.PaymentParam.Builder();
        try {
            builder
                    .setAmount(String.valueOf(convertStringToDouble(payableAmt)))
                    //                .setAmount("1")
                    .setTxnId(trxID)
                    .setPhone(phoneNumber)
                    .setProductName("bat")
                    .setFirstName(name)
                    .setEmail(email)
                    .setsUrl(Constants.SURL)
                    .setfUrl(Constants.FURL)
                    .setUdf1("1")
                    .setUdf2("2")
                    .setUdf3("3")
                    .setUdf4("4")
                    .setUdf5("5")
                    .setUdf6("")
                    .setUdf7("")
                    .setUdf8("")
                    .setUdf9("")
                    .setUdf10("")

                    .setIsDebug(Constants.DEBUG)
                    .setKey(Constants.MERCHANT_KEY)
                    .setMerchantId(Constants.MERCHANT_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            PayUmoneySdkInitializer.PaymentParam paymentParam = builder.build();
//set the hash
//            paymentParam.setMerchantHash(merchantHash);
            paymentParam.setMerchantHash(merchantHash);

            PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam,
                    this, R.style.AppTheme_default, true);
//            calculateHashInServer(mPaymentParams);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            mTxvBuy.setEnabled(true);
        }
    }


    private void calculateHashInServer(final PayUmoneySdkInitializer.PaymentParam mPaymentParams) {
        mPaymentParams.setMerchantHash(merchantHash);
        PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayUMoneyActivity.this, R.style.PayUMoney, true);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTxvBuy.setEnabled(true);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {

            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    showAlert("Payment Successful");
                } else if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.CANCELLED)) {
                    showAlert("Payment Cancelled");
                    System.out.println("ERROR " + transactionResponse.getMessage());
                    System.out.println("ERROR " + transactionResponse.getPayuResponse());
                    System.out.println("ERROR " + transactionResponse.getTransactionDetails());
                    System.out.println("ERROR " + transactionResponse.payuResponse);
                    System.out.println("ERROR " + (transactionResponse.describeContents()));
                } else if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.FAILED)) {
                    showAlert("Payment Failed");
                }

            } else if (resultModel != null && resultModel.getError() != null) {
                Toast.makeText(this, "Error check log", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Both objects are null", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_CANCELED) {
            showAlert("Payment Cancelled");
        }
    }

    private Double convertStringToDouble(String str) {
        return Double.parseDouble(str);
    }

    private void showAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
