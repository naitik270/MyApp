package com.demo.nspl.restaurantlite.Receives;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MySmsBroadcastReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Log.e("BROADCAST RECEIVER", "onReceive called()");
//        Bundle data = intent.getExtras();
//
//        Object[] pdus = (Object[]) data.get("pdus");
//
//        for (int i = 0; i < pdus.length; i++) {
//
//            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
//
//            String sender = smsMessage.getDisplayOriginatingAddress();
//            //You must check here if the sender is your provider and not another one with same text.
//            String messageBody = smsMessage.getMessageBody();
//
//            Log.e("sender", sender);
//
//            String getCodeFrom_messageBody = "";
//
//            Pattern p = Pattern.compile("\\d{4}");
//            Matcher m = p.matcher(messageBody);
//
//            while (m.find()) {
//                getCodeFrom_messageBody = m.group();
//            }
//
//            Log.e("getCode", getCodeFrom_messageBody);
//            Log.e("Textget", messageBody);
//            OtpVerificationActivity.recivedSms(getCodeFrom_messageBody);
//            ActivityForgotPasswordOTP.setOtpAutoFill(getCodeFrom_messageBody);
//            ActivityPaymentProcess.receivedSms(getCodeFrom_messageBody);
//            ActivityChangeMobileNum.changeMobileNo(getCodeFrom_messageBody);
//            ActivityCashCollectionOtp.verificationOtp(getCodeFrom_messageBody);
//
//        }
//
//
//    }

}


