package com.afa.chattingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afa.chattingsystem.databinding.ActivityLoginBinding;
import com.afa.chattingsystem.databinding.ActivityResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    ActivityResetPasswordBinding activityResetPasswordBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResetPasswordBinding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        View view = activityResetPasswordBinding.getRoot();
        setContentView(view);
        /*auth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.submit_email);
        activityResetPasswordBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                if (TextUtils.isEmpty(txt_email)){
                    Toast.makeText(ResetPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }else {
                    auth.sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPassword.this, "Plaease resent the password", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPassword.this,Login.class));
                            }else {
                                String message = task.getException().getMessage();
                                Toast.makeText(ResetPassword.this, "Error"+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();*/

    }
}