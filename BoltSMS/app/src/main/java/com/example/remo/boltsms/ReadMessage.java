package com.example.remo.boltsms;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class ReadMessage extends BroadcastReceiver {

    private SharedPreferences preferences;
    private TextView tvNumber;
    private TextView tvMsgBody;


    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from="";
            String msgbody="";
            if(bundle!=null){
                try{
                    Object[] pdus = (Object[])bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i=0; i<msgs.length;i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress().toString();
                        msgbody  = msgs[i].getMessageBody().toString();
                    }
                    Notification.Builder builder = new Notification.Builder(context);
                    builder.setContentTitle(msg_from);
                    builder.setContentText(msgbody);
                    builder.setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_launcher));
                    builder.setSmallIcon(R.drawable.ic_launcher);

                    NotificationManager manager =(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
                    manager.notify();

                   //tvNumber.setText(msg_from);
                   //tvMsgBody.setText(msgbody);
                }catch (Exception ex){
                    Log.d("Exception caught", ex.getMessage());
                }
            }
        }
    }
}
