package com.afa.chattingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afa.chattingsystem.databinding.ActivityLoginBinding;
import com.afa.chattingsystem.databinding.ActivityResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class DisplayInfo extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    TextView show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        sharedPreferences = getSharedPreferences("MyPREFER", Context.MODE_PRIVATE);
         String display = sharedPreferences.getString("display", "");
         show = (TextView) findViewById(R.id.tv_show);
         show.setText(display);


    }
}