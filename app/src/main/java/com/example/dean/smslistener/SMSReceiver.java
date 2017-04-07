package com.example.dean.smslistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * Created by Dean on 09/03/2017.
 */

public class SMSReceiver extends BroadcastReceiver {
    Vehicle vehicle;
    Recipient recipient;
    // SmsManager class is responsible for all SMS related actions
    final SmsManager sms = SmsManager.getDefault();
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message received
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    // This will create an SmsMessage object from the received pdu
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    // Get sender phone number
                    String phoneNumber = sms.getDisplayOriginatingAddress();
                    String sender = phoneNumber;
                    String message = sms.getDisplayMessageBody();
                    String formattedText = String.format(context.getResources().getString(R.string.sms_message), sender, message);
                    // Display the SMS message in a Toast
                    //Toast.makeText(context, formattedText, Toast.LENGTH_LONG).show();
                    MainActivity inst = MainActivity.instance();
                    //inst.updateList(formattedText);
                    String mob = formattedText.substring(7, 20);

//                    if(message.substring(0,5).equals("CHECK"))
//                    {
                        String registrationNum;
                        //registrationNum = message.substring(message.lastIndexOf("|") + 1);
                    registrationNum = message;

                    //Check whether its an automated number ie 25467 STOP.
                    if(formattedText.substring(7, 20).length() == 13 && sms.getDisplayMessageBody().length() <=7)
                    {
                        vehicle = new Vehicle();
                        recipient = new Recipient();

                        vehicle.Reg = sms.getDisplayMessageBody();
                        recipient.MobileNumber = formattedText.substring(7, 20);
                        inst.getVehicleDetails(vehicle, recipient);
                    }

                    //inst.getCarMake(registrationNum, mob);
                   // }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}