package com.afa.chattingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afa.chattingsystem.databinding.ActivityGoogleLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleLogin extends AppCompatActivity {

    ActivityGoogleLoginBinding activityGoogleLoginBinding;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN =1;
    private static final String TAG = "GoogleLogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //https://developers.google.com/identity/sign-in/android/start-integrating?authuser=2
        activityGoogleLoginBinding = ActivityGoogleLoginBinding.inflate(getLayoutInflater());
        View view = activityGoogleLoginBinding.getRoot();
        setContentView(view);

        activityGoogleLoginBinding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(GoogleLogin.this,gso);
                googleSignInClient.signOut();

                Toast.makeText(GoogleLogin.this, "Signout", Toast.LENGTH_SHORT).show();
            }
        });

        activityGoogleLoginBinding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleSignInClient =GoogleSignIn.getClient(this, gso);

    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            String name = account.getDisplayName();
            String email = account.getEmail();
            String google = account.getId();
            String token = account.getIdToken();
            Log.d(TAG, name );
            Log.d(TAG, email );
            Log.d(TAG, google );
            Log.d(TAG, token );


            Toast.makeText(this, "Sign", Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }

    }


}
