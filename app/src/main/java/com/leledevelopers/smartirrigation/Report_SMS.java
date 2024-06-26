package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leledevelopers.smartirrigation.models.Message;
import com.leledevelopers.smartirrigation.services.MessageAdapters;
import com.leledevelopers.smartirrigation.services.SmsServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Report_SMS extends SmsServices {
    private Button printFieldSMS, printAllSMM, back_8;
    private ArrayAdapter<CharSequence> adapter1, adapter2;
    private Spinner fieldSpinner, dateSpinner;
    private RecyclerView recyclerView;
    private List<Message> messages = new ArrayList<Message>();
    private TextView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sms);
        initViews();
        this.context = getApplicationContext();
        try {
            readAllMessages();
            messages = getSMS();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        adapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpinner.setAdapter(adapter1);

        adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.messgesFrom, android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter2);

        printFieldSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterMessages("fields");
            }
        });

        printAllSMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterMessages("date");
            }
        });

        back_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intentB=(new Intent(Report_SMS.this, MainActivity_GSM.class));
                intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentB);
            }
        });
    }

    private void filterMessages(String filterType) {
        List<Message> messageArrayList = new ArrayList<Message>();
        if (filterType.equals("fields")) {
            int fieldNo = Integer.parseInt(fieldSpinner.getSelectedItem().toString());
            String fieldValue = fieldNo < 10 ? String.format("%02d", fieldNo) : fieldNo + "";
            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).getAction().toLowerCase().contains("field no." + fieldValue)
                        || messages.get(i).getAction().toLowerCase().contains("field no. " + fieldValue)
                        || messages.get(i).getAction().contains("Wet Field Detected.")
                        || messages.get(i).getAction().contains("Phase failure detected, Suspending all Actions")) {
                    messageArrayList.add(messages.get(i));
                }
                if (i == messages.size() - 1) {
                    sortMessages(messageArrayList);
                }
            }
        } else if (filterType.equals("date")) {
            int selectedDate = spinnerIntValue(dateSpinner.getSelectedItem().toString());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, -selectedDate);
            String pastDate = dateFormat.format(currentCal.getTime());
            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).getDate().equals(pastDate)) {
                    messageArrayList.add(messages.get(i));
                }
                if (i == messages.size() - 1) {
                    sortMessages(messageArrayList);
                }
            }
        }
    }

    private void sortMessages(List<Message> messageArrayList) {
        if(messageArrayList.size() != 0){
            nodata.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            Collections.sort(messageArrayList, new SortByDate());
            messageArrayList = reverseMessageList(messageArrayList);
            startRecyclerView(messageArrayList);
        } else {
            nodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

    }

    private int spinnerIntValue(String value) {
        int val = 0;
        switch (value) {
            case "Today":
                val = 0;
                break;
            case "Yesterday":
                val = 1;
                break;
            case "Today-2":
                val = 2;
                break;
            case "Today-3":
                val = 3;
                break;
            case "Today-4":
                val = 4;
                break;
            case "Today-5":
                val = 5;
                break;
            case "Today-6":
                val = 6;
                break;
        }
        return val;
    }

    private void startRecyclerView(List<Message> messageList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MessageAdapters(messageList));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void initViews() {
        fieldSpinner = findViewById(R.id.fieldNoSpinner8);
        dateSpinner = findViewById(R.id.DateSpinner8);
        printFieldSMS = findViewById(R.id.printFieldSMM);
        printAllSMM = findViewById(R.id.printAllSMM);
        recyclerView = findViewById(R.id.displaySMS);
        back_8 = findViewById(R.id.back_8);
        nodata = findViewById(R.id.no_data);
    }

    @Override
    public void enableViews() {

    }

    @Override
    public void disableViews() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentB=(new Intent(Report_SMS.this, MainActivity_GSM.class));
        intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentB);
    }

    private List<Message> reverseMessageList(List<Message> messages) {
        if (messages.size() <= 1) {
            return messages;
        }
        for (int i = 0; i < messages.size() / 2; i++) {
            Message temp1 = messages.get(i);
            Message temp2 = messages.get(messages.size() - 1 - i);
            messages.set(i, temp2);
            messages.set(messages.size() - 1 - i, temp1);
        }
        return messages;
    }

    static class SortByDate implements Comparator<Message> {
        @Override
        public int compare(Message a, Message b) {
            return a.getDateTime().compareTo(b.getDateTime());
        }
    }
}

//http://realembed.blogspot.com/2013/11/retrieve-sms-message-on-particular-date.html
//https://findnerd.com/list/view/How-to-read-sms-from-a-particular-date-in-Android/8556/