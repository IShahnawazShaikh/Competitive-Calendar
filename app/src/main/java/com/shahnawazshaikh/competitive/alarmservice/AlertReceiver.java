package com.shahnawazshaikh.competitive.alarmservice;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.shahnawazshaikh.competitive.R;
import com.shahnawazshaikh.competitive.models.ContestBean;

public class AlertReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String event=intent.getStringExtra("event");
        String start=intent.getStringExtra("start");
        String end=intent.getStringExtra("end");
        String StartDate=intent.getStringExtra("StartDate");
        String EndDate=intent.getStringExtra("EndDate");

       // System.out.println("...............................set......................................."+event);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"competitive")
                .setContentText("Start  "+StartDate+" | "+start+"\nEnd  "+EndDate+" | "+end)
                .setTicker("New Message Alert!")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(""+event)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200,builder.build());
        //notificationManagerCompat.cancel(200);
      //  builder.setAutoCancel(true);

    }
}
