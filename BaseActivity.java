package com.sachet.flickrbrowser;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {
    /*We start by creating a base class BaseActivity that all of the other three apps are going to share*/
    /*As we know by defining common methods in a single class and having all other activities extend it, they
     get access to everything that is defined in the base class.That means we dont have to define same
     methods in every other activity.*/
    /*In this app we may need to send datas between the activities, such as when we create photodetail activity we will send the
    * details of photo so created to the activities*/
    private static final String TAG = "BaseActivity";

    /*The method created in this activity will show a toolbar, and it will allow an activity tto choose wether the toolbar should have the
    * home button showing or not*/
    public static final String FLICKR_QUERY = "FLICKR_QUERY";
    public static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    void activateToolBar(boolean enable){
        Log.d(TAG, "activateToolBar: starts");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar==null){
            Toolbar toolbar = findViewById(R.id.toolbar);

            if(toolbar!=null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(enable);
        }
    }
    /*All the method does is use the get support action bar method to get reference to action bar so that we can add to it. Now provided there
     is an action bar, we inflate the toolbar fron toolbar xml file, then we used set support action bar method with our inflated toolbar to put the
     toolbar in place at the top of the screen*/
}
