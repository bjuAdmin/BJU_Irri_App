package com.leledevelopers.smartirrigation.registration;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.leledevelopers.smartirrigation.MainActivity_GSM;
import com.leledevelopers.smartirrigation.R;
import com.leledevelopers.smartirrigation.Screen_4;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class Screen_2_1 extends SmsServices {
    private static final String TAG = Screen_2_1.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private  Handler handler = new Handler();
    private EditText oldPassword, newPassword;
    private TextView status;
    private Button gsmContact, set;
    private Boolean b, systemDown = false;
    SmsUtils smsUtils = new SmsUtils();
    private boolean isSetClicked = false, isGSMSelected = false, isPasswordSaved = false;
    private CheckBox checkbox1, checkbox2;
    private String smsData;
    private Intent intent;
    private Bundle bundle;
    private Boolean extra = false;
    private double randomNumber=-1;
    private static boolean scrren_2_1_Visible = false;

    private  StringBuffer activityMessage=new StringBuffer("");
    private  boolean handlerActivated=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2_1);

        try {
            intent = getIntent();
            bundle = intent.getExtras();
            if (bundle != null)
                extra = bundle.getBoolean("Settings");
        } catch (Exception e) {
            e.printStackTrace();
        }
        initViews();
        this.context = getApplicationContext();
        gsmContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    isGSMSelected = false;
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent, ProjectUtils.PICK_CONTACT);
                }
            }
        });
        if (!externalStorageIsAvailableForRW()) {
            set.setEnabled(false);
        }
        oldPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword.setCursorVisible(true);
            }
        });
        newPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword.setCursorVisible(true);
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("");

                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    oldPassword.clearFocus();
                    newPassword.clearFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isGSMSelected) {
                    if (!systemDown) {

                        if (validateInput(oldPassword.getText().toString(), newPassword.getText().toString())) {
                            randomNumber = Math.random();
                            //smsReceiver.waitFor_1_Minute(randomNumber);
                            //b = true;
                            disableViews();
                            isSetClicked = true;
                            cursorVisibility();
                            activityMessage.replace(0,activityMessage.length(),"Admin registration SMS ");
                            smsData = smsUtils.OutSMS_1(oldPassword.getText().toString(), newPassword.getText().toString());
                            sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber,"Admin registration SMS ");
                            status.setText("Admin registration SMS sent\r\nWaiting for response from controller");
                        } else {
                            focus(oldPassword.getText().toString(), newPassword.getText().toString());
                        }
                    }
                } else {
                    Toast.makeText(Screen_2_1.this, "Please select the GSM Sim No.!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    oldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        newPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validate(newPassword.getText().toString(), "Newpassword");

                }
            }
        });
        oldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validate(oldPassword.getText().toString(), "Oldpassword");

                }
            }
        });
        newPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return true;
            }
        });
        newPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    status.setText("Status");

                    try {

                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    newPassword.clearFocus();
                }
                return true;
            }
        });

    }


    private boolean validateInput(String oldPasswordlocal, String newPasswordlocal) {
        boolean matching = false;
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        if (p.matcher(oldPasswordlocal).matches() && p.matcher(newPasswordlocal).matches()
                && oldPasswordlocal.length() == 6 && newPasswordlocal.length() == 6 && !(oldPasswordlocal.equals(newPasswordlocal))) {
            matching = true;
        } else {
            status.setText("Status");
        }
        return matching;
    }

    private void validate(String input, String editTextField) {
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        if (!(p.matcher(input).matches() && input.length() == 6)) {
            switchFocus(editTextField);
        } else {
            status.setText("Status");
        }

    }

    private void switchFocus(String editTextField) {
        switch (editTextField) {
            case "Oldpassword": {

                oldPassword.getText().clear();
                oldPassword.setError("Enter valid 6 digit password");
                status.setText("Enter valid Old/Factory password");
            }
            break;
            case "Newpassword": {

                newPassword.getText().clear();
                newPassword.setError("Enter valid 6 digit password");
                status.setText("Enter valid new password");
            }
            break;
            case "Same": {
                status.setText("Both Passwords cannot be same");
            }
            break;
        }
    }

    private void focus(String oldPasswordlocal, String newPasswordlocal) {
        String regex = "[0-9]+";

        if (oldPasswordlocal.length() != 6 && !(oldPasswordlocal.matches(regex))) {
            oldPassword.getText().clear();
            oldPassword.setError("Enter valid 6 digit password");
            status.setText("Enter valid Old/Factory password");
        }
        if (newPasswordlocal.length() != 6 && !(newPasswordlocal.matches(regex))) {
            newPassword.getText().clear();
            newPassword.setError("Enter valid 6 digit password");
            status.setText("Enter valid new password");
        }
        if (oldPasswordlocal.equals(newPasswordlocal)) {

            status.setText("Both Passwords cannot be same");
        }
    }

    private boolean externalStorageIsAvailableForRW() {
        String extStorageState = Environment.getExternalStorageState();
        if (extStorageState.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }


    @Override
    public void initViews() {
        gsmContact = findViewById(R.id.screen_2_1_button_1);
        oldPassword = findViewById(R.id.screen_2_1_edittext_1);
        newPassword = findViewById(R.id.screen_2_1_edittext_2);
        set = findViewById(R.id.screen_2_1_button_2);
        status = findViewById(R.id.screen_2_1_status);
        checkbox1 = findViewById(R.id.checkbox1);
        checkbox2 = findViewById(R.id.checkbox2);

        File file = new File(Screen_2_1.this.getExternalFilesDir(null) + ProjectUtils.FILE_PATH);
        if (file.exists()) {

            gsmContact.setText(SmsServices.phoneNumber);
        }
    }

    @Override
    public void enableViews() {
        gsmContact.setEnabled(true);
        newPassword.setEnabled(true);
        oldPassword.setEnabled(true);
        set.setEnabled(true);
    }

    @Override
    public void disableViews() {
        gsmContact.setEnabled(false);
        newPassword.setEnabled(false);
        oldPassword.setEnabled(false);
        set.setEnabled(false);
    }

    private void cursorVisibility() {
        try {
            oldPassword.setCursorVisible(false);
            newPassword.setCursorVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            Cursor phone = getContentResolver().query(contactData, null, null, null, null);
            if (phone.moveToFirst()) {
                String contactNumberName = phone.getString(phone.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                SmsServices.phoneNumber = contactNumber;
                gsmContact.setText(contactNumberName + " - " + contactNumber);
                isGSMSelected = true;
            }
        }
    }

    public void checkSMS(String message) {
        try {
            if (message.toLowerCase().contains(SmsUtils.INSMS_1_1.toLowerCase())) {
                enableViews();
                b = false;
                handlerActivated=false;
                saveFileDetails();
                createConfgFiles();
                status.setText("Admin set successfully");
                  handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isPasswordSaved = true;
                        Intent intent = (new Intent(Screen_2_1.this, MainActivity_GSM.class));
                        intent.putExtra("newUser", true);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            } else if (message.toLowerCase().contains(SmsUtils.INSMS_1_2.toLowerCase())) {
                enableViews();
                b = false;
                handlerActivated=false;
                status.setText("Wrong password entered");
                oldPassword.requestFocus();
            } else if (message.toLowerCase().contains(SmsUtils.INSMS_3_1.toLowerCase())) {
                enableViews();
                b = false;
                handlerActivated=false;
                status.setText("Password changed successfully");
                isPasswordSaved = true;
                saveFileDetails();
                createConfgFiles();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Screen_2_1.this, MainActivity_GSM.class));
                        finish();
                    }
                }, 1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!SmsServices.phoneNumber.equals("")) {
            isGSMSelected = true;
        }
        smsReceiver.setContext(getApplicationContext());
        smsReceiver.startBroadcastReceiver();
        smsReceiver.setSmsMessageBroadcast(new SmsReceiver.SmsReceiverBroadcast() {
            @Override
            public void onReceiveSms(String phoneNumber, String message) {
                if (SmsServices.phoneNumber.replaceAll("\\s", "").equals(phoneNumber.replaceAll("\\s", "")) && isSetClicked && !systemDown) {
                    checkSMS(message);
                } else if (phoneNumber.contains(SmsServices.phoneNumber.replaceAll("\\s", "")) && isSetClicked && !systemDown) {
                    checkSMS(message);
                }
            }

            @Override
            public void checkTime(double randomValue) {
                if (b && (randomNumber == randomValue) && scrren_2_1_Visible) {
                    systemDown = true;
                    disableViews();
                    handlerActivated=false;
                    randomNumber=-1;
                    smsReceiver.unRegisterBroadCasts();
                    status.setText(SmsUtils.SYSTEM_DOWN);
                }
            }

        });

        this.setSmsServiceBroadcast(new SmsServiceBroadcast() {
            @Override
            public void onReceiveSmsDeliveredStatus(boolean smsDeliveredStatus, String message) {
                System.out.println("non service page smsDeliveredStatus - " + smsDeliveredStatus);
                if (smsDeliveredStatus) {
                    if(message.equals(activityMessage.toString()) && !(handlerActivated)){
                       // status.setText(message+ " sent");
                        smsReceiver.waitFor_1_Minute(randomNumber,smsReceiver);
                        b = true;
                    }

                } else {
                    status.setText(message+" sending failed");
                    enableViews();
                    isSetClicked = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();
        scrren_2_1_Visible = true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
        scrren_2_1_Visible = false;
    }

    private void createConfgFiles() throws IOException {
        CURD_Files curd_files = new CURD_FilesImpl();
        if (!curd_files.isFileExists(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_IRRIGATION_NAME)) {
            curd_files.createEmptyFile(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_IRRIGATION_NAME);
        }
        if (!curd_files.isFileExists(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_FERTIGATION_NAME)) {
            curd_files.createEmptyFile(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_FERTIGATION_NAME);
        }
        if (!curd_files.isFileExists(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_FILTRATION_NAME)) {
            curd_files.createEmptyFile(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_FILTRATION_NAME);
        }
        if (!curd_files.isFileExists(getApplicationContext(), ProjectUtils.DIRECTORY_PATH, ProjectUtils.MESSAGES_FILE)) {
            curd_files.createEmptyFile(getApplicationContext(), ProjectUtils.DIRECTORY_PATH, ProjectUtils.MESSAGES_FILE);
        }
    }

    private void saveFileDetails() {
        if (!SmsServices.phoneNumber.equals("")) {
            File myExternalFile = new File(getExternalFilesDir(ProjectUtils.DIRECTORY_PATH), ProjectUtils.FILE_NAME);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(myExternalFile);
                String data = SmsServices.phoneNumber;
                fos.write(data.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(Screen_2_1.this, "PLease select proper GSM number", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isPasswordSaved) {
            // SmsServices.phoneNumber = "";
            isSetClicked = false;
            isGSMSelected = false;
            isPasswordSaved = false;
            randomNumber=-1;
        }

        try {
            if (extra) {
                startActivity(new Intent(Screen_2_1.this, Screen_4.class));
                finish();
            } else {
                SmsServices.phoneNumber = "";
                startActivity(new Intent(Screen_2_1.this, Screen_1.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
     }
}
