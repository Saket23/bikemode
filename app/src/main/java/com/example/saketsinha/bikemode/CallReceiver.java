package com.example.saketsinha.bikemode;

import java.lang.reflect.Method;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
    Log.d("a","b");
        try {
            System.out.println("Receiver start");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            View view = View.inflate(context, R.layout.activity_main, null);
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Toast.makeText(context, "Incoming Call State", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Ringing State Number is -" + incomingNumber, Toast.LENGTH_SHORT).show();
                System.out.println("Extra State Ringing");
                System.out.println("tvonoff1"+MainActivity.tvonoff);
                System.out.println("tvonoff"+MainActivity.tvonoff.getText());
                System.out.println("tvphone"+MainActivity.tvphone.getText());
                System.out.println(MainActivity.tvphone.getText().toString().substring(7).replaceAll("\\s+",""));
                System.out.println("incomingcall "+incomingNumber);
                if(incomingNumber.replaceAll("\\s+","").contains(MainActivity.tvphone.getText().toString().substring(7).replaceAll("\\s+",""))) {
                    if (MainActivity.tvonoff.getText().toString().equals("On")) {
                        Log.d("msg", "1" + MainActivity.tvonoff.getText().toString());
                        if (!killCall(context)) { // Using the method defined earlier
                            Log.d("msg", "PhoneStateReceiver **Unable to kill incoming call");
                        }
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(incomingNumber, null, "Hi, I am On Bike !! Will call back in sometime", null, null);
                    } else {
                        Log.d("msg", "2" + MainActivity.tvonoff.getText().toString());
                    }
                }
                    }
                /*});*/

            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                Toast.makeText(context,"Call Received State",Toast.LENGTH_SHORT).show();
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context,"Call Idle State",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public boolean killCall(Context context) {
        try {
            // Get the boring old TelephonyManager
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.d("msg","PhoneStateReceiver **" + ex.toString());
            return false;
        }
        return true;
    }
}