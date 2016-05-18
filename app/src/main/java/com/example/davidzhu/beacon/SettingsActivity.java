package com.example.davidzhu.beacon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseUser;

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

        ParseUser user = ParseUser.getCurrentUser();

        mobilePhone = (EditText) findViewById(R.id.mobile_number);
        workPhone = (EditText) findViewById(R.id.work_number);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.settings_address_text);
        website = (EditText) findViewById(R.id.settings_web_text);

        if (user.get("phone") != null)
            mobilePhone.setText((CharSequence) user.get("phone"));

        if (user.get("workPhone") != null)
            workPhone.setText((CharSequence) user.get("workPhone"));

        if (user.get("email") != null)
            email.setText((CharSequence) user.get("email"));

        if (user.get("address") != null)
            address.setText((CharSequence) user.get("address"));

        if (user.get("website") != null)
            website.setText((CharSequence) user.get("website"));

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
            ParseUser user = ParseUser.getCurrentUser();
            switch (v.getId()) {
                case R.id.mobile_number:
                    user.put("phone", mobilePhone.getText().toString());
                    break;
                case R.id.work_number:
                    user.put("workPhone", workPhone.getText().toString());
                    break;
                case R.id.email:
                    user.put("email", email.getText().toString());
                    break;
                case R.id.settings_address_text:
                    user.put("address", email.getText().toString());
                    break;
                case R.id.settings_web_text:
                    user.put("website", website.getText().toString());
                    break;
            }
            user.saveInBackground();
        }
    }
}
