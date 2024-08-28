package com.samajsocials.plugins;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;


@CapacitorPlugin(name = "SmsRetriever")
public class SmsRetrieverPlugin extends Plugin {

    private BroadcastReceiver smsReceiver;

    @PluginMethod
    public void startSmsRetriever(PluginCall call) {
        SmsRetrieverClient client = SmsRetriever.getClient(getContext());
        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(aVoid -> {
            JSObject ret = new JSObject();
            ret.put("value", "SMS retriever started");
            call.resolve(ret);
            registerReceiver();
        });

        task.addOnFailureListener(e -> {
            call.reject("Error starting SMS retriever", e);
        });
    }

    private void registerReceiver() {
        if (smsReceiver != null) {
            return;
        }

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                    Bundle extras = intent.getExtras();
                    Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                    switch(status.getStatusCode()) {
                        case CommonStatusCodes.SUCCESS:
                            String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                            // Extract the OTP code from the message
                            String otp = extractOtpFromMessage(message);
                            onOtpReceived(otp);
                            break;
                        case CommonStatusCodes.TIMEOUT:
                            onOtpTimeOut();
                            break;
                    }
                }
            }
        };

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
             getContext().registerReceiver(smsReceiver, intentFilter, Context.RECEIVER_EXPORTED);
         } else {
             getContext().registerReceiver(smsReceiver, intentFilter);
         }
    }

    private String extractOtpFromMessage(String message) {
        String[] parts = message.split(" ");
        for (String part : parts) {
            if (part.matches("\\d{4,6}")) {
                return part;
            }
        }
        return null;
    }

    private void onOtpReceived(String otp) {
        JSObject ret = new JSObject();
        ret.put("otp", otp);
        notifyListeners("otpReceived", ret);
        unregisterReceiver();
    }

    private void onOtpTimeOut() {
        notifyListeners("otpTimeout", new JSObject());
        unregisterReceiver();
    }

    private void unregisterReceiver() {
        if (smsReceiver != null) {
            getContext().unregisterReceiver(smsReceiver);
            smsReceiver = null;
        }
    }

    @Override
    protected void handleOnDestroy() {
        unregisterReceiver();
    }
}
