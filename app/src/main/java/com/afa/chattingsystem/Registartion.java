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
import android.widget.Toast;

import com.afa.chattingsystem.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registartion extends AppCompatActivity {
    //Refernece  https://www.youtube.com/watch?v=b9nNm-xxmOY

    private static final String TAG = "MainActivity";
   EditText username,email,password;
   FirebaseAuth auth;
   DatabaseReference reference;
   ActivityRegisterBinding activityRegisterBinding;
   SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = activityRegisterBinding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();



        activityRegisterBinding.registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPreferences = getSharedPreferences("MyPREFER", Context.MODE_PRIVATE);

                        String txt_username = username.getText().toString();
                        String txt_email = email.getText().toString();
                        String txt_password = password.getText().toString();



                        if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {

                            Toast.makeText(Registartion.this, "All fields are required", Toast.LENGTH_SHORT).show();
                        } else if (txt_password.length() < 6) {
                            Toast.makeText(Registartion.this, "password must be atleast 6 character", Toast.LENGTH_SHORT).show();

                        } else {
                            register(txt_username, txt_email, txt_password);
                        }

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(txt_username +txt_email + txt_password + "data" ,  txt_email +"/n" + txt_email);
                        editor.commit();

                    }
                });


            }
            //register in firebase

            private void register(final String username, String email, String password) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isCanceled()) {
                                        Intent intent = new Intent(Registartion.this, Login.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(Registartion.this, "You cannot register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
    }