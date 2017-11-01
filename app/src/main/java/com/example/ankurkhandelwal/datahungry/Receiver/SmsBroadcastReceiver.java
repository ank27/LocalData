package com.example.ankurkhandelwal.datahungry.Receiver;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.example.ankurkhandelwal.datahungry.R;
import com.example.ankurkhandelwal.datahungry.DataHungryApplication;
import com.example.ankurkhandelwal.datahungry.MainActivity;
import com.example.ankurkhandelwal.datahungry.Models.SMS;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";
    public static final String TAG = "SMSBroadCastReceiver";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            String userdata = "";
            String address = "";
            String smsBody = "";
            for (Object obj : sms) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                smsBody = smsMessage.getMessageBody();
                address = smsMessage.getOriginatingAddress();
                smsMessageStr = (smsMessageStr + "SMS From: " + address + "\n") + smsBody + "\n";
                for (byte b : smsMessage.getUserData()) {
                    userdata = userdata + Byte.toString(b);
                }
            }
            Log.d("Receiver ", "Sender  " + address + " msg " + smsBody);
            SMS sms_save = new SMS(address, String.valueOf(System.currentTimeMillis() / 1000), smsBody, "2", String.valueOf(0));
            DataHungryApplication.smsArrayList.add(0, sms_save);
            DataHungryApplication.smsArrayListFull.add(0, sms_save);
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            Notification noti = new Builder(context).setContentTitle("New SMS from " + address).setContentText(smsBody).setSmallIcon(R.drawable.ic_sms_small_icon).setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, 0)).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_sms)).build();
            noti.flags |= 16;
//            ((NotificationManager) context.getSystemService(Context.INTEN)).notify(1, noti);
        }
    }
}
