package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

public class Screen_10 extends SmsServices {
    private static final String TAG = Screen_10.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private SmsUtils smsUtils = new SmsUtils();
    private Boolean b=false, systemDown = false;
    EditText noLoadCutoffText, fullLoadCutOffText;
    ArrayAdapter<CharSequence> adapter1;
    private Spinner fieldSpinner1;
    private Button setMotorLoadThreshold, setMotorLoadThreshold2, back_10;
    private TextView status;
    private Handler handler = new Handler();
    private double randomNumber=-1;
    private boolean isSetMotorLoadClicked = false;
    private boolean isSetMotorLoad2Clicked = false;
    private static boolean screen_10_Visible = false;

    private  StringBuffer
            activityMessage=new StringBuffer("");
    private  boolean handlerActivated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen10);
        this.context = getApplicationContext();
        initViews();
        adapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpinner1.setAdapter(adapter1);

        setMotorLoadThreshold2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                randomNumber = Math.random();
                activityMessage.replace(0,activityMessage.length(),"Auto Motorload cut-off settings SMS ");
                isSetMotorLoad2Clicked = true;
                int fieldNo = Integer.parseInt(fieldSpinner1.getSelectedItem().toString());
                String smsData = smsUtils.OutSMS_12_2((fieldNo < 10 ? String.format("%02d", fieldNo) : fieldNo + ""));
                sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber,"Auto Motorload cut-off settings SMS ");
                status.setText("Auto Motorload cut-off settings SMS sent\r\nWaiting for response from controller");
            }
        });
        setMotorLoadThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursorVisibility();
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                 if (validateInput(noLoadCutoffText.getText().toString(), fullLoadCutOffText.getText().toString()) && !systemDown) {
                    disableViews();
                    status.setText("Motorload cut-off setting SMS sent\r\nWaiting for response from controller");
                    randomNumber = Math.random();
                    activityMessage.replace(0,activityMessage.length(),"Motorload cut-off setting SMS ");
                    String smsData = smsUtils.OutSMS_12_1(noLoadCutoffText.getText().toString(),fullLoadCutOffText.getText().toString());
                    sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber, "Motorload cut-off setting SMS ");
                    isSetMotorLoadClicked = true;
                }
            }
        });
        back_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                randomNumber=-1;
                startActivity(new Intent(Screen_10.this, Screen_9.class));
                finish();
            }
        });
        noLoadCutoffText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noLoadCutoffText.setCursorVisible(true);
            }
        });
        fullLoadCutOffText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullLoadCutOffText.setCursorVisible(true);
            }
        });
        noLoadCutoffText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (noLoadCutoffText.getText().toString().length() == 0 ||
                            !(validateRange(0, 1024, Integer.parseInt(noLoadCutoffText.getText().toString())))) {
                        noLoadCutoffText.getText().clear();
                        noLoadCutoffText.setError("Enter a valid value");

                    }
                }
            }
        });
        fullLoadCutOffText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                     if (fullLoadCutOffText.getText().toString().length() == 0 ||
                            !(validateRange(0, 1024, Integer.parseInt(fullLoadCutOffText.getText().toString())))) {

                        fullLoadCutOffText.getText().clear();
                        fullLoadCutOffText.setError("Enter a valid value");
                    }
                }
            }
        });
        fullLoadCutOffText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fullLoadCutOffText.clearFocus();
                }
                return true;
            }
        });
    }


    private boolean validateInput(String noLoadCutoffTextlocal, String fullLoadCutOffTextlocal) {
         boolean validate = true;
        try {
            if (noLoadCutoffTextlocal.equals("") || !(validateRange(0, 1024, Integer.parseInt(noLoadCutoffTextlocal)))) {
                 noLoadCutoffText.getText().clear();
                noLoadCutoffText.setError("Enter a valid value");
                validate = false;
            }
            if (fullLoadCutOffTextlocal.equals("") || !(validateRange(0, 1024, Integer.parseInt(fullLoadCutOffTextlocal)))) {
                 fullLoadCutOffText.getText().clear();
                fullLoadCutOffText.setError("Enter a valid value");
                validate = false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return validate;
    }

    private boolean validateRange(int min, int max, int inputValue) {
        if (inputValue >= min && inputValue <= max) {
            return true;
        }
        return false;
    }

    private void cursorVisibility() {
        try {
            noLoadCutoffText.setCursorVisible(false);
            fullLoadCutOffText.setCursorVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initViews() {
        noLoadCutoffText = findViewById(R.id.noLoadCutoffText);
        fullLoadCutOffText = findViewById(R.id.fullLoadCutOffText);
        setMotorLoadThreshold = findViewById(R.id.setMotorLoadThreshold);
        setMotorLoadThreshold2 = findViewById(R.id.setMotorLoadThreshold2);
        fieldSpinner1 = findViewById(R.id.fieldSpinner10);
        status = findViewById(R.id.screen_10_status);
        back_10 = findViewById(R.id.back_10);
    }

    @Override
    public void enableViews() {
        noLoadCutoffText.setFocusableInTouchMode(true);
        fullLoadCutOffText.setFocusableInTouchMode(true);
        //disable views
        noLoadCutoffText.setEnabled(true);
        fullLoadCutOffText.setEnabled(true);
        fieldSpinner1.setEnabled(true);
        //setMotorLoadThreshold.setEnabled(true);
        //setMotorLoadThreshold2.setEnabled(true);
        setMotorLoadThreshold.setVisibility(View.VISIBLE);
        setMotorLoadThreshold2.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableViews() {
        noLoadCutoffText.setFocusableInTouchMode(false);
        fullLoadCutOffText.setFocusableInTouchMode(false);

        //disable views
        noLoadCutoffText.setEnabled(false);
        fullLoadCutOffText.setEnabled(false);
        fieldSpinner1.setEnabled(false);
        //setMotorLoadThreshold.setEnabled(false);
        //setMotorLoadThreshold2.setEnabled(false);
        setMotorLoadThreshold.setVisibility(View.INVISIBLE);
        setMotorLoadThreshold2.setVisibility(View.INVISIBLE);
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
                 if (b && (randomNumber == randomValue) && screen_10_Visible) {
                     systemDown = true;
                    disableViews();
                    handlerActivated=false;
                    smsReceiver.unRegisterBroadCasts();
                    status.setText(SmsUtils.SYSTEM_DOWN);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            randomNumber=-1;
                            Intent intent=(new Intent(Screen_10.this, MainActivity_GSM.class));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }, 5000);
                }
            }
        });

        this.setSmsServiceBroadcast(new SmsServiceBroadcast() {
            @Override
            public void onReceiveSmsDeliveredStatus(boolean smsDeliveredStatus, String message) {
                 if (smsDeliveredStatus) {
                    if(message.equals(activityMessage.toString()) && !(handlerActivated))

                    {
                    handlerActivated=true;
                    smsReceiver.waitFor_1_Minute(randomNumber,smsReceiver);
                    b = true;
                    }
                } else {
                    status.setText(message+" sending failed");
                    enableViews();
                    isSetMotorLoadClicked = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();
        screen_10_Visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
        screen_10_Visible = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        randomNumber=-1;
        startActivity(new Intent(Screen_10.this, Screen_9.class));
        finish();
    }

    public void checkSMS(String message) {
        if (message.toLowerCase().contains(SmsUtils.INSMS_16_2.toLowerCase()) && isSetMotorLoad2Clicked) {
            b = false;
            handlerActivated=false;
            isSetMotorLoad2Clicked = false;
            status.setText(message);
            enableViews();
        } else
        if (message.toLowerCase().contains(SmsUtils.INSMS_16_1.toLowerCase()) && isSetMotorLoad2Clicked) {
            b = false;
            handlerActivated=false;
            isSetMotorLoad2Clicked = false;
            status.setText(message);
            enableViews();
        } else if (message.toLowerCase().contains(SmsUtils.INSMS_12_1.toLowerCase()) && isSetMotorLoadClicked) {
            b = false;
            handlerActivated=false;
            enableViews();
            isSetMotorLoadClicked = false;
            status.setText(message);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Screen_10.this, Screen_9.class));
                    finish();
                }
            }, 5000);
        }
    }
}