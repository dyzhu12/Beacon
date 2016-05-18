package com.example.davidzhu.beacon;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class MyCreatedBeaconsActivity extends AppCompatActivity {
    private TextView itemTextView;
    private ArrayAdapter<String> adapter;

    //fake data for testing purposes
    private ArrayList<String> testArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_created_beacons);

        //fake data
//      populateFakeData();
//

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ArrayList<String> mCreatedBeaconsArray = new ArrayList<String>();
        // final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.beacon_list_item, R.id.list_item_textview, testArray.toArray());
        final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.beacon_list_item, R.id.list_item_textview, mCreatedBeaconsArray.toArray());

        ListView myCreatedLv = (ListView) findViewById(R.id.list_view_my_created_beacons);
        myCreatedLv.setAdapter(adapter);

        myCreatedLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = testArray.get(position);
//                testArray.remove(position);
                adapter.notifyDataSetChanged();
//                System.out.println("removed " + item);
            }
        });

        ParseUser user = ParseUser.getCurrentUser();
        String username =  user.getUsername();
        System.out.println("I am the user and my name is: " + username);

        final ParseQuery query = ParseUser.getQuery();

        query.whereEqualTo("username", user.getUsername()); //restricts query to just the current user

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) { //if no exception
                    //System.out.println("here!");
                    //for each beacon in the objects List
                    int size = results.size(); //should be 1. should only have 1 current user

                    System.out.println("size of <ParseObjects> list = " + size);

                    for (ParseObject result : results) { //result is a single user (the current user)
                        List<String> list_of_beacon_id_strings = result.getList("created"); //accesses the array of beacon id's
                        int list_size = list_of_beacon_id_strings.size();
                        System.out.println("list_size is: " + list_size);

                        for (String beacon_id_str : list_of_beacon_id_strings){ //access individual beacons using beacon id string

                        }

//                        Beacon beacon = (Beacon) result;
//                        String myCreatedBeaconName = beacon.getDisplayName();
//                        adapter.add(myCreatedBeaconName);
//                        System.out.println("added myCreatedBeaconName, yay!");
                    }
                   // System.out.println("here! #2");
                } else {//Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show()

                }
            }
        });




    }

//
//        //TODO replace testArray with database
//
//        myCreatedLv.setAdapter(adapter);
//
//        //causes null pointer exception :O
//        myCreatedLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = testArray.get(position);
//                testArray.remove(position);
//                adapter.notifyDataSetChanged();
//                System.out.println("removed " + item);
//            }
//        });
//   }

    public void populateFakeData(){
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
    }
}
