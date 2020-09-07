package com.afa.chattingsystem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class BaseActivity extends AppCompatActivity {

    /**
     * Permissions Properties
     **/
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;

    public void Snackbar(String msg){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}