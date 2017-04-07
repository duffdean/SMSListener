package com.example.dean.smslistener;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity
{
    private static final String INBOX_URI = "content://sms/inbox";
    private static MainActivity activity;
    ArrayList<String> htmlToString;
    final public static int SEND_SMS = 101;
    Button checkButton;
    EditText regTextField;
    Source source;
    ResultsParser rp;

    //private ArrayList<String> smsList = new ArrayList<String>();
    //private ListView mListView;
    //private ArrayAdapter<String> adapter;
    // private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    //    String motDate;
//    String taxDate;
//    String makeValue;
    //Vehicle vehicle;
    //String mobile;
    //    String reg;
//    String mobileNumber;

    long executeStart, executeEnd;

    public static MainActivity instance() {
        return activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        source = new Source();
        checkButton = (Button) findViewById(R.id.button2);
        regTextField = (EditText) findViewById(R.id.editText2);

//        checkButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                checkRegFromMain("CHECK|" + regTextField.getText().toString().toUpperCase());
//            }
//        });

//        mListView = (ListView) findViewById(R.id.list);
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsList);
//        mListView.setAdapter(adapter);
//        mListView.setOnItemClickListener(MyItemClickListener);
        readSMS();
    }
    @Override
    public void onStart() {
        super.onStart();
        activity = this;
    }
    public void readSMS() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse(INBOX_URI), null, null, null, null);
        int senderIndex = smsInboxCursor.getColumnIndex("address");
        int messageIndex = smsInboxCursor.getColumnIndex("body");
        if (messageIndex < 0 || !smsInboxCursor.moveToFirst()) return;
        //adapter.clear();
        do {
            String sender = smsInboxCursor.getString(senderIndex);
            String message = smsInboxCursor.getString(messageIndex);
            String formattedText = String.format(getResources().getString(R.string.sms_message), sender, message);
            //adapter.add(Html.fromHtml(formattedText).toString());
        } while (smsInboxCursor.moveToNext());
    }
//    public void updateList(final String newSms) {
////        adapter.insert(newSms, 0);
////        adapter.notifyDataSetChanged();
//    }

    public void getVehicleDetails(final Vehicle vehicle, final Recipient recipient)
    {
        //DEAN
        executeStart = System.currentTimeMillis();
        Log.d("DEAN - Execute Start: ", ""+executeStart);
        Log.d("DEAN - MSUPER...", "");

        Ion.with(getApplicationContext())
                .load(source.MSUPER + vehicle.Reg)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        rp = new ResultsParser(result);

                        vehicle.Model = rp.GetModelFromReg(result);
                        vehicle.Make = rp.GetMakeFromReg(result);

                        getTaxMOTStatus(vehicle, recipient);
                    }
                });
    }

    public void getTaxMOTStatus(final Vehicle vehicle, final Recipient recipient)
    {
        Ion.with(getApplicationContext())
                .load(source.GOVUK + vehicle.Reg + "&Make=" + vehicle.Make)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        result = result.replace("\n", "").replace("\r", "");

                        vehicle.TaxDate = rp.getTaxFromGov(result);
                        vehicle.MotDate = rp.getMOTFromGov(result);

//                        htmlToString = vehicle.Reg.toUpperCase() + "\nMake: " + vehicle.Make + "\nTax: " + vehicle.TaxDate + "\nMOT: "  + vehicle.MotDate;
//
//                        checkAndroidVersion(recipient);

                        generateSMSBody(vehicle, recipient);
                    }
                });
    }

    public void generateSMSBody(final Vehicle vehicle, final Recipient recipient)
    {
        MessageBody body = new MessageBody();

//        if(vehicle.hasMOT(vehicle.MotDate) && vehicle.hasTax(vehicle.TaxDate))
//        {
//            htmlToString = body.TaxMotValid(vehicle);
//        }
//        else if(vehicle.hasMOT(vehicle.MotDate) && !vehicle.hasTax(vehicle.TaxDate))
//        {
//            htmlToString = body.TaxInvalid(vehicle);
//        }
//        else if(!vehicle.hasMOT(vehicle.MotDate) && vehicle.hasTax(vehicle.TaxDate))
//        {
//            htmlToString = body.MOTInvalid(vehicle);
//        }
//        else
//        {
//            htmlToString = body.TaxMOTInvalid(vehicle);
//        }

        if(!vehicle.MotDate.equals("No Mot") && !vehicle.TaxDate.equals("No Mot"))
        {
            htmlToString = body.TaxMotValid(vehicle);
        }
        else if(!vehicle.MotDate.equals("No Mot") && vehicle.TaxDate.equals("No Mot"))
        {
            htmlToString = body.TaxInvalid(vehicle);
        }
        else if(vehicle.MotDate.equals("No Mot") && !vehicle.TaxDate.equals("No Mot"))
        {
            htmlToString = body.MOTInvalid(vehicle);
        }
        else
        {
            htmlToString = body.TaxMOTInvalid(vehicle);
        }

        //htmlToString = vehicle.Reg.toUpperCase() + "\nMake: " + vehicle.Make + "\nTax: " + vehicle.TaxDate + "\nMOT: "  + vehicle.MotDate;

        checkAndroidVersion(recipient);
        if(vehicle.TaxDate.isEmpty() && vehicle.MotDate.isEmpty())
        {
            //DO a certain body etc.
        }
    }
//    public void getDetailsFromMain(String reg){
//        this.reg= reg;
//        if (Build.VERSION.SDK_INT >= 23) {
//            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS);
//            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},SEND_SMS);
//                return;
//            }else{
//                getDetailsFromMain(reg);
//            }
//        } else {
//            getDetailsFromMain(reg);
//        }
//    }

    public void checkAndroidVersion(Recipient recipient){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},SEND_SMS);
                return;
            }else{
                sendSms(recipient);
            }
        } else {
            sendSms(recipient);
        }
    }

//    public void checkRegFromMain(String reg)
//    {
//        try {
//            SmsManager sm = SmsManager.getDefault();
//            sm.sendTextMessage("+447853163879", null, reg, null, null);
////            Intent myIntent = new Intent(MainActivity.this,VerificationActivity.class);
////            myIntent.putExtra("verficationCode", PINString);
////            myIntent.putExtra("phoneNo", edt_phoneNo);
////            MainActivity.this.startActivity(myIntent);
//            //startActivity(smsSIntent);
//        } catch (Exception ex) {
//            Toast.makeText(MainActivity.this, "Your sms has failed...",
//                    Toast.LENGTH_LONG).show();
//            ex.printStackTrace();
//        }
//    }

    public void sendSms(Recipient recipient)
    {
        try {
            SmsManager sm = SmsManager.getDefault();
           // sm.sendTextMessage(recipient.MobileNumber, null, htmlToString, null, null);

//            SmsManager sm = SmsManager.getDefault();
//            ArrayList<String> msg = new ArrayList<String>();
//            String a = "✅";
//            msg.add(htmlToString);
//            msg.add("SPACE");
//            msg.add(a);
//            msg.add("SPACE");
//            msg.add(htmlToString);
            sm.sendMultipartTextMessage(recipient.MobileNumber, null, htmlToString, null, null);

            //DEAN
            executeEnd = System.currentTimeMillis();
            long elapsedTime = executeEnd - executeStart;
            Log.d("DEAN - Execute End: ", ""+executeEnd);
            Log.d("DEAN - Time Taken: ", ""+elapsedTime);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case SEND_SMS:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    sendSms(mobile);
//                } else {
//
//                    Toast.makeText(MainActivity.this, "SEND_SMS Denied", Toast.LENGTH_SHORT)
//                            .show();
//                }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }
}