package com.leledevelopers.smartirrigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.registration.Screen_1;
import com.leledevelopers.smartirrigation.registration.Screen_2_1;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity_GSM extends SmsServices {
    private static final String TAG = MainActivity_GSM.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private TextView smsLabel, status;
    private Handler handler = new Handler();
    private Button connect, resetConnection, showMessages;
    private Boolean b=false, extra = false, systemDown = false;
    private Intent extraIntent;
    private Bundle bundle;
    private double randomNumber;
    private static boolean Mainacitivity_GSM_Visible = false;
    private  StringBuffer activityMessage=new StringBuffer("");
    private  boolean handlerActivated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity_gsm);
        this.context = getApplicationContext();
        initViews();
        readUserFile();
        try {
            extraIntent = getIntent();
            bundle = extraIntent.getExtras();
            if ((bundle != null)) {
                extra = bundle.getBoolean("newUser");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                if (!systemDown) {
                    randomNumber = Math.random();
                    activityMessage.replace(0,activityMessage.length(),"Authentication SMS ");
                     sendMessage(SmsServices.phoneNumber, SmsUtils.OutSMS_2, status, smsReceiver, randomNumber,"Authentication SMS ");
                    status.setText("Authentication SMS sent\r\nWaiting for response from controller");
                }

            }
        });

        resetConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();
            }
        });

        showMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Show Field/Date Wise Messages");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity_GSM.this, Report_SMS.class));
                        finish();
                    }
                }, 1000);

            }
        });
    }

    private void openDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to reset the data");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        new File(MainActivity_GSM.this.getExternalFilesDir(null) + ProjectUtils.FILE_PATH).delete();
                        status.setText("Reset Successful");
                        disableViews();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intentR=(new Intent(MainActivity_GSM.this, Screen_1.class));
                                intentR.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intentR);

                            }
                        }, 1000);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void initViews() {
        smsLabel = findViewById(R.id.screen_3_1_textview_1);
        connect = findViewById(R.id.screen_3_1_button_1);
        resetConnection = findViewById(R.id.screen_3_1_button_2);
        showMessages = findViewById(R.id.screen_3_1_button_3);
        status = findViewById(R.id.screen_3_1_status);
    }

    @Override
    public void enableViews() {
        connect.setEnabled(true);
        resetConnection.setEnabled(true);
        showMessages.setEnabled(true);
    }

    @Override
    public void disableViews() {
        connect.setEnabled(false);
        resetConnection.setEnabled(false);
        showMessages.setEnabled(false);
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
                 if (b && (randomNumber == randomValue) && Mainacitivity_GSM_Visible) {
                    handlerActivated=false;
                    systemDown = true;
                    disableViews();
                    smsReceiver.unRegisterBroadCasts();
                    status.setText(SmsUtils.SYSTEM_DOWN);

                }

            }

        });

        this.setSmsServiceBroadcast(new SmsServiceBroadcast() {
            @Override
            public void onReceiveSmsDeliveredStatus(boolean smsDeliveredStatus,String message) {
                 if (smsDeliveredStatus) {
                    if(message.equals(activityMessage.toString()) && !(handlerActivated)){
                        smsReceiver.waitFor_1_Minute(randomNumber,smsReceiver);
                        handlerActivated=true;
                        b = true;
                    }

                }
                else
                {
                    status.setText(message+" sending failed");
                    enableViews();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();
        Mainacitivity_GSM_Visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
        Mainacitivity_GSM_Visible = false;
    }

    public void checkSMS(String message) {
        if (message.toLowerCase().contains(SmsUtils.INSMS_2_1.toLowerCase())) {
            handlerActivated=false;
            b = false;
            status.setText("System Connected");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (extra) {
                        Intent intent9 = new Intent(MainActivity_GSM.this, Screen_9.class);
                        intent9.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent9);

                    } else {
                        Intent intentM = new Intent(MainActivity_GSM.this, Screen_4.class);
                        intentM.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentM);

                    }
                }
            }, 1000);
        } else if (message.toLowerCase().contains(SmsUtils.INSMS_2_2.toLowerCase())) {
            b = false;
            handlerActivated=false;
            status.setText("Authentication failed, please reauthenticate device");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity_GSM.this, Screen_2_1.class));
                    finish();
                }
            }, 1000);
        }
    }

    private void readUserFile() {
        File file = new File(MainActivity_GSM.this.getExternalFilesDir(null) + ProjectUtils.FILE_PATH);
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    text.append(line);
                }
                String[] s = text.toString().split("[#]");
                SmsServices.phoneNumber = s[0];
                smsLabel.setText(s[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        randomNumber-=1;
    }
}