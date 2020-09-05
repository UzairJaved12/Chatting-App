package com.afa.chattingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afa.chattingsystem.databinding.ActivityLoginBinding;
import com.afa.chattingsystem.databinding.ActivityMainactivityBinding;

public class Mainactivity extends AppCompatActivity {
    public static String user = "user";
    ActivityMainactivityBinding activityMainactivityBinding;

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
    public void onStart(){
        super.onStart();
        TextView view=(TextView)findViewById(R.id.txtuser);
        view.setText("Welcome "+user);
    }
    public void logout() {
        SharedPreferences sharedPrefs = getSharedPreferences(Login.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear();
        editor.commit();
        user = "";
        //show login form
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);


    }
}


