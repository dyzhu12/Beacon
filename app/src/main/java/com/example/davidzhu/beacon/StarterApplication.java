/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.example.davidzhu.beacon;

import android.app.Application;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.interceptors.ParseLogInterceptor;


public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        //Parse.initialize(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("beacon")
                        .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                        .addNetworkInterceptor(new ParseLogInterceptor())
//            .server("https://beacon436.herokuapp.com/parse/")
                        .server("http://107.170.46.252:1337/parse/")
                        .build()
        );

        //ParseUser.enableAutomaticUser();
/*    ParseUser user = new ParseUser();
    user.setUsername("my name");
    user.setPassword("my pass");
    user.setEmail("email@example.com");
    user.put("phone", "650-253-0000");



    user.signUpInBackground(new SignUpCallback() {
      public void done(ParseException e) {
        if (e == null) {
          // Hooray! Let them use the app now.
        } else {
          int i = 5;// Sign up didn't succeed. Look at the ParseException
          // to figure out what went wrong

        }
      }
    });
    */



/*        ParseUser.logInInBackground("my name", "my pass", new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });*/

        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


    }
}
