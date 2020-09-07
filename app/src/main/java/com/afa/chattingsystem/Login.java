package com.afa.chattingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.afa.chattingsystem.databinding.ActivityLoginBinding;
import com.afa.chattingsystem.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.internal.$Gson$Preconditions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {
    private static final String TAG = "v";

    EditText email,password;
 ActivityLoginBinding activityLoginBinding;
    DatabaseReference reference;
    FirebaseAuth auth;
    Dialog dialog;
    Button submit,cancel;
    SharedPreferences sharedPreferences;
    public static String  PREFS_NAME="mypre";
    public static String PREF_EMAIL="email";
    public static String PREF_PASSWORD="password";


    boolean dailogstatus = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = activityLoginBinding.getRoot();
        setContentView(view);


        auth = FirebaseAuth.getInstance();



        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);

        activityLoginBinding.OtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Login.this,CreateOtp.class);
                startActivity(intent);
            }
        });

        activityLoginBinding.btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,FacebookLogin.class);
                startActivity(intent);
            }
        });
        activityLoginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();



                //Firebase auth in email
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){

                    Toast.makeText(Login.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(Login.this, Mainactivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Auth is failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        });




        activityLoginBinding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registartion.class);
                startActivity(intent);

            }
        });


      activityLoginBinding.forget.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              showDialog();
          }
      });

    }
    private  void showDialog(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.forgetpassword);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        dailogstatus = true;


        submit = (Button)dialog.findViewById(R.id.btn_submit);
        cancel = (Button)dialog.findViewById(R.id.cancel);
        email = (EditText)dialog.findViewById(R.id.submit_email);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                if (TextUtils.isEmpty(txt_email)){
                    Toast.makeText(Login.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }else {
                    auth.sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Login.this, "Plaease check your email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this,Login.class));
                            }else {
                                String message = task.getException().getMessage();
                                Toast.makeText(Login.this, "Error"+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });

    }

    public void onStart(){
        super.onStart();
        getUser();
        //read username and password from SharedPreferences
    }
    public void doLogin(){
        EditText txt_email=(EditText)findViewById(R.id.login_email);
        EditText txtpwd=(EditText)findViewById(R.id.login_password);
        String email="myemail";
        String password="mypassword";
        if(txt_email.getText().toString().equals(email) && txtpwd.getText().toString().equals(password)){
            CheckBox ch=(CheckBox)findViewById(R.id.ch_rememberme);
            if(ch.isChecked())
                rememberMe(email,password); //save username and password
            //show logout activity
            showLogout(email);

        }
        else{
            Toast.makeText(this, "Invalid email or password",Toast.LENGTH_LONG).show();
        }


    }
    @Override
    public void onBackPressed() {
        if (dailogstatus){
            Log.e(TAG, "onBackPressed: "+dailogstatus );
           dialog.dismiss();
        }else{
            super.onBackPressed();
        }
    }

    public void getUser(){
        sharedPreferences = getSharedPreferences("MyPREFER", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(PREF_EMAIL, null);
        String password = sharedPreferences.getString(PREF_PASSWORD, null);

        if (email != null || password != null) {
            //directly show logout form
            showLogout(email);
        }
    }

    public void rememberMe(String user, String password){
        //save username and password in SharedPreferences
        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                .edit()
                .putString(PREF_EMAIL,user)
                .putString(PREF_PASSWORD,password)
                .apply();
    }

    public void showLogout(String username){
        //display log out activity
        Intent intent=new Intent(this, Mainactivity.class);
        intent.putExtra("user",username);
        startActivity(intent);
    }
}