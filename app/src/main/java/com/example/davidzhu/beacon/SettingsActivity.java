package com.example.davidzhu.beacon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Asus on 5/17/2016.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private EditText mobilePhone;
    private EditText workPhone;
    private EditText email;
    private EditText address;
    private EditText website;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mobilePhone = (EditText) findViewById(R.id.mobile_number);
        workPhone = (EditText) findViewById(R.id.work_number);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.settings_address_text);
        website = (EditText) findViewById(R.id.settings_web_text);

        mobilePhone.setOnFocusChangeListener(this);
        workPhone.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        address.setOnFocusChangeListener(this);
        website.setOnFocusChangeListener(this);
    }

    @Override
    // TODO add the parse user saveInBackground statements
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.mobile_number:
                    break;
                case R.id.work_number:
                    break;
                case R.id.email:
                    break;
                case R.id.settings_address_text:
                    break;
                case R.id.settings_web_text:
                    break;
            }
        }
    }
}
