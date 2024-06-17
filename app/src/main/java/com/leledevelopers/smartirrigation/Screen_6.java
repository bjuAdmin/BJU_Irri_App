package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.leledevelopers.smartirrigation.models.BaseConfigurationFeildFertigationModel;
import com.leledevelopers.smartirrigation.models.ConfigurationFeildFertigationModel;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.SmsTesting;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Screen_6 extends SmsServices {
    private static final String TAG = Screen_6.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private Boolean b = false, systemDown = false;
    private Spinner fieldSpinner;
    EditText wetPeriod, injectPeriod, noOfIterations, injector1OnPeriod, injector1OffPeriod, injector1Cycles, injector2OnPeriod, injector2OffPeriod, injector2Cycles, injector3OnPeriod, injector3OffPeriod, injector3Cycles, injector4OnPeriod, injector4OffPeriod, injector4Cycles;
    private Button enableFieldFertigation, disableFieldFertigation, back_6;
    private ArrayAdapter<CharSequence> adapter;
    private TextView status;
    private ConfigurationFeildFertigationModel model;
    private CURD_Files<ConfigurationFeildFertigationModel> curd_files = new CURD_FilesImpl<ConfigurationFeildFertigationModel>();
    private List<ConfigurationFeildFertigationModel> modelList = new ArrayList<ConfigurationFeildFertigationModel>();
    private BaseConfigurationFeildFertigationModel baseConfigurationFeildFertigationModel = new BaseConfigurationFeildFertigationModel();
    private SmsUtils smsUtils = new SmsUtils();
    private String regex = "\\d+";
    private int fieldNo;
    private boolean isEditedInjectPeriod = false;
    private boolean isEditedNoOfIterations = false;
    private boolean isEditedWetPeriod = false;
    private boolean isEditedInjector1OnPeriod = false;
    private boolean isEditedInjector2OnPeriod = false;
    private boolean isEditedInjector3OnPeriod = false;
    private boolean isEditedInjector4OnPeriod = false;
    private boolean isEditedInjector1OffPeriod = false;
    private boolean isEditedInjector2OffPeriod = false;
    private boolean isEditedInjector3OffPeriod = false;
    private boolean isEditedInjector4OffPeriod = false;
    private boolean isEditedInjector1Cycles = false;
    private boolean isEditedInjector2Cycles = false;
    private boolean isEditedInjector3Cycles = false;
    private boolean isEditedInjector4Cycles = false;
    private boolean isInitial = false;
    private boolean isEnabledClicked = false;
    private boolean isDisabledClicked = false;
    private boolean handlerActivated = false;
    private double randomNumber = -1;
    private static boolean screen_6_Visible = false;
    private SmsTesting smsTesting = new SmsTesting();
    private StringBuffer activityMessage = new StringBuffer("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen6);
        this.context = getApplicationContext();
        initViews();
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpinner.setAdapter(adapter);
        initializeModel();
        fieldSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (modelList.get(position).isEnabled() || !modelList.get(position).isModelEmpty()) {
                    model = modelList.get(position);
                    wetPeriod.setText(model.getWetPeriod() + "");
                    injectPeriod.setText(model.getInjectPeriod() + "");
                    noOfIterations.setText(model.getNoIterations() + "");
                    injector1OnPeriod.setText(model.getInjector1OnPeriod() + "");
                    injector1OffPeriod.setText(model.getInjector1OffPeriod() + "");
                    injector1Cycles.setText(model.getInjector1Cycles() + "");
                    injector2OnPeriod.setText(model.getInjector2OnPeriod() + "");
                    injector2OffPeriod.setText(model.getInjector2OffPeriod() + "");
                    injector2Cycles.setText(model.getInjector2Cycles() + "");
                    injector3OnPeriod.setText(model.getInjector3OnPeriod() + "");
                    injector3OffPeriod.setText(model.getInjector3OffPeriod() + "");
                    injector3Cycles.setText(model.getInjector3Cycles() + "");
                    injector4OnPeriod.setText(model.getInjector4OnPeriod() + "");
                    injector4OffPeriod.setText(model.getInjector4OffPeriod() + "");
                    injector4Cycles.setText(model.getInjector4Cycles() + "");

                    if (model.isEnabled()) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                        enableFieldFertigation.setVisibility(View.VISIBLE);
                    } else {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                        enableFieldFertigation.setVisibility(View.VISIBLE);
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

        wetPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wetPeriod.setCursorVisible(true);
            }
        });

        injectPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injectPeriod.setCursorVisible(true);
            }
        });

        noOfIterations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOfIterations.setCursorVisible(true);
            }
        });

        injector1OnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector1OnPeriod.setCursorVisible(true);
            }
        });

        injector1OffPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector1OffPeriod.setCursorVisible(true);
            }
        });

        injector1Cycles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector1Cycles.setCursorVisible(true);
            }
        });

        injector2OnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector2OnPeriod.setCursorVisible(true);
            }
        });

        injector2OffPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector2OffPeriod.setCursorVisible(true);
            }
        });

        injector2Cycles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector2Cycles.setCursorVisible(true);
            }
        });

        injector3OnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector3OnPeriod.setCursorVisible(true);
            }
        });

        injector3OffPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector3OffPeriod.setCursorVisible(true);
            }
        });

        injector3Cycles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector3Cycles.setCursorVisible(true);
            }
        });

        injector4OnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector4OnPeriod.setCursorVisible(true);
            }
        });

        injector4OffPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector4OffPeriod.setCursorVisible(true);
            }
        });

        injector4Cycles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injector4Cycles.setCursorVisible(true);
            }
        });
        enableFieldFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (validateInput() && !systemDown) {
                    status.setText("Enable fertigation configuration SMS sent\r\nWaiting for response from controller");
                    disableViews();
                    cursorVisibility();
                    activityMessage.replace(0, activityMessage.length(), "Enable fertigation configuration SMS ");
                    updateData_And_SendSMS("enable", "Enable fertigation configuration SMS ");
                    isEnabledClicked = true;
                }
            }
        });

        disableFieldFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (!systemDown) {
                    status.setText("Disable fertigation configuration SMS sent\r\nWaiting for response from controller");
                    disableViews();
                    activityMessage.replace(0, activityMessage.length(), "Disable fertigation configuration SMS ");
                    updateData_And_SendSMS("disable", "Disable fertigation configuration SMS ");

                    randomNumber = Math.random();
                    isDisabledClicked = true;
                }
            }
        });
        back_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomNumber = -1;
                Intent intentB = (new Intent(Screen_6.this, Screen_4.class));
                intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentB);

            }
        });
        wetPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(wetPeriod.getText().toString().matches(regex)
                            && wetPeriod.getText().toString().length() >= 1
                            && validateRange(0, 999, Integer.parseInt(wetPeriod.getText().toString())))) {
                        wetPeriod.getText().clear();
                        wetPeriod.setError("Enter value between 1 to 999");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (wetPeriod.getText().toString().equals(model.getWetPeriod() + "")) {
                            isEditedWetPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedWetPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injectPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injectPeriod.getText().toString().matches(regex)
                            && injectPeriod.getText().toString().length() >= 1
                            && validateRange(0, 999, Integer.parseInt(injectPeriod.getText().toString())))) {
                        injectPeriod.getText().clear();
                        injectPeriod.setError("Enter value between 1 to 999");

                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injectPeriod.getText().toString().equals(model.getInjectPeriod() + "")) {
                            isEditedInjectPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjectPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        noOfIterations.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(noOfIterations.getText().toString().matches(regex)
                            && noOfIterations.getText().toString().length() >= 1
                            && validateRange(0, 99, Integer.parseInt(noOfIterations.getText().toString())))) {
                        noOfIterations.getText().clear();
                        noOfIterations.setError("Enter value between 1 to 99");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (noOfIterations.getText().toString().equals(model.getNoIterations() + "")) {
                            isEditedNoOfIterations = false;
                            isAnyViewEdited();
                        } else {
                            isEditedNoOfIterations = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector1OnPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector1OnPeriod.getText().toString().matches(regex)
                            && injector1OnPeriod.getText().toString().length() >= 1
                            && validateRange(0, 999, Integer.parseInt(injector1OnPeriod.getText().toString())))) {
                        injector1OnPeriod.getText().clear();
                        injector1OnPeriod.setError("Enter value between 1 to 999");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector1OnPeriod.getText().toString().equals(model.getInjector1OnPeriod() + "")) {
                            isEditedInjector1OnPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector1OnPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector1OffPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector1OffPeriod.getText().toString().matches(regex)
                            && injector1OffPeriod.getText().toString().length() >= 1
                            && validateRange(0, 999, Integer.parseInt(injector1OffPeriod.getText().toString())))) {
                        injector1OffPeriod.getText().clear();
                        injector1OffPeriod.setError("Enter value between 1 to 999");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector1OffPeriod.getText().toString().equals(model.getInjector1OffPeriod() + "")) {
                            isEditedInjector1OffPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector1OffPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector1Cycles.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector1Cycles.getText().toString().matches(regex)
                            && injector1Cycles.getText().toString().length() >= 1
                            && validateRange(0, 99, Integer.parseInt(injector1Cycles.getText().toString())))) {
                        injector1Cycles.getText().clear();
                        injector1Cycles.setError("Enter value between 1 to 99");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector1Cycles.getText().toString().equals(model.getInjector1Cycles() + "")) {
                            isEditedInjector1Cycles = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector1Cycles = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector2OnPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector2OnPeriod.getText().toString().matches(regex)
                            && injector2OnPeriod.getText().toString().length() >= 1
                            && validateRange(0, 999, Integer.parseInt(injector2OnPeriod.getText().toString())))) {
                        injector2OnPeriod.getText().clear();
                        injector2OnPeriod.setError("Enter value between 1 to 999");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector2OnPeriod.getText().toString().equals(model.getInjector2OnPeriod() + "")) {
                            isEditedInjector2OnPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector2OnPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector2OffPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector2OffPeriod.getText().toString().matches(regex)
                            && injector2OffPeriod.getText().toString().length() >= 1
                            && validateRange(0, 999, Integer.parseInt(injector2OffPeriod.getText().toString())))) {
                        injector2OffPeriod.getText().clear();
                        injector2OffPeriod.setError("Enter value between 1 to 999");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector2OffPeriod.getText().toString().equals(model.getInjector2OffPeriod() + "")) {
                            isEditedInjector2OffPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector2OffPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector2Cycles.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector2Cycles.getText().toString().matches(regex)
                            && injector2Cycles.getText().toString().length() >= 1
                            && validateRange(0, 99, Integer.parseInt(injector2Cycles.getText().toString())))) {
                        injector2Cycles.getText().clear();
                        injector2Cycles.setError("Enter value between 1 to 99");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector2Cycles.getText().toString().equals(model.getInjector2Cycles() + "")) {
                            isEditedInjector2Cycles = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector2Cycles = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector3OnPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector3OnPeriod.getText().toString().matches(regex)
                            && injector3OnPeriod.getText().toString().length() >= 1
                            && validateRange(0, 999, Integer.parseInt(injector3OnPeriod.getText().toString())))) {
                        injector3OnPeriod.getText().clear();
                        injector3OnPeriod.setError("Enter value between 1 to 999");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector3OnPeriod.getText().toString().equals(model.getInjector3OnPeriod() + "")) {
                            isEditedInjector3OnPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector3OnPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector3OffPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector3OffPeriod.getText().toString().matches(regex)
                            && injector3OffPeriod.getText().toString().length() >= 1
                            && validateRange(0, 999, Integer.parseInt(injector3OffPeriod.getText().toString())))) {
                        injector3OffPeriod.getText().clear();
                        injector3OffPeriod.setError("Enter value between 1 to 999");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector3OffPeriod.getText().toString().equals(model.getInjector3OffPeriod() + "")) {
                            isEditedInjector3OffPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector3OffPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector3Cycles.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector3Cycles.getText().toString().matches(regex)
                            && injector3Cycles.getText().toString().length() >= 1
                            && validateRange(0, 99, Integer.parseInt(injector3Cycles.getText().toString())))) {
                        injector3Cycles.getText().clear();
                        injector3Cycles.setError("Enter value between 1 to 99");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector3Cycles.getText().toString().equals(model.getInjector3Cycles() + "")) {
                            isEditedInjector3Cycles = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector3Cycles = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector4OnPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector4OnPeriod.getText().toString().matches(regex)
                            && injector4OnPeriod.getText().toString().length() >= 1
                            && validateRange(0, 999, Integer.parseInt(injector4OnPeriod.getText().toString())))) {
                        injector4OnPeriod.getText().clear();
                        injector4OnPeriod.setError("Enter value between 1 to 999");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector4OnPeriod.getText().toString().equals(model.getInjector4OnPeriod() + "")) {
                            isEditedInjector4OnPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector4OnPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector4OffPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector4OffPeriod.getText().toString().matches(regex)
                            && injector4OffPeriod.getText().toString().length() >= 1
                            && validateRange(0, 999, Integer.parseInt(injector4OffPeriod.getText().toString())))) {
                        injector4OffPeriod.getText().clear();
                        injector4OffPeriod.setError("Enter value between 1 to 999");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector4OffPeriod.getText().toString().equals(model.getInjector4OffPeriod() + "")) {
                            isEditedInjector4OffPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector4OffPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector4Cycles.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injector4Cycles.getText().toString().matches(regex)
                            && injector4Cycles.getText().toString().length() >= 1
                            && validateRange(0, 99, Integer.parseInt(injector4Cycles.getText().toString())))) {
                        injector4Cycles.getText().clear();
                        injector4Cycles.setError("Enter value between 1 to 99");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                    } else if (model.isEnabled()) {
                        if (injector4Cycles.getText().toString().equals(model.getInjector4Cycles() + "")) {
                            isEditedInjector4Cycles = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjector4Cycles = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injector4Cycles.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    try {

                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    injector4Cycles.clearFocus();
                }
                return true;
            }
        });
    }

    @Override
    public void initViews() {
        fieldSpinner = (Spinner) findViewById(R.id.fieldNoSpinner6);
        wetPeriod = findViewById(R.id.wetPeriod);
        injectPeriod = findViewById(R.id.injectPeriod);
        noOfIterations = findViewById(R.id.iterations6);
        injector1OnPeriod = findViewById(R.id.injector1OnPeriod);
        injector1OffPeriod = findViewById(R.id.injector1OffPeriod);
        injector1Cycles = findViewById(R.id.injector1cycles);
        injector2OnPeriod = findViewById(R.id.injector2OnPeriod);
        injector2OffPeriod = findViewById(R.id.injector2OffPeriod);
        injector2Cycles = findViewById(R.id.injector2cycles);
        injector3OnPeriod = findViewById(R.id.injector3OnPeriod);
        injector3OffPeriod = findViewById(R.id.injector3OffPeriod);
        injector3Cycles = findViewById(R.id.injector3cycles);
        injector4OnPeriod = findViewById(R.id.injector4OnPeriod);
        injector4OffPeriod = findViewById(R.id.injector4OffPeriod);
        injector4Cycles = findViewById(R.id.injector4cycles);

        enableFieldFertigation = findViewById(R.id.enableFieldFertigation6);
        disableFieldFertigation = findViewById(R.id.disableFieldFertigation6);
        back_6 = findViewById(R.id.back_6);
        status = findViewById(R.id.screen_6_status);
    }

    @Override
    public void enableViews() {
        fieldSpinner.setEnabled(true);
        wetPeriod.setEnabled(true);
        injectPeriod.setEnabled(true);
        noOfIterations.setEnabled(true);
        injector1OnPeriod.setEnabled(true);
        injector1OffPeriod.setEnabled(true);
        injector1Cycles.setEnabled(true);
        injector2OnPeriod.setEnabled(true);
        injector2OffPeriod.setEnabled(true);
        injector2Cycles.setEnabled(true);
        injector3OnPeriod.setEnabled(true);
        injector3OffPeriod.setEnabled(true);
        injector3Cycles.setEnabled(true);
        injector4OnPeriod.setEnabled(true);
        injector4OffPeriod.setEnabled(true);
        injector4Cycles.setEnabled(true);
    }

    @Override
    public void disableViews() {
        fieldSpinner.setEnabled(false);
        wetPeriod.setEnabled(false);
        injectPeriod.setEnabled(false);
        noOfIterations.setEnabled(false);
        injector1OnPeriod.setEnabled(false);
        injector1OffPeriod.setEnabled(false);
        injector1Cycles.setEnabled(false);
        injector2OnPeriod.setEnabled(false);
        injector2OffPeriod.setEnabled(false);
        injector2Cycles.setEnabled(false);
        injector3OnPeriod.setEnabled(false);
        injector3OffPeriod.setEnabled(false);
        injector3Cycles.setEnabled(false);
        injector4OnPeriod.setEnabled(false);
        injector4OffPeriod.setEnabled(false);
        injector4Cycles.setEnabled(false);
    }


    private void isAnyViewEdited() {
        if (isEditedInjector4Cycles || isEditedInjector4OffPeriod || isEditedInjector4OnPeriod || isEditedInjector3Cycles || isEditedInjector3OffPeriod || isEditedInjector3OnPeriod || isEditedInjector2Cycles || isEditedInjector2OffPeriod || isEditedInjector2OnPeriod || isEditedInjector1Cycles || isEditedInjector1OffPeriod || isEditedInjector1OnPeriod || isEditedNoOfIterations || isEditedInjectPeriod || isEditedWetPeriod) {
            enableFieldFertigation.setVisibility(View.VISIBLE);
            disableFieldFertigation.setVisibility(View.INVISIBLE);
        } else {
            disableFieldFertigation.setVisibility(View.VISIBLE);
            enableFieldFertigation.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public String toString() {
        return "Screen_6{" +
                "isEditedInjectPeriod=" + isEditedInjectPeriod +
                ", isEditedNoOfIterations=" + isEditedNoOfIterations +
                ", isEditedWetPeriod=" + isEditedWetPeriod +
                ", isEditedInjector1OnPeriod=" + isEditedInjector1OnPeriod +
                ", isEditedInjector2OnPeriod=" + isEditedInjector2OnPeriod +
                ", isEditedInjector3OnPeriod=" + isEditedInjector3OnPeriod +
                ", isEditedInjector4OnPeriod=" + isEditedInjector4OnPeriod +
                ", isEditedInjector1OffPeriod=" + isEditedInjector1OffPeriod +
                ", isEditedInjector2OffPeriod=" + isEditedInjector2OffPeriod +
                ", isEditedInjector3OffPeriod=" + isEditedInjector3OffPeriod +
                ", isEditedInjector4OffPeriod=" + isEditedInjector4OffPeriod +
                ", isEditedInjector1Cycles=" + isEditedInjector1Cycles +
                ", isEditedInjector2Cycles=" + isEditedInjector2Cycles +
                ", isEditedInjector3Cycles=" + isEditedInjector3Cycles +
                ", isEditedInjector4Cycles=" + isEditedInjector4Cycles +
                ", isInitial=" + isInitial +
                '}';
    }

    private boolean validateInput() {
        if (fieldSpinner.getSelectedItem().toString().trim().equals("Pick one")) {

            return false;
        }
        if (!(wetPeriod.getText().toString().matches(regex)
                && wetPeriod.getText().toString().length() >= 1
                && validateRange(0, 999, Integer.parseInt(wetPeriod.getText().toString())))) {
            wetPeriod.getText().clear();
            wetPeriod.setError("Enter value between 1 to 999");
            return false;
        }
        if (!(injectPeriod.getText().toString().matches(regex)
                && injectPeriod.getText().toString().length() >= 1
                && validateRange(0, 999, Integer.parseInt(injectPeriod.getText().toString())))) {
            injectPeriod.getText().clear();
            injectPeriod.setError("Enter value between 1 to 999");
            return false;
        }
        if (!(noOfIterations.getText().toString().matches(regex)
                && noOfIterations.getText().toString().length() >= 1
                && validateRange(0, 99, Integer.parseInt(noOfIterations.getText().toString())))) {
            noOfIterations.getText().clear();
            noOfIterations.setError("Enter value between 1 to 99");
            return false;
        }
        if (!(injector1OnPeriod.getText().toString().matches(regex)
                && injector1OnPeriod.getText().toString().length() >= 1
                && validateRange(0, 999, Integer.parseInt(injector1OnPeriod.getText().toString())))) {
            injector1OnPeriod.getText().clear();
            injector1OnPeriod.setError("Enter value between 1 to 999");
            return false;
        }
        if (!(injector1OffPeriod.getText().toString().matches(regex)
                && injector1OffPeriod.getText().toString().length() >= 1
                && validateRange(0, 999, Integer.parseInt(injector1OffPeriod.getText().toString())))) {
            injector1OffPeriod.getText().clear();
            injector1OffPeriod.setError("Enter value between 1 to 999");
            return false;
        }
        if (!(injector1Cycles.getText().toString().matches(regex)
                && injector1Cycles.getText().toString().length() >= 1
                && validateRange(0, 99, Integer.parseInt(injector1Cycles.getText().toString())))) {
            injector1Cycles.getText().clear();
            injector1Cycles.setError("Enter value between 1 to 99");
            return false;
        }
        if (!(injector2OnPeriod.getText().toString().matches(regex)
                && injector2OnPeriod.getText().toString().length() >= 1
                && validateRange(0, 999, Integer.parseInt(injector2OnPeriod.getText().toString())))) {
            injector2OnPeriod.getText().clear();
            injector2OnPeriod.setError("Enter value between 1 to 999");
            return false;
        }
        if (!(injector2OffPeriod.getText().toString().matches(regex)
                && injector2OffPeriod.getText().toString().length() >= 1
                && validateRange(0, 999, Integer.parseInt(injector2OffPeriod.getText().toString())))) {
            injector2OffPeriod.getText().clear();
            injector2OffPeriod.setError("Enter value between 1 to 999");
            return false;
        }
        if (!(injector2Cycles.getText().toString().matches(regex)
                && injector2Cycles.getText().toString().length() >= 1
                && validateRange(0, 99, Integer.parseInt(injector2Cycles.getText().toString())))) {
            injector2Cycles.getText().clear();
            injector2Cycles.setError("Enter value between 1 to 99");
            return false;
        }
        if (!(injector3OnPeriod.getText().toString().matches(regex)
                && injector3OnPeriod.getText().toString().length() >= 1
                && validateRange(0, 999, Integer.parseInt(injector3OnPeriod.getText().toString())))) {
            injector3OnPeriod.getText().clear();
            injector3OnPeriod.setError("Enter value between 1 to 999");
            return false;
        }
        if (!(injector3OffPeriod.getText().toString().matches(regex)
                && injector3OffPeriod.getText().toString().length() >= 1
                && validateRange(0, 999, Integer.parseInt(injector3OffPeriod.getText().toString())))) {
            injector3OffPeriod.getText().clear();
            injector3OffPeriod.setError("Enter value between 1 to 999");
            return false;
        }
        if (!(injector3Cycles.getText().toString().matches(regex)
                && injector3Cycles.getText().toString().length() >= 1
                && validateRange(0, 99, Integer.parseInt(injector3Cycles.getText().toString())))) {
            injector3Cycles.getText().clear();
            injector3Cycles.setError("Enter value between 1 to 99");
            return false;
        }
        if (!(injector4OnPeriod.getText().toString().matches(regex)
                && injector4OnPeriod.getText().toString().length() >= 1
                && validateRange(0, 999, Integer.parseInt(injector4OnPeriod.getText().toString())))) {
            injector4OnPeriod.getText().clear();
            injector4OnPeriod.setError("Enter value between 1 to 999");
            return false;
        }
        if (!(injector4OffPeriod.getText().toString().matches(regex)
                && injector4OffPeriod.getText().toString().length() >= 1
                && validateRange(0, 999, Integer.parseInt(injector4OffPeriod.getText().toString())))) {
            injector4OffPeriod.getText().clear();
            injector4OffPeriod.setError("Enter value between 1 to 999");
            return false;
        }
        if (!(injector4Cycles.getText().toString().matches(regex)
                && injector4Cycles.getText().toString().length() >= 1
                && validateRange(0, 99, Integer.parseInt(injector4Cycles.getText().toString())))) {
            injector4Cycles.getText().clear();
            injector4Cycles.setError("Enter value between 1 to 99");
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
            if (curd_files.isFileHasData(getApplicationContext(), ProjectUtils.CONFG_FERTIGATION_FILE)) {
                baseConfigurationFeildFertigationModel = (BaseConfigurationFeildFertigationModel) curd_files.getFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE);
                modelList = baseConfigurationFeildFertigationModel.getModelList();
                if (baseConfigurationFeildFertigationModel.getLastEnabledFieldNo() != -1) {
                    model = modelList.get(baseConfigurationFeildFertigationModel.getLastEnabledFieldNo());
                    if (model.isEnabled() || !model.isModelEmpty()) {
                        fieldSpinner.setSelection(model.getFieldNo() - 1);
                        wetPeriod.setText(model.getWetPeriod() + "");
                        injectPeriod.setText(model.getInjectPeriod() + "");
                        noOfIterations.setText(model.getNoIterations() + "");
                        injector1OnPeriod.setText(model.getInjector1OnPeriod() + "");
                        injector2OnPeriod.setText(model.getInjector2OnPeriod() + "");
                        injector3OnPeriod.setText(model.getInjector3OnPeriod() + "");
                        injector4OnPeriod.setText(model.getInjector4OnPeriod() + "");
                        injector1OffPeriod.setText(model.getInjector1OffPeriod() + "");
                        injector2OffPeriod.setText(model.getInjector2OffPeriod() + "");
                        injector3OffPeriod.setText(model.getInjector3OffPeriod() + "");
                        injector4OffPeriod.setText(model.getInjector4OffPeriod() + "");
                        injector1Cycles.setText(model.getInjector1Cycles() + "");
                        injector2Cycles.setText(model.getInjector2Cycles() + "");
                        injector3Cycles.setText(model.getInjector3Cycles() + "");
                        injector4Cycles.setText(model.getInjector4Cycles() + "");


                        if (model.isEnabled()) {
                            disableFieldFertigation.setVisibility(View.VISIBLE);
                            enableFieldFertigation.setVisibility(View.VISIBLE);
                        } else {
                            disableFieldFertigation.setVisibility(View.VISIBLE);
                            enableFieldFertigation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        isInitial = true;
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                        enableFieldFertigation.setVisibility(View.VISIBLE);
                    }
                } else {
                    isInitial = true;
                    disableFieldFertigation.setVisibility(View.VISIBLE);
                    enableFieldFertigation.setVisibility(View.VISIBLE);
                }
            } else {
                for (int i = 0; i < 12; i++) {
                    ConfigurationFeildFertigationModel modelData = new ConfigurationFeildFertigationModel();
                    modelData.setFieldNo(i);
                    modelData.setEnabled(false);
                    modelList.add(modelData);
                    baseConfigurationFeildFertigationModel.setModelList(modelList);
                }
                curd_files.createFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE, baseConfigurationFeildFertigationModel);
                isInitial = true;
                setEmptyData();
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setEmptyData() {
        disableFieldFertigation.setVisibility(View.VISIBLE);
        enableFieldFertigation.setVisibility(View.VISIBLE);
        wetPeriod.setText("");
        injectPeriod.setText("");
        noOfIterations.setText("");
        injector1OnPeriod.setText("");
        injector2OnPeriod.setText("");
        injector3OnPeriod.setText("");
        injector4OnPeriod.setText("");
        injector1OffPeriod.setText("");
        injector2OffPeriod.setText("");
        injector3OffPeriod.setText("");
        injector4OffPeriod.setText("");;
        injector1Cycles.setText("");
        injector2Cycles.setText("");
        injector3Cycles.setText("");
        injector4Cycles.setText("");
        model = null;
    }

    private void updateData_And_SendSMS(String typeOfAction, String screen_Specific_SMS) {
        if (!fieldSpinner.getSelectedItem().toString().trim().equals("Pick one")) {
            String smsdata;
            fieldNo = Integer.parseInt(fieldSpinner.getSelectedItem().toString());
            model = modelList.get(fieldNo - 1);
            model.setFieldNo(Integer.parseInt(fieldSpinner.getSelectedItem().toString()));
            model.setWetPeriod(Integer.parseInt(wetPeriod.getText().toString()));
            model.setInjectPeriod(Integer.parseInt(injectPeriod.getText().toString()));
            model.setNoIterations(Integer.parseInt(noOfIterations.getText().toString()));
            model.setInjector1OnPeriod(Integer.parseInt(injector1OnPeriod.getText().toString()));
            model.setInjector2OnPeriod(Integer.parseInt(injector2OnPeriod.getText().toString()));
            model.setInjector3OnPeriod(Integer.parseInt(injector3OnPeriod.getText().toString()));
            model.setInjector4OnPeriod(Integer.parseInt(injector4OnPeriod.getText().toString()));
            model.setInjector1OffPeriod(Integer.parseInt(injector1OffPeriod.getText().toString()));
            model.setInjector2OffPeriod(Integer.parseInt(injector2OffPeriod.getText().toString()));
            model.setInjector3OffPeriod(Integer.parseInt(injector3OffPeriod.getText().toString()));
            model.setInjector4OffPeriod(Integer.parseInt(injector4OffPeriod.getText().toString()));
            model.setInjector1Cycles(Integer.parseInt(injector1Cycles.getText().toString()));
            model.setInjector2Cycles(Integer.parseInt(injector2Cycles.getText().toString()));
            model.setInjector3Cycles(Integer.parseInt(injector3Cycles.getText().toString()));
            model.setInjector4Cycles(Integer.parseInt(injector4Cycles.getText().toString()));
            if (typeOfAction.equals("enable")) {
                model.setEnabled(true);
                model.setModelEmpty(false);
                smsdata = smsUtils.OutSMS_6((model.getFieldNo() < 10 ? String.format("%02d", model.getFieldNo()) : model.getFieldNo() + ""), model.getWetPeriod(),
                        model.getInjectPeriod(), model.getNoIterations(), model.getInjector1OnPeriod(), model.getInjector1OffPeriod(), model.getInjector1Cycles(), model.getInjector2OnPeriod(), model.getInjector2OffPeriod(), model.getInjector2Cycles(), model.getInjector3OnPeriod(), model.getInjector3OffPeriod(), model.getInjector3Cycles(), model.getInjector4OnPeriod(), model.getInjector4OffPeriod(), model.getInjector4Cycles());
                baseConfigurationFeildFertigationModel.setLastEnabledFieldNo(fieldNo - 1);
                enableFieldFertigation.setVisibility(View.INVISIBLE);
                disableFieldFertigation.setVisibility(View.INVISIBLE);
                isInitial = false;
            } else {
                model.setEnabled(false);
                model.setModelEmpty(false);
                smsdata = smsUtils.OutSMS_7((fieldNo < 10 ? String.format("%02d", fieldNo) : fieldNo + ""));
                baseConfigurationFeildFertigationModel.setLastEnabledFieldNo(fieldNo - 1);
                disableFieldFertigation.setVisibility(View.INVISIBLE);
            }
            randomNumber = Math.random();
            SmsTesting.contextPri = getApplicationContext();
            smsTesting.sendMessageBox(SmsServices.phoneNumber, smsdata, randomNumber, screen_Specific_SMS);
            modelList.set(fieldNo - 1, model);
            isEditedInjectPeriod = false;
            isEditedNoOfIterations = false;
            isEditedWetPeriod = false;
            isEditedInjector1OnPeriod = false;
            isEditedInjector2OnPeriod = false;
            isEditedInjector3OnPeriod = false;
            isEditedInjector4OnPeriod = false;
            isEditedInjector1OffPeriod = false;
            isEditedInjector2OffPeriod = false;
            isEditedInjector3OffPeriod = false;
            isEditedInjector4OffPeriod = false;
            isEditedInjector1Cycles = false;
            isEditedInjector2Cycles = false;
            isEditedInjector3Cycles = false;
            isEditedInjector4Cycles = false;
        } else {
            Toast.makeText(Screen_6.this, "Please select the field no", Toast.LENGTH_LONG).show();
            enableFieldFertigation.setVisibility(View.VISIBLE);
        }
    }

    private void cursorVisibility() {
        try {
            wetPeriod.setCursorVisible(false);
            injectPeriod.setCursorVisible(false);
            noOfIterations.setCursorVisible(false);
            injector1OnPeriod.setCursorVisible(false);
            injector2OnPeriod.setCursorVisible(false);
            injector3OnPeriod.setCursorVisible(false);
            injector4OnPeriod.setCursorVisible(false);
            injector1OffPeriod.setCursorVisible(false);
            injector2OffPeriod.setCursorVisible(false);
            injector3OffPeriod.setCursorVisible(false);
            injector4OffPeriod.setCursorVisible(false);
            injector1Cycles.setCursorVisible(false);
            injector2Cycles.setCursorVisible(false);
            injector3Cycles.setCursorVisible(false);
            injector4Cycles.setCursorVisible(false);
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
                 if (b && (randomNumber == randomValue) && screen_6_Visible) {
                    disableViews();
                    systemDown = true;
                    smsReceiver.unRegisterBroadCasts();
                    status.setText(SmsUtils.SYSTEM_DOWN);
                    handlerActivated = false;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            randomNumber = -1;
                            Intent intentS = (new Intent(Screen_6.this, MainActivity_GSM.class));
                            intentS.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentS);
                        }
                    }, 5000);
                }
            }

        });

        smsTesting.setSmsServiceBroadcast(new SmsServiceBroadcast() {
            @Override
            public void onReceiveSmsDeliveredStatus(boolean smsDeliveredStatus, String message) {
                if (smsDeliveredStatus) {
                    if (message.equals(activityMessage.toString()) && !(handlerActivated)) {
                        handlerActivated = true;
                        smsReceiver.waitFor_1_Minute(randomNumber, smsReceiver);
                        b = true;
                    }
                } else {
                    status.setText(message + " sending failed");
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
        screen_6_Visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
        screen_6_Visible = false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        randomNumber = -1;
        startActivity(new Intent(Screen_6.this, Screen_4.class));
        finish();
    }

    public void checkSMS(String message) {
        try {
            if (message.toLowerCase().contains(SmsUtils.INSMS_6_1.toLowerCase()) && isEnabledClicked) {
                if (Integer.parseInt(message.substring(SmsUtils.INSMS_6_1.length()).trim()) == model.getFieldNo()) {
                    b = false;
                    handlerActivated = false;
                    isEnabledClicked = false;
                    baseConfigurationFeildFertigationModel.setModelList(modelList);
                    curd_files.updateFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE, baseConfigurationFeildFertigationModel);
                    status.setText("Fertigation enabled for field no. " + model.getFieldNo());
                    enableViews();
                    initializeModel();
                }
            } else if (message.toLowerCase().contains(SmsUtils.INSMS_6_2.toLowerCase())) {
                b = false;
                handlerActivated = false;
                status.setText("Incorrect values Fertigation not enabled for field no." + model.getFieldNo());
                enableViews();
                initializeModel();
            } else if (message.toLowerCase().contains(SmsUtils.INSMS_6_3.toLowerCase())) {
                b = false;
                handlerActivated = false;
                status.setText("Irrigation is not Active. Fertigation not enabled for field no." + model.getFieldNo());
                enableViews();
                initializeModel();
            } else if (message.toLowerCase().contains(SmsUtils.INSMS_7_1.toLowerCase()) && isDisabledClicked) {
                if (Integer.parseInt(message.substring(SmsUtils.INSMS_7_1.length()).trim()) == model.getFieldNo()) {
                    b = false;
                    isDisabledClicked = false;
                    handlerActivated = false;
                    baseConfigurationFeildFertigationModel.setModelList(modelList);
                    curd_files.updateFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE, baseConfigurationFeildFertigationModel);
                    status.setText("Fertigation disabled for field no." + model.getFieldNo());
                    enableViews();
                    initializeModel();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
