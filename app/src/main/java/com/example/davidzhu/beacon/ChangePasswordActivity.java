package com.example.davidzhu.beacon;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
        View view = findViewById(R.id.password_container);
        ViewGroup parent = (ViewGroup) view.getParent();

        int index = parent.indexOfChild(view);
        parent.removeView(view);

        view = getLayoutInflater().inflate(R.layout.content_change_password, parent, false);
        parent.addView(view, index);

        TextView newPassword = (TextView) findViewById(R.id.new_password);
        TextView confirmPassword = (TextView) findViewById(R.id.confirm_password);

        newPassword.setTypeface(Typeface.DEFAULT);
        confirmPassword.setTypeface(Typeface.DEFAULT);
        confirmPassword.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {

            if (v.getId() == R.id.old_password) {
                checkPassword(((EditText) v).getText().toString());
                return true;
            }
            if (v.getId() == R.id.confirm_password) {
                confirmPassword(((EditText) v).getText().toString());
                return true;
            }
        }
        return false;
    }

    private void confirmPassword(String v) {

        String newPasswordValue = ((EditText) findViewById(R.id.new_password)).getText().toString();

        if (v.equals(newPasswordValue)) {
            // Do Parse Password magic here
        } else {
            Toast.makeText(getApplicationContext(), "Password Mismatch - Try Again", Toast.LENGTH_SHORT).show();
        }

    }
}
