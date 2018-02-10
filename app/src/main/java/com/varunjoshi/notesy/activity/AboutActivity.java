package com.varunjoshi.notesy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.varunjoshi.notesy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.cketti.mailto.EmailIntentBuilder;

public class AboutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.version)
    TextView mVersion;
    @BindView(R.id.spinner)
    Spinner mSpinner;
    @BindView(R.id.sendEmail)
    Button mSendEmail;

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        mVersion.setText("Version: " + Constant.appVersion);
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Say Hello!");
        categories.add("Send feedback");
        categories.add("Suggest a feature");
        categories.add("Report an issue");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item;
        pos = position;
        item = (String) parent.getItemAtPosition(position);
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick(R.id.sendEmail)
    public void onViewClicked() {
        Intent emailIntent = null;
        switch (pos){
            case 0:
                emailIntent = EmailIntentBuilder.from(this)
                        .to("joshvarun@gmail.com")
                        .subject("Hello!")
                        .build();
                break;
            case 1:
                emailIntent = EmailIntentBuilder.from(this)
                        .to("joshvarun@gmail.com")
                        .subject("Feedback")
                        .build();
                break;
            case 2:
                emailIntent = EmailIntentBuilder.from(this)
                        .to("joshvarun@gmail.com")
                        .subject("Hey! I need a feature!")
                        .build();
                break;
            case 3:
                emailIntent = EmailIntentBuilder.from(this)
                        .to("joshvarun@gmail.com")
                        .subject("Hey! I found an issue!")
                        .build();
                break;
        }
        startActivity(emailIntent);
    }
}
