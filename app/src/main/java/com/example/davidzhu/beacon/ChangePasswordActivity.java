package com.example.davidzhu.beacon;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by David Zhu on 5/17/2016.
 */
public class ChangePasswordActivity extends AppCompatActivity implements View.OnKeyListener {

    private EditText oldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassword = (EditText) findViewById(R.id.old_password);

        oldPassword.requestFocus();
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(oldPassword, 0);

        oldPassword.setOnKeyListener(this);
    }


    private void checkPassword(String text) {
        // Filler
        if (text.equals("abc")) {
            changePassword();
        } else {
            Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
        }
    }

    private void changePassword() {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {
            checkPassword(((EditText) v).getText().toString());
            return true;
        }
        return false;
    }
}
