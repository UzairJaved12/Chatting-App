package com.afa.chattingsystem;

import android.os.Build;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

    public void statusbarTheme() {
        //Change status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }

    }
}