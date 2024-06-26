package com.leledevelopers.smartirrigation;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.leledevelopers.smartirrigation.models.BaseConfigureFieldIrrigationModel;
import com.leledevelopers.smartirrigation.models.ConfigureFieldIrrigationModel;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Screen_5 extends SmsServices {
    private static final String TAG = Screen_5.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    ArrayAdapter<CharSequence> fieldNoArray, startFrom;
    private Spinner fieldArraySpinner, startFromSpinner;
    private int hour, min;
    EditText valveOnPeriod, valveOffPeriod, soilDryness, soilWetness, priority, cycles;
    private TextView status, motorOnTime;
    private Button enableFertigation, disableFertigation, back_5;
    private Boolean b = false, systemDown = false;
    private ConfigureFieldIrrigationModel model;
    private CURD_Files<ConfigureFieldIrrigationModel> curd_files = new CURD_FilesImpl<ConfigureFieldIrrigationModel>();
    private List<ConfigureFieldIrrigationModel> modelList = new ArrayList<ConfigureFieldIrrigationModel>();
    private BaseConfigureFieldIrrigationModel baseConfigureFieldIrrigationModel = new BaseConfigureFieldIrrigationModel();
    private SmsUtils smsUtils = new SmsUtils();
    private String regex = "\\d+";
    private int fieldNo;
    private boolean isEditedValveOnPeriod = false;
    private boolean isEditedValveOffPeriod = false;
    private boolean isEditedSoilDryness = false;
    private boolean isEditedSoilWetness = false;
    private boolean isEditedMotorOnTime = false;
    private boolean isEditedPriority = false;
    private boolean isEditedCycles = false;
    private boolean isEditedStartFrom = false;
    private boolean isInitial = false;
    private boolean isEnabledClicked = false;
    private boolean isDisabledClicked = false;
    private TimePickerDialog timePickerDialog;
    private double randomNumber = -1;
    private static boolean screen_5_Visible = false;
    private Timer timer;
    private int counter = 0;
    private boolean handlerActivated = false;
    private StringBuffer activityMessage = new StringBuffer("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen5);
        this.context = getApplicationContext();
        initViews();
        fieldNoArray = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        fieldNoArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldArraySpinner.setAdapter(fieldNoArray);
        startFrom = ArrayAdapter.createFromResource(getApplicationContext(), R.array.startfrom, android.R.layout.simple_spinner_dropdown_item);
        startFromSpinner.setAdapter(startFrom);
        initializeModel();
        fieldArraySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (modelList.get(position).isEnabled() || !modelList.get(position).isModelEmpty()) {
                    model = modelList.get(position);
                    valveOnPeriod.setText(model.getValveOnPeriod() + "");
                    valveOffPeriod.setText(model.getValveOffPeriod() + "");
                    soilDryness.setText(model.getSoilDryness() + "");
                    soilWetness.setText(model.getSoilWetness() + "");
                    motorOnTime.setText(model.getMotorOnTime());
                    priority.setText(model.getPriority() + "");
                    cycles.setText(model.getCycle() + "");
                    startFromSpinner.setSelection(spinnerIntValue(model.getStartFrom()));
                    getHoursAndMinutes(model.getMotorOnTime());

                    if (model.isEnabled()) {
                        disableFertigation.setVisibility(View.VISIBLE);
                        enableFertigation.setVisibility(View.VISIBLE);
                    } else {
                        disableFertigation.setVisibility(View.VISIBLE);
                        enableFertigation.setVisibility(View.VISIBLE);
                    }
                    isInitial = false;
                } else {
                    isInitial = true;
                    setEmptyData();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startFromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInitial) {
                    disableFertigation.setVisibility(View.VISIBLE);
                } else if (model.isEnabled()) {
                    if ((position + "").equals(spinnerIntValue(model.getStartFrom()) + "")) {
                        isEditedStartFrom= false;
                        isAnyViewEdited();
                    } else {
                        isEditedStartFrom = true;
                        isAnyViewEdited();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        valveOnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valveOnPeriod.setCursorVisible(true);
            }
        });

        valveOffPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valveOffPeriod.setCursorVisible(true);
            }
        });

        soilDryness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soilDryness.setCursorVisible(true);
            }
        });

        soilWetness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soilWetness.setCursorVisible(true);
            }
        });

        priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority.setCursorVisible(true);
            }
        });

        cycles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycles.setCursorVisible(true);
            }
        });


        back_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                Intent intentB = (new Intent(Screen_5.this, Screen_4.class));
                intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentB);

            }
        });


        motorOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                motorOnTime.clearFocus();
                motorOnTime.setError(null);
                timePickerDialog = new TimePickerDialog(Screen_5.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hour = hourOfDay;
                                min = minute;
                                Calendar cal = Calendar.getInstance();
                                cal.set(0, 0, 0, hour, min);
                                motorOnTime.setText(DateFormat.format("HH:mm", cal));


                                if (isInitial) {
                                    disableFertigation.setVisibility(View.VISIBLE);
                                } else if (motorOnTime.getText().toString().equals(model.getMotorOnTime() + "")) {
                                    isEditedMotorOnTime = false;
                                    isAnyViewEdited();
                                } else {
                                    isEditedMotorOnTime = true;
                                    isAnyViewEdited();
                                }
                            }
                        }, 24, 0, true);

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour, min);
                timePickerDialog.show();
            }
        });


        //enable fertigation
        enableFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    valveOnPeriod.clearFocus();
                    valveOffPeriod.clearFocus();
                    soilDryness.clearFocus();
                    soilWetness.clearFocus();
                    priority.clearFocus();
                    cycles.clearFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (validateInput() && !systemDown) {
                    disableViews();
                    cursorVisibility();
                    status.setText("Enable Irrigation configuration SMS Sent\r\nWaiting for response from controller");
                    activityMessage.replace(0, activityMessage.length(), "Enable Irrigation configuration SMS ");
                    updateData_And_SendSMS("enable", "Enable Irrigation configuration SMS ");
                    isEnabledClicked = true;
                }
            }
        });

        //disable fertigation
        disableFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (!systemDown) {
                    disableViews();
                    status.setText("Disable Irrigation configuration SMS Sent\r\nWaiting for response from controller");
                    randomNumber = Math.random();
                    activityMessage.replace(0, activityMessage.length(), "Disable Irrigation configuration SMS ");
                    updateData_And_SendSMS("disable", "Disable Irrigation configuration SMS ");
                    isDisabledClicked = true;
                }
            }
        });

        valveOnPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(valveOnPeriod.getText().toString().matches(regex)
                            && valveOnPeriod.getText().toString().length() >= 1 &&
                            validateRange(1, 999, Integer.parseInt(valveOnPeriod.getText().toString())))) {
                        valveOnPeriod.getText().clear();
                        valveOnPeriod.setError("Enter value between 1 to 999");
                    }
                    if (isInitial) {
                        disableFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (valveOnPeriod.getText().toString().equals(model.getValveOnPeriod() + "")) {
                            isEditedValveOnPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedValveOnPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        valveOffPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(valveOffPeriod.getText().toString().matches(regex)
                            && valveOffPeriod.getText().toString().length() >= 1 &&
                            validateRange(1, 99, Integer.parseInt(valveOffPeriod.getText().toString())))) {
                        valveOffPeriod.getText().clear();
                        valveOffPeriod.setError("Enter value between 1 to 99");

                    }
                    if (isInitial) {
                        disableFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (valveOffPeriod.getText().toString().equals(model.getValveOffPeriod() + "")) {
                            isEditedValveOffPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedValveOffPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });

        soilDryness.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(soilDryness.getText().toString().matches(regex)
                            && soilDryness.getText().toString().length() >= 1 &&
                            validateRange(0, 999, Integer.parseInt(soilDryness.getText().toString())))) {
                        soilDryness.getText().clear();
                        soilDryness.setError("Enter value between 0 to 999");

                    }
                    if (isInitial) {
                        disableFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (soilDryness.getText().toString().equals(model.getSoilDryness() + "")) {
                            isEditedSoilDryness = false;
                            isAnyViewEdited();
                        } else {
                            isEditedSoilDryness = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        soilWetness.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(soilWetness.getText().toString().matches(regex)
                            && soilWetness.getText().toString().length() >= 1 &&
                            validateRange(0, 999, Integer.parseInt(soilWetness.getText().toString())))) {
                        soilWetness.setError("Enter value between 0 to 999");
                        soilWetness.getText().clear();

                    }
                    if (isInitial) {
                        disableFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (soilWetness.getText().toString().equals(model.getSoilWetness() + "")) {
                            isEditedSoilWetness = false;
                            isAnyViewEdited();
                        } else {
                            isEditedSoilWetness = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        priority.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(priority.getText().toString().matches(regex) &&
                            priority.getText().toString().length() >= 1 &&
                            validateRange(1, 12, Integer.parseInt(priority.getText().toString())))) {
                        priority.getText().clear();
                        priority.setError("Enter value between 1 to 12");
                    }
                    if (isInitial) {
                        disableFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (priority.getText().toString().equals(model.getPriority() + "")) {
                            isEditedPriority = false;
                            isAnyViewEdited();
                        } else {
                            isEditedPriority = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        cycles.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(cycles.getText().toString().matches(regex) &&
                            cycles.getText().toString().length() >= 1 &&
                            validateRange(1, 99, Integer.parseInt(cycles.getText().toString())))) {
                        cycles.getText().clear();
                        cycles.setError("Enter value between 1 to 99");

                    }
                    if (isInitial) {
                        disableFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (cycles.getText().toString().equals(model.getCycle() + "")) {
                            isEditedCycles = false;
                            isAnyViewEdited();
                        } else {
                            isEditedCycles = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        cycles.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    try {

                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception

                    }
                    cycles.clearFocus();
                    startFromSpinner.performClick();
                }
                return true;
            }
        });
        soilWetness.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    soilWetness.clearFocus();
                    motorOnTime.performClick();
                }
                return true;
            }
        });


    }

    private boolean validateInput() {

        if (fieldArraySpinner.getSelectedItem().toString().trim().equals("Pick one")) {

            return false;
        }
        if (!(valveOnPeriod.getText().toString().matches(regex)
                && valveOnPeriod.getText().toString().length() >= 1 &&
                validateRange(1, 999, Integer.parseInt(valveOnPeriod.getText().toString())))) {
            valveOnPeriod.getText().clear();
            valveOnPeriod.setError("Enter value between 1 to 999");
            return false;
        }

        if (!(valveOffPeriod.getText().toString().matches(regex)
                && valveOffPeriod.getText().toString().length() >= 1 &&
                validateRange(1, 99, Integer.parseInt(valveOffPeriod.getText().toString())))) {
            valveOffPeriod.getText().clear();
            valveOffPeriod.setError("Enter value between 1 to 99");
            return false;

        }
        if (!(soilDryness.getText().toString().matches(regex)
                && soilDryness.getText().toString().length() >= 1 &&
                validateRange(0, 999, Integer.parseInt(soilDryness.getText().toString())))) {
            soilDryness.getText().clear();
            soilDryness.setError("Enter value between 0 to 999");
            return false;

        }
        if (!(soilWetness.getText().toString().matches(regex)
                && soilWetness.getText().toString().length() >= 1 &&
                validateRange(0, 999, Integer.parseInt(soilWetness.getText().toString())))) {
            soilWetness.getText().clear();
            soilWetness.setError("Enter value between 0 to 999");
            return false;

        }

        if (motorOnTime.getText().toString() == "")  /// motor time validation is to be checked
        {
            motorOnTime.setError("Enter a valid Time");
            return false;
        }
        if (!(priority.getText().toString().matches(regex) &&
                priority.getText().toString().length() >= 1 &&
                validateRange(1, 12, Integer.parseInt(priority.getText().toString())))) {
            priority.getText().clear();
            priority.setError("Enter value between 1 to 12");

            return false;

        }

        if (!(cycles.getText().toString().matches(regex) &&
                cycles.getText().toString().length() >= 1 &&
                validateRange(1, 99, Integer.parseInt(cycles.getText().toString())))) {
            cycles.getText().clear();
            cycles.setError("Enter value between 1 to 99");
            return false;

        }

        return true;
    }

    @Override
    public void initViews() {
        fieldArraySpinner = (Spinner) findViewById(R.id.fieldNoSpinner5);
        valveOnPeriod = findViewById(R.id.valveOnPeriod);
        valveOffPeriod = findViewById(R.id.valveOffPeriod);
        soilDryness = findViewById(R.id.soilDryness);
        soilWetness = findViewById(R.id.soilWetness);
        motorOnTime = findViewById(R.id.motorOnTime);
        priority = findViewById(R.id.priority);
        cycles = findViewById(R.id.cycles);
        startFromSpinner = findViewById(R.id.startFrom);
        enableFertigation = findViewById(R.id.enableFieldIrrigation5);
        disableFertigation = findViewById(R.id.disableIrrigation5);
        back_5 = findViewById(R.id.back_5);
        status = findViewById(R.id.screen_5_status);
    }

    @Override
    public void enableViews() {
        fieldArraySpinner.setEnabled(true);
        valveOnPeriod.setEnabled(true);
        valveOffPeriod.setEnabled(true);
        soilDryness.setEnabled(true);
        soilWetness.setEnabled(true);
        motorOnTime.setEnabled(true);
        priority.setEnabled(true);
        cycles.setEnabled(true);
        startFromSpinner.setEnabled(true);
    }

    @Override
    public void disableViews() {
        fieldArraySpinner.setEnabled(false);
        valveOnPeriod.setEnabled(false);
        valveOffPeriod.setEnabled(false);
        soilDryness.setEnabled(false);
        soilWetness.setEnabled(false);
        motorOnTime.setEnabled(false);
        priority.setEnabled(false);
        cycles.setEnabled(false);
        startFromSpinner.setEnabled(false);
    }

    private void cursorVisibility() {
        valveOnPeriod.setCursorVisible(false);
        valveOffPeriod.setCursorVisible(false);
        soilDryness.setCursorVisible(false);
        soilWetness.setCursorVisible(false);
        priority.setCursorVisible(false);
        cycles.setCursorVisible(false);

    }


    private void setEmptyData() {
        disableFertigation.setVisibility(View.VISIBLE);
        enableFertigation.setVisibility(View.VISIBLE);
        valveOnPeriod.setText("");
        valveOffPeriod.setText("");
        soilDryness.setText("");
        soilWetness.setText("");
        motorOnTime.setText("");
        priority.setText("");
        cycles.setText("");
        model = null;
    }

    @Override
    public String toString() {
        return "Screen_5{" +
                "isEditedValveOnPeriod=" + isEditedValveOnPeriod +
                ", isEditedValveOffPeriod=" + isEditedValveOffPeriod +
                ", isEditedSoilDryness=" + isEditedSoilDryness +
                ", isEditedSoilWetness=" + isEditedSoilWetness +
                ", isEditedMotorOnTime=" + isEditedMotorOnTime +
                ", isEditedPriority=" + isEditedPriority +
                ", isEditedCycles=" + isEditedCycles +
                ", isEditedStartFrom=" + isEditedStartFrom +
                ", isInitial=" + isInitial +
                '}';
    }

    private void isAnyViewEdited() {
        if (isEditedValveOnPeriod || isEditedValveOffPeriod || isEditedCycles || isEditedStartFrom || isEditedPriority
                || isEditedSoilDryness || isEditedSoilWetness || isEditedMotorOnTime) {
            enableFertigation.setVisibility(View.VISIBLE);
            disableFertigation.setVisibility(View.INVISIBLE);
        } else {
            disableFertigation.setVisibility(View.VISIBLE);
            enableFertigation.setVisibility(View.INVISIBLE);

        }
    }


    private boolean validateRange(int min, int max, int inputValue) {
        if (inputValue >= min && inputValue <= max) {
            return true;
        }
        return false;
    }


    private void initializeModel() {
        try {
            if (curd_files.isFileHasData(getApplicationContext(), ProjectUtils.CONFG_IRRIGATION_FILE)) {
                baseConfigureFieldIrrigationModel = (BaseConfigureFieldIrrigationModel) curd_files.getFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE);
                modelList = baseConfigureFieldIrrigationModel.getModelList();
                if (baseConfigureFieldIrrigationModel.getLastEnabledFieldNo() != -1) {
                    model = modelList.get(baseConfigureFieldIrrigationModel.getLastEnabledFieldNo());
                    if (model.isEnabled() || !model.isModelEmpty()) {
                        fieldArraySpinner.setSelection(model.getFieldNo() - 1);
                        valveOnPeriod.setText(model.getValveOnPeriod() + "");
                        valveOffPeriod.setText(model.getValveOffPeriod() + "");
                        soilDryness.setText(model.getSoilDryness() + "");
                        soilWetness.setText(model.getSoilWetness() + "");
                        motorOnTime.setText(model.getMotorOnTime());
                        priority.setText(model.getPriority() + "");
                        cycles.setText(model.getCycle() + "");
                        startFromSpinner.setSelection(spinnerIntValue(model.getStartFrom()));
                        getHoursAndMinutes(model.getMotorOnTime());
                        if (model.isEnabled()) {
                            disableFertigation.setVisibility(View.VISIBLE);
                            enableFertigation.setVisibility(View.VISIBLE);
                        } else {
                            disableFertigation.setVisibility(View.VISIBLE);
                            enableFertigation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        isInitial = true;
                        disableFertigation.setVisibility(View.VISIBLE);
                        enableFertigation.setVisibility(View.VISIBLE);
                    }
                } else {
                    isInitial = true;
                    disableFertigation.setVisibility(View.VISIBLE);
                    enableFertigation.setVisibility(View.VISIBLE);
                }
            } else {
                for (int i = 0; i < 12; i++) {
                    ConfigureFieldIrrigationModel modelData = new ConfigureFieldIrrigationModel();
                    modelData.setFieldNo(i);
                    modelData.setEnabled(false);
                    modelList.add(modelData);
                    baseConfigureFieldIrrigationModel.setModelList(modelList);
                }
                curd_files.createFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE, baseConfigureFieldIrrigationModel);
                isInitial = true;
                setEmptyData();
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void getHoursAndMinutes(String motorOnTime) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dateFormatter.parse(motorOnTime));
            String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            String mintue = String.valueOf(c.get(Calendar.MINUTE));
            model.setMotorOnTimeHr(Integer.parseInt(hour));
            model.setMotorOnTimeMins(Integer.parseInt(mintue));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateData_And_SendSMS(String typeOfAction, String screen_Specific_SMS) {
        if (!fieldArraySpinner.getSelectedItem().toString().trim().equals("Pick one")) {
            String smsdata;
            fieldNo = Integer.parseInt(fieldArraySpinner.getSelectedItem().toString());
            model = modelList.get(fieldNo - 1);
            model.setFieldNo(Integer.parseInt(fieldArraySpinner.getSelectedItem().toString()));
            model.setValveOnPeriod(Integer.parseInt(valveOnPeriod.getText().toString()));
            model.setValveOffPeriod(Integer.parseInt(valveOffPeriod.getText().toString()));
            model.setSoilDryness(Integer.parseInt(soilDryness.getText().toString()));
            model.setSoilWetness(Integer.parseInt(soilWetness.getText().toString()));
            model.setMotorOnTime(motorOnTime.getText().toString());
            model.setPriority(Integer.parseInt(priority.getText().toString()));
            model.setCycle(Integer.parseInt(cycles.getText().toString()));
            model.setStartFrom((startFromSpinner.getSelectedItem().toString()));
            getHoursAndMinutes(model.getMotorOnTime());
            if (typeOfAction.equals("enable")) {
                model.setEnabled(true);
                model.setModelEmpty(false);
                smsdata = smsUtils.OutSMS_4((model.getFieldNo() < 10 ? String.format("%02d", model.getFieldNo()) : model.getFieldNo() + ""), model.getValveOnPeriod(), model.getValveOffPeriod()
                        , model.getMotorOnTimeHr(), model.getMotorOnTimeMins(), model.getSoilDryness(),
                        model.getSoilWetness(), model.getPriority(), model.getCycle(), spinnerIntValue(model.getStartFrom()));
                baseConfigureFieldIrrigationModel.setLastEnabledFieldNo(fieldNo - 1);
                enableFertigation.setVisibility(View.INVISIBLE);
                disableFertigation.setVisibility(View.INVISIBLE);
                isInitial = false;
            } else {
                model.setEnabled(false);
                model.setModelEmpty(false);
                smsdata = smsUtils.OutSMS_5((fieldNo < 10 ? String.format("%02d", fieldNo) : fieldNo + ""));
                baseConfigureFieldIrrigationModel.setLastEnabledFieldNo(fieldNo - 1);
                disableFertigation.setVisibility(View.INVISIBLE);
            }
            randomNumber = Math.random();
            sendMessage(SmsServices.phoneNumber, smsdata, status, smsReceiver, randomNumber, screen_Specific_SMS);
            modelList.set(fieldNo - 1, model);
            isEditedValveOnPeriod = false;
            isEditedValveOffPeriod = false;
            isEditedSoilDryness = false;
            isEditedSoilWetness = false;
            isEditedMotorOnTime = false;
            isEditedPriority = false;
            isEditedCycles = false;
            isEditedStartFrom = false;
        } else {
            Toast.makeText(Screen_5.this, "Please select the field no", Toast.LENGTH_LONG).show();
            enableFertigation.setVisibility(View.VISIBLE);
        }
    }

    public void checkSMS(String message) {
        try {
            if (message.toLowerCase().contains(SmsUtils.INSMS_4_1.toLowerCase()) && isEnabledClicked) {
                if (Integer.parseInt(message.substring(SmsUtils.INSMS_4_1.length()).trim()) == model.getFieldNo()) {
                    b = false;
                    handlerActivated = false;
                    isEnabledClicked = false;
                    baseConfigureFieldIrrigationModel.setModelList(modelList);
                    curd_files.updateFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE, baseConfigureFieldIrrigationModel);
                    status.setText(message);
                    enableViews();
                    initializeModel();
                }
            } else if (message.toLowerCase().contains(SmsUtils.INSMS_5_1.toLowerCase()) && isDisabledClicked) {
                if (Integer.parseInt(message.substring(SmsUtils.INSMS_5_1.length()).trim()) == model.getFieldNo()) {
                    b = false;
                    isDisabledClicked = false;
                    baseConfigureFieldIrrigationModel.setModelList(modelList);
                    curd_files.updateFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE, baseConfigureFieldIrrigationModel);
                    status.setText(message);
                    counter = 0;
                    handlerActivated = false;
                    enableViews();
                    initializeModel();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int spinnerIntValue(String value) {
        int val = 0;
        switch (value) {
            case "Today":
                val = 0;
                break;
            case "Today+1":
                val = 1;
                break;
            case "Today+2":
                val = 2;
                break;
            case "Today+3":
                val = 3;
                break;
            case "Today+4":
                val = 4;
                break;
            case "Today+5":
                val = 5;
                break;
            case "Today+6":
                val = 6;
                break;
            case "Today+7":
                val = 7;
                break;
            default:
        }
        return val;
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
                if (b && (randomNumber == randomValue) && screen_5_Visible) {
                    disableViews();
                    systemDown = true;
                    smsReceiver.unRegisterBroadCasts();
                    status.setText(SmsUtils.SYSTEM_DOWN);
                    handlerActivated = false;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intentS = (new Intent(Screen_5.this, MainActivity_GSM.class));
                            intentS.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    if (message.equals(activityMessage.toString()) && !(handlerActivated)) {
                        smsReceiver.waitFor_1_Minute(randomNumber, smsReceiver);
                        b = true;
                        handlerActivated = true;
                    }
                } else {
                    status.setText(message + " sending failed");
                    enableViews();
                    isEnabledClicked = false;
                    isDisabledClicked = false;
                    initializeModel();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();
        screen_5_Visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
        screen_5_Visible = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentB = (new Intent(Screen_5.this, Screen_4.class));
        intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentB);

    }


}