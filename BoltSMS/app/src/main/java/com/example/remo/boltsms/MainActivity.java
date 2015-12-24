package com.example.remo.boltsms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private EditText etNumber;
    private EditText etMsg;
    IntentFilter intentFilter;

    private BroadcastReceiver intentReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Display the message in text view
            TextView tvResult=(TextView)findViewById(R.id.tvResult);
            tvResult.setText(intent.getExtras().getString("sms"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter=new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        etNumber=(EditText)findViewById(R.id.etNumber);
        etMsg=(EditText)findViewById(R.id.etMsg);

    }

    public void onClick(View view) {
        String myNumber=etNumber.getText().toString();
        String myMessage=etMsg.getText().toString();
        sendMsg(myNumber,myMessage);
    }

    public void sendMsg(String myNumber,String myMessage){
        String SENT="Message Sent";
        String DELIVERED="Message Delivered";

        PendingIntent sentPI=PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        PendingIntent deliveredPI=PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,"SMS sent",Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(),"Generic Failure",Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(),"No Service",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        },new IntentFilter(SENT));

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(),"SMS delivered",Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(),"SMS Cancelled",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        },new IntentFilter(DELIVERED));

        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(myNumber,null,myMessage,sentPI,deliveredPI);

    }

    @Override
    protected void onResume() {
        registerReceiver(intentReceiver,intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(intentReceiver);
        super.onPause();
    }
}
