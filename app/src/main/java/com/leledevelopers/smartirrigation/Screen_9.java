package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.registration.Screen_2_1;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Screen_9 extends SmsServices {
    private static final String TAG = Screen_9.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private SmsUtils smsUtils = new SmsUtils();
    private Boolean b=false, systemDown = false;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Button setSystemTime, getSystemTime, updatePassword, setMotorloadCutoff, back_9, save;
    private TextView status;
    DecimalFormat mFormat = new DecimalFormat("00");
    Calendar calendar = Calendar.getInstance();
    private double randomNumber=-1;
    private boolean isSetTimeClicked = false;
    private boolean isGetTimeClicked = false;
    private static boolean screen_9_Visible = false;

    private  StringBuffer activityMessage=new StringBuffer("");
    private  boolean handlerActivated=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen9);

        this.context = getApplicationContext();
        initViews();
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.languagesArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        setMotorloadCutoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen_9.this, Screen_10.class));
            }
        });
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                Intent intentU = new Intent(Screen_9.this, Screen_2_1.class);
                intentU.putExtra("Settings", true);
                intentU.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentU);

            }
        });
        setSystemTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                randomNumber = Math.random();
                isSetTimeClicked = true;
                String smsData = smsUtils.OutSMS_10(mFormat.format(Double.valueOf(calendar.get(Calendar.DATE))) + "", mFormat.format(Double.valueOf((calendar.get(Calendar.MONTH) + 1))) + "",
                        mFormat.format(Double.valueOf((calendar.get(Calendar.YEAR)) % 100)) + "", mFormat.format(Double.valueOf(calendar.get(Calendar.HOUR_OF_DAY))) + ""
                        , mFormat.format(Double.valueOf(calendar.get(Calendar.MINUTE))) + "", mFormat.format(Double.valueOf(calendar.get(Calendar.SECOND))) + "");
                activityMessage.replace(0,activityMessage.length(),"Sync System Time SMS ");
                sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber,"Sync System Time SMS ");
                status.setText("Sync System Time SMS Sent\r\nWaiting for response from controller");
            }
        });
        getSystemTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                randomNumber = Math.random();
                activityMessage.replace(0,activityMessage.length(),"Show System Time SMS ");
                isGetTimeClicked = true;
                String smsData = smsUtils.OutSMS_11;
                sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber,"Show System Time SMS ");
                status.setText("Show System Time SMS Sent\r\nWaiting for response from controller");
            }
        });
        back_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                randomNumber=-1;
                Intent intentB=(new Intent(Screen_9.this, Screen_4.class));
                intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentB);
            }
        });
    }


    @Override
    public void initViews() {
        spinner = (Spinner) findViewById(R.id.language_spinner);
        setSystemTime = findViewById(R.id.setSystemTime);
        getSystemTime = findViewById(R.id.getSystemTime);
        updatePassword = findViewById(R.id.updatePassword);
        setMotorloadCutoff = findViewById(R.id.setMotorloadCutoff);
        status = findViewById(R.id.screen_9_status);
        save = findViewById(R.id.save);
        back_9 = findViewById(R.id.back_9);
    }

    @Override
    public void enableViews() {
        spinner.setEnabled(true);
        setSystemTime.setEnabled(true);
        getSystemTime.setEnabled(true);
        updatePassword.setEnabled(true);
        setMotorloadCutoff.setEnabled(true);
    }

    @Override
    public void disableViews() {
        spinner.setEnabled(false);
        setSystemTime.setEnabled(false);
        getSystemTime.setEnabled(false);
        updatePassword.setEnabled(false);
        setMotorloadCutoff.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsReceiver.setContext(getApplicationContext());
        smsReceiver.startBroadcastReceiver();
        smsReceiver.setSmsMessageBroadcast(new SmsReceiver.SmsReceiverBroadcast() {
            @Override
            public void onReceiveSms(String phoneNumber, String message) {
                if (SmsServices.phoneNumber.replaceAll("\\s", "").equals(phoneNumber.replaceAll("\\s", "")) && !systemDown) {
                    checkSMS(message);
                } else if (phoneNumber.contains(SmsServices.phoneNumber.replaceAll("\\s", "")) && !systemDown) {
                    checkSMS(message);
                }
            }

            @Override
            public void checkTime(double randomValue) {
                if (b && (randomNumber == randomValue)&& screen_9_Visible) {
                    enableViews();
                    systemDown = true;
                    handlerActivated=false;
                    smsReceiver.unRegisterBroadCasts();
                    status.setText(SmsUtils.SYSTEM_DOWN);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            randomNumber=-1;
                            Intent intentS=(new Intent(Screen_9.this, MainActivity_GSM.class));
                            intentS.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentS);
                        }
                    }, 5000);

                }
            }

        });

        this.setSmsServiceBroadcast(new SmsServiceBroadcast() {
            @Override
            public void onReceiveSmsDeliveredStatus(boolean smsDeliveredStatus, String message) {
                 if (smsDeliveredStatus) {
                    if(message.equals(activityMessage.toString()) &&!(handlerActivated))
                    //status.setText(message+" sent");
                        handlerActivated=true;
                    smsReceiver.waitFor_1_Minute(randomNumber,smsReceiver);
                    b = true;
                } else {
                    status.setText(message+" sending failed");
                    enableViews();
                    isGetTimeClicked = false;
                    isSetTimeClicked = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();
        screen_9_Visible = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
        screen_9_Visible = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        looper.quit();
        randomNumber=-1;
        Intent intentB=(new Intent(Screen_9.this, Screen_4.class));
        intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentB);
    }

    public void checkSMS(String message) {
        if (message.toLowerCase().contains(SmsUtils.INSMS_10_1.toLowerCase()) && isSetTimeClicked) {
            b = false;
            handlerActivated=false;
            isSetTimeClicked = false;
            status.setText("System time set to current local time");
            enableViews();
        } else if (message.toLowerCase().contains(SmsUtils.INSMS_10_2.toLowerCase())) {
            b = false;
            handlerActivated=false;
            status.setText("Failed to sync system time, please sync system time again");
            enableViews();
        } else if (message.toLowerCase().contains(SmsUtils.INSMS_11_1.toLowerCase()) && isGetTimeClicked) {
            b = false;
            handlerActivated=false;
            isGetTimeClicked = false;
            /*status.setText(SmsUtils.INSMS_11_1 + (mFormat.format(Double.valueOf(calendar.get(Calendar.DATE))) + "/" + mFormat.format(Double.valueOf((calendar.get(Calendar.MONTH) + 1))) + "/"
                    + mFormat.format(Double.valueOf((calendar.get(Calendar.YEAR)) % 100)))+" "+mFormat.format(Double.valueOf(calendar.get(Calendar.HOUR_OF_DAY))) + ":"
                    + mFormat.format(Double.valueOf(calendar.get(Calendar.MINUTE))) + ":"+ mFormat.format(Double.valueOf(calendar.get(Calendar.SECOND))));*/
            status.setText(message);
            enableViews();
        }
    }
}