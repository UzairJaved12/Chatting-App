package com.afa.chattingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afa.chattingsystem.databinding.ActivityLoginBinding;
import com.afa.chattingsystem.databinding.ActivityMainactivityBinding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Mainactivity extends AppCompatActivity {
    private static final String TAG = "Mainactivity";
    public static String user = "user";
    ActivityMainactivityBinding activityMainactivityBinding;
    SharedPreferences mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainactivityBinding = ActivityMainactivityBinding.inflate(getLayoutInflater());
        View view = activityMainactivityBinding.getRoot();
        setContentView(view);


        activityMainactivityBinding.btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

   public void logout() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();




    }
}


