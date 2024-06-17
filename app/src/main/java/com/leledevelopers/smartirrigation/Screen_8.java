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

import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.text.DecimalFormat;

public class Screen_8 extends SmsServices {
    private static final String TAG = Screen_8.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private SmsUtils smsUtils = new SmsUtils();
    private Boolean b=false, systemDown = false;
    ArrayAdapter<CharSequence> adapter1, adapter2;
    private Spinner fieldSpinner1, fieldSpinner2;
    private Button getFieldData, getSensordData, getFiltrationData, getMotorLoadData, back_9_1;
    private TextView status;
    DecimalFormat mFormat = new DecimalFormat("00");
    private double randomNumber=-1;
    private boolean isGetFieldDataClicked = false;
    private boolean isGetSensordDataClicked = false;
    private boolean isGetFiltrationDataClicked = false;
    private boolean isGetMotorLoadDataClicked = false;
    private static boolean screen_9_1_Visible = false;

    private  StringBuffer activityMessage=new StringBuffer("");
    private  boolean handlerActivated=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen8);

        this.context = getApplicationContext();
        initViews();
        adapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpinner1.setAdapter(adapter1);

        adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpinner2.setAdapter(adapter2);


        getFieldData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                randomNumber = Math.random();
                activityMessage.replace(0,activityMessage.length(),"Get Field Data SMS ");
                isGetFieldDataClicked = true;
                int fieldNo = Integer.parseInt(fieldSpinner1.getSelectedItem().toString());
                String smsData = smsUtils.OutSMS_13((fieldNo < 10 ? String.format("%02d", fieldNo) : fieldNo + ""));
                sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber,"Get Field Data SMS ");
                status.setText("Get Irrigation Data SMS Sent\r\nWaiting for response from controller");
            }
        });
        getSensordData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                randomNumber = Math.random();
                activityMessage.replace(0,activityMessage.length(),"Get Sensor Data SMS ");
                isGetSensordDataClicked = true;
                int fieldNo = Integer.parseInt(fieldSpinner2.getSelectedItem().toString());
                String smsData = smsUtils.OutSMS_14((fieldNo < 10 ? String.format("%02d", fieldNo) : fieldNo + ""));
                sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber,"Get Sensor Data SMS ");
                status.setText("Get Sensor Data SMS Sent\r\nWaiting for response from controller");
            }
        });
        getFiltrationData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                randomNumber = Math.random();
                activityMessage.replace(0,activityMessage.length(),"Get Filtration Data SMS ");
                isGetFiltrationDataClicked = true;
                String smsData = smsUtils.OutSMS_15;
                sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber,"Get Filtration Data SMS ");
                status.setText("Get Filtration Data SMS Sent\r\nWaiting for response from controller");
            }
        });
        getMotorLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                randomNumber = Math.random();
                activityMessage.replace(0,activityMessage.length(),"Get Motor Load Data SMS ");
                isGetMotorLoadDataClicked = true;
                String smsData = smsUtils.OutSMS_16;
                sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber,"Get Motor Load Data SMS ");
                status.setText("Get Motor Load Data SMS Sent\r\nWaiting for response from controller");
            }
        });
        back_9_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                randomNumber=-1;
                Intent intentB=(new Intent(Screen_8.this, Screen_4.class));
                intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentB);
            }
        });
    }


    @Override
    public void initViews() {
        fieldSpinner1 = (Spinner) findViewById(R.id.fieldspinner1);
        fieldSpinner2 = (Spinner) findViewById(R.id.fieldspinner2);
        getFieldData = findViewById(R.id.getFieldData);
        getSensordData = findViewById(R.id.getSensordData);
        getFiltrationData = findViewById(R.id.getFiltrationData);
        getMotorLoadData = findViewById(R.id.getMotorLoadData);
        status = findViewById(R.id.screen_9_1_status);
        back_9_1 = findViewById(R.id.back_9_1);
    }

    @Override
    public void enableViews() {
        fieldSpinner1.setEnabled(true);
        fieldSpinner2.setEnabled(true);
        getFieldData.setEnabled(true);
        getSensordData.setEnabled(true);
        getFiltrationData.setEnabled(true);
        getMotorLoadData .setEnabled(true);
    }

    @Override
    public void disableViews() {
        fieldSpinner1.setEnabled(false);
        fieldSpinner2.setEnabled(false);
        getFieldData.setEnabled(false);
        getSensordData.setEnabled(false);
        getFiltrationData.setEnabled(false);
        getMotorLoadData .setEnabled(false);
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
                if (b && (randomNumber == randomValue)&& screen_9_1_Visible) {
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
                            Intent intentS=(new Intent(Screen_8.this, MainActivity_GSM.class));
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
                    isGetFieldDataClicked = false;
                    isGetSensordDataClicked = false;
                    isGetFiltrationDataClicked = false;
                    isGetMotorLoadDataClicked = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();
        screen_9_1_Visible = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
        screen_9_1_Visible = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        looper.quit();
        randomNumber=-1;
        Intent intentB=(new Intent(Screen_8.this, Screen_4.class));
        intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentB);
    }

    public void checkSMS(String message) {
        if (message.toLowerCase().contains(SmsUtils.INSMS_13_1.toLowerCase()) && isGetFieldDataClicked) {
            b = false;
            handlerActivated=false;
            isGetFieldDataClicked = false;
            status.setText(message);
            enableViews();
        } else if (message.toLowerCase().contains(SmsUtils.INSMS_13_2.toLowerCase()) && isGetFieldDataClicked) {
            b = false;
            handlerActivated=false;
            isGetFieldDataClicked = false;
            status.setText(message);
            enableViews();
        }  else if (message.toLowerCase().contains(SmsUtils.INSMS_14_1.toLowerCase()) && isGetSensordDataClicked) {
            b = false;
            handlerActivated=false;
            isGetSensordDataClicked = false;
            status.setText(message);
            enableViews();
        }  else if (message.toLowerCase().contains(SmsUtils.INSMS_14_2.toLowerCase()) && isGetSensordDataClicked) {
            b = false;
            handlerActivated=false;
            isGetSensordDataClicked = false;
            status.setText(message);
            enableViews();
        }   else if (message.toLowerCase().contains(SmsUtils.INSMS_15_1.toLowerCase()) && isGetFiltrationDataClicked) {
            b = false;
            handlerActivated=false;
            isGetFiltrationDataClicked = false;
            status.setText(message);
            enableViews();
        }   else if (message.toLowerCase().contains(SmsUtils.INSMS_15_2.toLowerCase()) && isGetFiltrationDataClicked) {
            b = false;
            handlerActivated = false;
            isGetFiltrationDataClicked = false;
            status.setText(message);
            enableViews();
        }   else if (message.toLowerCase().contains(SmsUtils.INSMS_16_1.toLowerCase()) && isGetMotorLoadDataClicked) {
            b = false;
            handlerActivated = false;
            isGetMotorLoadDataClicked = false;
            status.setText(message);
            enableViews();
        }
    }
}