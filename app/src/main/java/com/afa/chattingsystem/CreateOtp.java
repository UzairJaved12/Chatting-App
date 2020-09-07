package com.afa.chattingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.afa.chattingsystem.databinding.ActivityCreateBinding;
import com.afa.chattingsystem.databinding.ActivityOtpBinding;
import com.google.firebase.auth.FirebaseAuth;

public class CreateOtp extends AppCompatActivity {

    private Spinner spinner;
    private EditText editText;
    FirebaseAuth auth;
    ActivityCreateBinding activityCreateBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreateBinding = ActivityCreateBinding.inflate(getLayoutInflater());
        View view = activityCreateBinding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();



        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));



        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

                String number = editText.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    activityCreateBinding.editTextPhone.setError("Valid number is required");
                    activityCreateBinding.editTextPhone.requestFocus();
                    return;
                }

                String phonenumber = "+" + code + number;

                Intent intent = new Intent(CreateOtp.this, OtpActivity.class);
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, Mainactivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }


}