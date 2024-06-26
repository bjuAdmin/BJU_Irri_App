package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.models.FiltrationModel;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.SmsTesting;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.IOException;

public class Screen_7 extends SmsServices {
    private static final String TAG = Screen_7.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private Boolean b=false, systemDown = false;
    EditText filtrationControlUnitNoDelay_1, filtrationControlUnitNoDelay_2, filtrationControlUnitNoDelay_3;
    EditText filtrationControlUnitOnTime, filtrationControlUnitSeparation;
    private Button enableFiltration, disableFiltration, back_7;
    private TextView status;
    private FiltrationModel model;
    private CURD_Files curd_files = new CURD_FilesImpl();
    private SmsUtils smsUtils = new SmsUtils();
    private String regex = "\\d+";
    private boolean isEditedDelay_1 = false;
    private boolean isEditedDelay_2 = false;
    private boolean isEditedDelay_3 = false;
    private boolean isEditedOnTime = false;
    private boolean isEditedSeparation = false;
    private boolean isInitial = false;
    private boolean isEnabledClicked = false;
    private boolean isDisabledClicked = false;
    private double randomNumber=-1;
    private static boolean screen_7_Visible = false;
    private SmsTesting smsTesting=new SmsTesting();
    private  StringBuffer activityMessage=new StringBuffer("");
    private  boolean handlerActivated=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen7);
        this.context = getApplicationContext();
        initViews();
        initializeModel();

        filtrationControlUnitNoDelay_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrationControlUnitNoDelay_1.setCursorVisible(true);
            }
        });
        filtrationControlUnitNoDelay_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrationControlUnitNoDelay_2.setCursorVisible(true);
            }
        });
        filtrationControlUnitNoDelay_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrationControlUnitNoDelay_3.setCursorVisible(true);
            }
        });
        filtrationControlUnitOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrationControlUnitOnTime.setCursorVisible(true);
            }
        });
        filtrationControlUnitSeparation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrationControlUnitSeparation.setCursorVisible(true);
            }
        });
        enableFiltration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                cursorVisibility();
                if (validateInput() && !systemDown) {
                    status.setText("Enable filtration SMS sent\r\nWaiting for response from controller");
                    activityMessage.replace(0,activityMessage.length(),"Enable filtration SMS");
                    disableViews();
                    randomNumber = Math.random();
                    updateData_And_SendSMS("enable","Enable filtration SMS ");
                    isEnabledClicked = true;
                }
            }
        });
        disableFiltration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (!systemDown) {
                    status.setText("Disable filtration SMS sent\r\nWaiting for response from controller");
                    disableViews();
                    randomNumber = Math.random();
                    activityMessage.replace(0,activityMessage.length(),"Disable filtration SMS ");
                    updateData_And_SendSMS("disable","Disable filtration SMS ");
                    isDisabledClicked = true;
                }
            }
        });
        back_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                randomNumber=-1;
                Intent intentB=(new Intent(Screen_7.this, Screen_4.class));
                intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentB);
            }
        });
        filtrationControlUnitNoDelay_1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(filtrationControlUnitNoDelay_1.getText().toString().matches(regex) &&
                            filtrationControlUnitNoDelay_1.getText().toString().length() >= 1
                            && validateRange(1, 99, Integer.parseInt(filtrationControlUnitNoDelay_1.getText().toString())))) {

                        filtrationControlUnitNoDelay_1.getText().clear();
                        filtrationControlUnitNoDelay_1.setError("Enter value between 1 to 99");
                    }
                    if (isInitial) {
                        disableFiltration.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (filtrationControlUnitNoDelay_1.getText().toString().equals(model.getFcDelay_1() + "")) {
                            isEditedDelay_1 = false;
                            isAnyViewEdited();
                        } else {
                            isEditedDelay_1 = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        filtrationControlUnitNoDelay_2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(filtrationControlUnitNoDelay_2.getText().toString().matches(regex) &&
                            filtrationControlUnitNoDelay_2.getText().toString().length() >= 1
                            && validateRange(1, 99, Integer.parseInt(filtrationControlUnitNoDelay_2.getText().toString())))) {

                        filtrationControlUnitNoDelay_2.getText().clear();
                        filtrationControlUnitNoDelay_2.setError("Enter value between 1 to 99");

                    }
                    if (isInitial) {
                        disableFiltration.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (filtrationControlUnitNoDelay_2.getText().toString().equals(model.getFcDelay_2() + "")) {
                            isEditedDelay_2 = false;
                            isAnyViewEdited();
                        } else {
                            isEditedDelay_2 = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        filtrationControlUnitNoDelay_3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (!(filtrationControlUnitNoDelay_3.getText().toString().matches(regex) &&
                            filtrationControlUnitNoDelay_3.getText().toString().length() >= 1
                            && validateRange(1, 99, Integer.parseInt(filtrationControlUnitNoDelay_3.getText().toString())))) {

                        filtrationControlUnitNoDelay_3.getText().clear();
                        filtrationControlUnitNoDelay_3.setError("Enter value between 1 to 99");

                    }
                    if (isInitial) {
                        disableFiltration.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (filtrationControlUnitNoDelay_3.getText().toString().equals(model.getFcDelay_3() + "")) {
                            isEditedDelay_3 = false;
                            isAnyViewEdited();
                        } else {
                            isEditedDelay_3 = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        filtrationControlUnitOnTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(filtrationControlUnitOnTime.getText().toString().matches(regex) &&
                            filtrationControlUnitOnTime.getText().toString().length() >= 1
                            && validateRange(1, 99, Integer.parseInt(filtrationControlUnitOnTime.getText().toString())))) {
                        filtrationControlUnitOnTime.getText().clear();
                        filtrationControlUnitOnTime.setError("Enter value between 1 to 99");
                    }
                    if (isInitial) {
                        disableFiltration.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (filtrationControlUnitOnTime.getText().toString().equals(model.getFcOnTime() + "")) {
                            isEditedOnTime = false;
                            isAnyViewEdited();
                        } else {
                            isEditedOnTime = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        filtrationControlUnitSeparation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (!(filtrationControlUnitSeparation.getText().toString().matches(regex) &&
                            filtrationControlUnitSeparation.getText().toString().length() >= 1
                            && validateRange(1, 999, Integer.parseInt(filtrationControlUnitSeparation.getText().toString())))) {

                        filtrationControlUnitSeparation.getText().clear();
                        filtrationControlUnitSeparation.setError("Enter value between 1 to 999");
                    }
                    if (isInitial) {
                        disableFiltration.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (filtrationControlUnitSeparation.getText().toString().equals(model.getFcSeperation() + "")) {
                            isEditedSeparation = false;
                            isAnyViewEdited();
                        } else {
                            isEditedSeparation = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        filtrationControlUnitSeparation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    filtrationControlUnitSeparation.clearFocus();

                }
                return true;
            }
        });

    }

    @Override
    public void initViews() {
        filtrationControlUnitNoDelay_1 = findViewById(R.id.filtrationControlUnitNoDelay_1);
        filtrationControlUnitNoDelay_2 = findViewById(R.id.filtrationControlUnitNoDelay_2);
        filtrationControlUnitNoDelay_3 = findViewById(R.id.filtrationControlUnitNoDelay_3);
        filtrationControlUnitOnTime = findViewById(R.id.filtrationControlUnitOnTime);
        filtrationControlUnitSeparation = findViewById(R.id.filtrationControlUnitSeparation);
        enableFiltration = findViewById(R.id.enableFiltration7);
        disableFiltration = findViewById(R.id.disableFiltration7);
        back_7 = findViewById(R.id.back_7);
        status = findViewById(R.id.screen_7_status);
    }

    @Override
    public void enableViews() {
        filtrationControlUnitNoDelay_1.setEnabled(true);
        filtrationControlUnitNoDelay_2.setEnabled(true);
        filtrationControlUnitNoDelay_3.setEnabled(true);
        filtrationControlUnitOnTime.setEnabled(true);
        filtrationControlUnitSeparation.setEnabled(true);
    }

    @Override
    public void disableViews() {
        filtrationControlUnitNoDelay_1.setEnabled(false);
        filtrationControlUnitNoDelay_2.setEnabled(false);
        filtrationControlUnitNoDelay_3.setEnabled(false);
        filtrationControlUnitOnTime.setEnabled(false);
        filtrationControlUnitSeparation.setEnabled(false);
    }


    private void updateData_And_SendSMS(String typeOfAction,String screen_Specific_SMS) {
        String smsData;
        model.setFcDelay_1(Integer.parseInt(filtrationControlUnitNoDelay_1.getText().toString()));
        model.setFcDelay_2(Integer.parseInt(filtrationControlUnitNoDelay_2.getText().toString()));
        model.setFcDelay_3(Integer.parseInt(filtrationControlUnitNoDelay_3.getText().toString()));
        model.setFcOnTime(Integer.parseInt(filtrationControlUnitOnTime.getText().toString()));
        model.setFcSeperation(Integer.parseInt(filtrationControlUnitSeparation.getText().toString()));
        if (typeOfAction.equals("enable")) {
            model.setEnabled(true);
            model.setModelEmpty(false);
            smsData = smsUtils.OutSMS_8(model.getFcDelay_1() + "", model.getFcDelay_2() + ""
                    , model.getFcDelay_3() + "", model.getFcOnTime() + "",
                    model.getFcSeperation() + "");
            enableFiltration.setVisibility(View.INVISIBLE);
            disableFiltration.setVisibility(View.INVISIBLE);
            isInitial = false;
        } else {
            model.setEnabled(false);
            model.setModelEmpty(false);
            smsData = smsUtils.OutSMS_9;
            //enableFiltration.setVisibility(View.VISIBLE);
            disableFiltration.setVisibility(View.INVISIBLE);
        }
        SmsTesting.contextPri=getApplicationContext();
        smsTesting.sendMessageBox(SmsServices.phoneNumber, smsData,    randomNumber,screen_Specific_SMS);
        isEditedDelay_1 = false;
        isEditedDelay_2 = false;
        isEditedDelay_3 = false;
        isEditedSeparation = false;
        isEditedOnTime = false;
    }

    private void isAnyViewEdited() {
        if (isEditedDelay_1 || isEditedDelay_2 || isEditedDelay_3 || isEditedOnTime || isEditedSeparation) {
            enableFiltration.setVisibility(View.VISIBLE);
            disableFiltration.setVisibility(View.INVISIBLE);
        } else {
            disableFiltration.setVisibility(View.VISIBLE);
            enableFiltration.setVisibility(View.INVISIBLE);
        }
        // return (isEditedDelay_1 || isEditedDelay_2 || isEditedDelay_3 || isEditedOnTime || isEditedSeparation) ? true : false;
    }

    private boolean validateInput() {
        if (!(filtrationControlUnitNoDelay_1.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_1.getText().toString().length() >= 1
                && validateRange(1, 99, Integer.parseInt(filtrationControlUnitNoDelay_1.getText().toString())))) {

            filtrationControlUnitNoDelay_1.getText().clear();
            filtrationControlUnitNoDelay_1.setError("Enter value between 1 to 99");
            return false;
        }
        if (!(filtrationControlUnitNoDelay_2.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_2.getText().toString().length() >= 1
                && validateRange(1, 99, Integer.parseInt(filtrationControlUnitNoDelay_2.getText().toString())))) {

            filtrationControlUnitNoDelay_2.getText().clear();
            filtrationControlUnitNoDelay_2.setError("Enter value between 1 to 99");
            return false;

        }
        if (!(filtrationControlUnitNoDelay_3.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_3.getText().toString().length() >= 1
                && validateRange(1, 99, Integer.parseInt(filtrationControlUnitNoDelay_3.getText().toString())))) {

            filtrationControlUnitNoDelay_3.getText().clear();
            filtrationControlUnitNoDelay_3.setError("Enter value between 1 to 99");
            return false;

        }
        if (!(filtrationControlUnitOnTime.getText().toString().matches(regex) &&
                filtrationControlUnitOnTime.getText().toString().length() >= 1
                && validateRange(1, 99, Integer.parseInt(filtrationControlUnitOnTime.getText().toString())))) {
            filtrationControlUnitOnTime.getText().clear();
            filtrationControlUnitOnTime.setError("Enter value between 1 to 99");
            return false;
        }

        if (!(filtrationControlUnitSeparation.getText().toString().matches(regex) &&
                filtrationControlUnitSeparation.getText().toString().length() >= 1
                && validateRange(1, 999, Integer.parseInt(filtrationControlUnitSeparation.getText().toString())))) {
            filtrationControlUnitSeparation.getText().clear();
            filtrationControlUnitSeparation.setError("Enter value between 1 to 999");
            return false;
        }

        return true;
    }

    private boolean validateRange(int min, int max, int inputValue) {
        if (inputValue >= min && inputValue <= max) {
            return true;
        }
        return false;
    }


    private void initializeModel() {
        try {
            if (curd_files.isFileHasData(getApplicationContext(), ProjectUtils.CONFG_FILTRATION_FILE)) {
                model = (FiltrationModel) curd_files.getFile(getApplicationContext(), ProjectUtils.CONFG_FILTRATION_FILE);
                if (model.isEnabled() || !model.isModelEmpty()) {
                    filtrationControlUnitNoDelay_1.setText(model.getFcDelay_1() + "");
                    filtrationControlUnitNoDelay_2.setText(model.getFcDelay_2() + "");
                    filtrationControlUnitNoDelay_3.setText(model.getFcDelay_3() + "");
                    filtrationControlUnitOnTime.setText(model.getFcOnTime() + "");
                    filtrationControlUnitSeparation.setText(model.getFcSeperation() + "");
                    if (model.isEnabled()) {
                        disableFiltration.setVisibility(View.VISIBLE);
                        enableFiltration.setVisibility(View.VISIBLE);
                    } else {
                        disableFiltration.setVisibility(View.VISIBLE);
                        enableFiltration.setVisibility(View.VISIBLE);
                    }

                } else {
                    isInitial = true;
                    disableFiltration.setVisibility(View.VISIBLE);
                    enableFiltration.setVisibility(View.VISIBLE);
                }
            } else {
                model = new FiltrationModel();
                isInitial = true;
                disableFiltration.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void cursorVisibility() {

        try {
            filtrationControlUnitNoDelay_1.setCursorVisible(false);
            filtrationControlUnitNoDelay_2.setCursorVisible(false);
            filtrationControlUnitNoDelay_3.setCursorVisible(false);
            filtrationControlUnitOnTime.setCursorVisible(false);
            filtrationControlUnitSeparation.setCursorVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                 if (b && (randomNumber == randomValue) && screen_7_Visible) {

                    disableViews();
                    handlerActivated=false;
                    systemDown = true;
                    smsReceiver.unRegisterBroadCasts();
                    status.setText(SmsUtils.SYSTEM_DOWN);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            randomNumber=-1;
                            Intent intentS=(new Intent(Screen_7.this, MainActivity_GSM.class));
                            intentS.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentS);                        }
                    }, 5000);
                }
            }

        });

        smsTesting.setSmsServiceBroadcast(new SmsServiceBroadcast() {
            @Override
            public void onReceiveSmsDeliveredStatus(boolean smsDeliveredStatus, String message) {
                 if(smsDeliveredStatus){
                    if(message.equals(activityMessage.toString())&& !(handlerActivated))
                            {
                                handlerActivated=true;
                    smsReceiver.waitFor_1_Minute(randomNumber,smsReceiver);
                    b = true;
                    }
                } else {
                    status.setText(message+" sending failed");
                    isEnabledClicked = false;
                    isDisabledClicked = false;
                    enableViews();
                    initializeModel();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();
        screen_7_Visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
        screen_7_Visible = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        randomNumber=-1;
        Intent intentB=(new Intent(Screen_7.this, Screen_4.class));
        intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentB);
    }

    public void checkSMS(String message) {
        try {
            if (message.toLowerCase().contains(SmsUtils.INSMS_8_1.toLowerCase()) && isEnabledClicked) {
                b = false;
                handlerActivated=false;
                isEnabledClicked = false;
                curd_files.updateFile(getApplicationContext(), ProjectUtils.CONFG_FILTRATION_FILE, model);
                status.setText("Water filtration activated");
                enableViews();
                initializeModel();
            } else if (message.toLowerCase().contains(SmsUtils.INSMS_9_1.toLowerCase()) && isDisabledClicked) {
                b = false;
                handlerActivated=false;
                isDisabledClicked = false;
                curd_files.updateFile(getApplicationContext(), ProjectUtils.CONFG_FILTRATION_FILE, model);
                status.setText("Water filtration deactivated");
                enableViews();
                initializeModel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}