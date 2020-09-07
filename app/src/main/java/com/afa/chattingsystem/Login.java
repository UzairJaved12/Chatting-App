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
    private static final String TAG = "Login";

    EditText email,password;
 ActivityLoginBinding activityLoginBinding;
    DatabaseReference reference;
    FirebaseAuth auth;
    Dialog dialog;
    Button submit,cancel;
    String PREFS = "MyPrefs";
    SharedPreferences mPrefs;


    boolean dailogstatus = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = activityLoginBinding.getRoot();
        setContentView(view);

        mPrefs = getSharedPreferences(PREFS, 0);

        auth = FirebaseAuth.getInstance();

        mPrefs=this.getSharedPreferences("PREF_NAME",Context.MODE_PRIVATE);
/*

        if(mPrefs.getBoolean("firstTime", false))
        {
            startActivity(new Intent(this,Mainactivity.class));
            finish();
        }

*/


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

        activityLoginBinding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,GoogleLogin.class);
                startActivity(intent);
            }
        });
        activityLoginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_email = activityLoginBinding.loginEmail.getText().toString();
                String txt_password = activityLoginBinding.loginPassword.getText().toString();



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

                boolean rememberMe = mPrefs.getBoolean("rememberMe", false);
                if(rememberMe == true){
                    //get previously stored login details
                    String login = mPrefs.getString("login", null);
                    String upass = mPrefs.getString("password", null);

                    if(login != null && upass != null){
                        //fill input boxes with stored login and pass
                        EditText loginEbx = (EditText)findViewById(R.id.login_email);
                        EditText passEbx = (EditText)findViewById(R.id.login_password);
                        loginEbx.setText(login);
                        passEbx.setText(upass);

                        //set the check box to 'checked'
                        CheckBox rememberMeCbx = (CheckBox)findViewById(R.id.remember);
                        boolean isChecked = rememberMeCbx.isChecked();
                        if(isChecked){
                            saveLoginDetails();
                            Log.d(TAG, "onClick: "+isChecked);
                            
                        }else{
                            removeLoginDetails();
                            Log.d(TAG, "onClick: ");
                        }
                    }
                }
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

    @Override
    public void onBackPressed() {
        if (dailogstatus){
            Log.e(TAG, "onBackPressed: "+dailogstatus );
           dialog.dismiss();
        }else{
            super.onBackPressed();
        }

    }

    private void saveLoginDetails(){
        //fill input boxes with stored login and pass
        EditText loginEbx = (EditText)findViewById(R.id.login_email);
        EditText passEbx = (EditText)findViewById(R.id.login_password);
        String login = loginEbx.getText().toString();
        String upass = passEbx.getText().toString();

        SharedPreferences.Editor e = mPrefs.edit();
        e.putBoolean("rememberMe", true);
        e.putString("login", login);
        e.putString("password", upass);
        e.commit();
    }
    private void removeLoginDetails(){
        SharedPreferences.Editor e = mPrefs.edit();
        e.putBoolean("rememberMe", false);
        e.remove("login");
        e.remove("password");
        e.commit();
    }

  /*  public void getUser(){
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
                .putBoolean("simplelogin",true)
                .apply();
    }

    public void showLogout(String username){
        //display log out activity
        Intent intent=new Intent(this, Mainactivity.class);
        intent.putExtra("user",username);
        startActivity(intent);*/
    }
