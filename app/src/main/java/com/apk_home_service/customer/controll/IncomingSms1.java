package com.apk_home_service.customer.controll;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by hp on 09-Apr-18.
 */

public class IncomingSms1 extends BroadcastReceiver {

//    private SmsListener mSmsListener;

    public static final String SMS_RECEIVED = "com.apk_home_service.customer.SMS_RECEIVED";

    @Override public void onReceive(Context context, Intent intent) {
        Log.e("BROADCAST RECEIVER", "onReceive called()");
        Bundle data  = intent.getExtras();
        Log.e("BROADCAST RECEIVER", data.toString());
        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();

            System.out.println(sender+"=="+smsMessage.getMessageBody());

            //You must check here if the sender is your provider and not another one with same text.
            if(sender.contains("CZBLLA")){
                String messageBody = smsMessage.getMessageBody();
                System.out.println(sender+"--mess--"+messageBody);
                System.out.println("--smsMessage---");
                if(!messageBody.isEmpty()){
                    String otp=messageBody.split("\\s+")[0];
                    String  remove_character= otp.replace(".","");
                    if(null !=remove_character){
                        System.out.println("--otp--"+remove_character);
//                        if(null !=mSmsListener)
//                            mSmsListener.messageReceived(sender,remove_character);

                        Intent intent1 = new Intent(SMS_RECEIVED);
                        intent1.putExtra("sender",sender);
                        intent1.putExtra("msg",remove_character);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

                    }
                }


            }


            //Pass on the text to our listener.
            // mSmsListener.messageReceived(sender, messageBody);


        }

    }

//    public void bindListener(SmsListener listener){
//        System.out.println("---bindListener---");
//        mSmsListener = listener;
//    }
//
//
//    public interface SmsListener {
//        void messageReceived(String sender, String messageText);
//    }

}
