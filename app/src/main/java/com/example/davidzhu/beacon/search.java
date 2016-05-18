package com.example.davidzhu.beacon;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private RecyclerView.LayoutManager mLayoutManager;

    String headerName = "";
    ArrayList<String> childList = new ArrayList<String>();

    ArrayList<String> items = new ArrayList<String>();
    ArrayList<Integer> itemsImg = new ArrayList<Integer>();

    ExpandableListView expListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_app_bar).findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // when creating an activity, get the search field in focus and retract the keyboard
        EditText searchField = (EditText) findViewById(R.id.search_field);
        searchField.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT);

        createGroupList();


        expListView = (ExpandableListView) findViewById(R.id.search_exp_tags_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, headerName, childList);
        expListView.setAdapter(expListAdapter);

        expListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();

                return true;
            }
        });


        RecyclerView rv = (RecyclerView) findViewById(R.id.search_nearby);
        rv.setHasFixedSize(true);
        mLayoutManager = new CustomLinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);
        SearchRecycleViewAdapter srva = new SearchRecycleViewAdapter(items,itemsImg,10,childList,expListAdapter);
        rv.setAdapter(srva);
    }


    private void createGroupList() {
        childList.add("BOO_1");
        childList.add("BOO_2");
        childList.add("BOO_3");
        childList.add("BOO_4");
        childList.add("BOO_5");

        items.add(getResources().getString(R.string.food));
        items.add(getResources().getString(R.string.free));
        items.add(getResources().getString(R.string.fun));
        items.add(getResources().getString(R.string.more_cat));


        itemsImg.add(R.drawable.ic_local_dining_black_24dp);
        itemsImg.add(R.drawable.ic_local_atm_black_24dp);
        itemsImg.add(R.drawable.ic_insert_emoticon_black_24dp);

    }



}
