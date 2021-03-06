package com.example.davidzhu.beacon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;


/**
 * Created by David Zhu on 5/16/2016.
 */
public class MyAccountActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    // Filler values
    private String userEmail;
    private String userName;

    private EditText nameView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);


        ParseUser user = ParseUser.getCurrentUser();
        userEmail = (String) user.get("email");
        userName = (String) user.get("username");

        // Set subtitle to user gmail
        getSupportActionBar().setSubtitle(userEmail);



        nameView = (EditText) findViewById(R.id.real_name);
        nameView.setText(userName);

        nameView.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        if (!hasFocus){
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            saveName(view);
        }
    }

    public void editName(View view) {
        nameView.requestFocus();
        InputMethodManager keyboard = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(nameView, 0);
    }

    // TODO Save the user's name after editing text
    public void saveName(View v) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("username", ((TextView) v).getText().toString());
        user.saveInBackground();
    }

    public void launchChangePassword(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    // Handler for clicking on user settings
    public void launchSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
