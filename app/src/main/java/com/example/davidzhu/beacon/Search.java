package com.example.davidzhu.beacon;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> mUserTagsNames = new ArrayList<String>();
    ArrayList<String> mDefaultTagsNames = new ArrayList<String>();

    ExpandableListView expListView;

    ParseUser mUser;
    SearchIconLoader il = new SearchIconLoader();


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

        mUser = ParseUser.getCurrentUser();
        List<String> savedTags = mUser.getList("savedTags");
        for (String temp : savedTags) {
            mUserTagsNames.add(temp);
        }

        createGroupList();

        expListView = (ExpandableListView) findViewById(R.id.search_exp_tags_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, mUserTagsNames);
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
        SearchRecycleViewAdapter srva = new SearchRecycleViewAdapter(mDefaultTagsNames, mUserTagsNames,expListAdapter);
        rv.setAdapter(srva);
    }

    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first

        mUser.remove("savedTags");
        mUser.addAllUnique("savedTags", mUserTagsNames);
        mUser.saveInBackground();
    }


    private void createGroupList() {
        mDefaultTagsNames.add(getResources().getString(R.string.food));
        mDefaultTagsNames.add(getResources().getString(R.string.free));
        mDefaultTagsNames.add(getResources().getString(R.string.fun));
        mDefaultTagsNames.add(getResources().getString(R.string.more_cat));
    }


}
