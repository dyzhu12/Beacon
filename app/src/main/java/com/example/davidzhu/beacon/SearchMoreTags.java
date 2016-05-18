package com.example.davidzhu.beacon;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;

public class SearchMoreTags extends AppCompatActivity {

    ArrayList<String> mUserTags = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_more_tags);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RecyclerView rv = (RecyclerView) findViewById(R.id.search_more_tags);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new CustomLinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        ArrayList <String> defaultTags = SearchIconLoader.getAllTags();
        SearchMoreRecycleViewAdapter srva = new SearchMoreRecycleViewAdapter(defaultTags,mUserTags,this);
        rv.setAdapter(srva);

    }




    @Override
    protected void onPause() {
        super.onPause();  // Always call the superclass method first

        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        user.addAllUnique("savedTags", mUserTags);
        user.saveInBackground();
    }

}
