package com.example.davidzhu.beacon;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class search extends AppCompatActivity {
    private RecyclerView mCardListView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private final int numOfCards = 3;
    private static final  int SCROLL_DIRECTION_UP = -1;

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



        mCardListView = (RecyclerView) findViewById(R.id.search_card_list);
        mCardListView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mCardListView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new SearchAdapter(numOfCards);
        mCardListView.setAdapter(mAdapter);


        // need to elevate an app bar to make it look Google Maps like
        // add on scroll listener to the list to show/remove divider
        mCardListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mCardListView.canScrollVertically(SCROLL_DIRECTION_UP)) {
                    findViewById(R.id.search_app_bar).setElevation(10);
                } else {
                    findViewById(R.id.search_app_bar).setElevation(0);

                }
            }

        });

    }

}
