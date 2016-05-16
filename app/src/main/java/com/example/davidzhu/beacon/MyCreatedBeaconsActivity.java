package com.example.davidzhu.beacon;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCreatedBeaconsActivity extends AppCompatActivity {
    //for testing purposes only. Need to connect to database
    private TextView itemTextView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> testArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_created_beacons);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_my_created_beacons);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        testArray = new ArrayList();
        testArray.add("1. Puppies on the Mall");
        testArray.add("2. Coding Party");
        testArray.add("3. Ultimate Frisbee");
        testArray.add("4. Wow It's My 21st Birthday");
        testArray.add("5. Turtle Racing");
        testArray.add("6. Squirrel Watching");
        testArray.add("7. Free Hugs");
        testArray.add("8. Free Pizza");
        testArray.add("9. No Pizza for you");
        testArray.add("10. Google Tech Talk!!");
        testArray.add("11. Ride a camel");
        testArray.add("12. ugh");
        testArray.add("13. test scroll");
        testArray.add("14. are we there yet?");


        ListView lv = (ListView) findViewById(R.id.list_view_my_created_beacons);

        //TODO replace testArray with database
        adapter = new ArrayAdapter(this, R.layout.beacon_list_item, R.id.list_item_textview,testArray.toArray());
        lv.setAdapter(adapter);
    }

    //TODO need to get working with database
    public void deleteItem(View view){
        System.out.println("in OnClickDeleteItem, WHOOO!");
        adapter.notifyDataSetChanged();
    }

}
