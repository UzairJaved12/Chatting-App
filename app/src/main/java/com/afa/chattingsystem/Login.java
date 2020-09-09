package com.afa.chattingsystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afa.chattingsystem.databinding.ActivityLoginBinding;
import com.afa.chattingsystem.prefrence.PrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";

    EditText email, password;
    ActivityLoginBinding activityLoginBinding;
    DatabaseReference reference;
    FirebaseAuth auth;
    Dialog dialog;
    Button submit, cancel;
    String PREFS = "MyPrefs";
    SharedPreferences mPrefs;


    boolean dailogstatus = false;


    private CheckBox checkBoxRememberMe;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = activityLoginBinding.getRoot();
        setContentView(view);

        Log.d(TAG, "onCreate: ");

        mPrefs = getSharedPreferences(PREFS, 0);

        auth = FirebaseAuth.getInstance();


        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);



        //Here we will validate saved preferences
        if (!new PrefManager(this).isUserLogedOut()) {
            //user's email and password both are saved in preferences
            SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
            String email = sharedPreferences.getString("Email", "");
            String password = sharedPreferences.getString("Password", "");
            activityLoginBinding.loginEmail.setText(email);
            activityLoginBinding.loginPassword.setText(password);
            startHomeActivity();
            Log.d(TAG, "startHomeActivity: ");
        }


        activityLoginBinding.OtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, CreateOtp.class);
                startActivity(intent);
            }
        });

        activityLoginBinding.btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, FacebookLogin.class);
                startActivity(intent);
            }
        });

        activityLoginBinding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, GoogleLogin.class);
                startActivity(intent);
            }
        });


        activityLoginBinding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registartion.class);
                startActivity(intent);

            }
        });

        activityLoginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: ");
                final String txt_email = activityLoginBinding.loginEmail.getText().toString();
                final String txt_password = activityLoginBinding.loginPassword.getText().toString();

                Log.d(TAG, "txt_email: "+txt_email);
                Log.d(TAG, "txt_password: "+txt_password);

                //Firebase auth in email
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {

                    Toast.makeText(Login.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: ");
                                // save data in local shared preferences
                                if (checkBoxRememberMe.isChecked())
                                    Log.d(TAG, "checkBoxRememberMe: ");

                                    saveLoginDetails(txt_email, txt_password);
                                startHomeActivity();

                            } else {
                                Toast.makeText(Login.this, "Auth is failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        activityLoginBinding.forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


    }

    private void showDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(R.layout.forgetpassword);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        dailogstatus = true;


        submit = dialog.findViewById(R.id.btn_submit);
        cancel = dialog.findViewById(R.id.cancel);
        email = dialog.findViewById(R.id.submit_email);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                if (TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(Login.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Plaease check your email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, Login.class));
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(Login.this, "Error" + message, Toast.LENGTH_SHORT).show();
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
        if (dailogstatus) {
            Log.e(TAG, "onBackPressed: " + dailogstatus);
            dialog.dismiss();
        } else {
            super.onBackPressed();
        }

    }


    private void startHomeActivity() {
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Authentication");
        progressDialog.show();
        Intent intent = new Intent(this, Mainactivity.class);
        startActivity(intent);

    }

    private void saveLoginDetails(String email, String password) {

        Log.d(TAG, "saveLoginDetails: "+email+password);
        new PrefManager(this).saveLoginDetails(email, password);
    }

}
