package com.example.davidzhu.beacon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Asus on 5/17/2016.
 */
public class NotificationsActivity extends AppCompatActivity {

    private Spinner distanceCompareSpinner;
    private Spinner distanceValueSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        distanceCompareSpinner = (Spinner) findViewById(R.id.distance_compare);
        distanceValueSpinner = (Spinner) findViewById(R.id.distance_value);

        ArrayAdapter<CharSequence> compareAdapter = ArrayAdapter.createFromResource(this, R.array.distance_compare_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> valueAdapter = ArrayAdapter.createFromResource(this, R.array.distance_value_array, android.R.layout.simple_spinner_item);

        compareAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceCompareSpinner.setAdapter(compareAdapter);

        valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceValueSpinner.setAdapter(valueAdapter);


    }
}
