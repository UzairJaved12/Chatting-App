package com.afa.chattingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afa.chattingsystem.databinding.ActivityFacebookLoginBinding;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;

public class FacebookLogin extends AppCompatActivity {
    private static final String TAG = "FacebookLogin";

    private CallbackManager callbackManager;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    ActivityFacebookLoginBinding activityFacebookloginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_facebooklogin);
        activityFacebookloginBinding = ActivityFacebookLoginBinding.inflate(getLayoutInflater());
        View view = activityFacebookloginBinding.getRoot();
        setContentView(view);


        auth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());


        Log.e(TAG, "onCreate: ");
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        activityFacebookloginBinding.loginButton.setReadPermissions("user_friends");
        activityFacebookloginBinding.loginButton.registerCallback(callbackManager, callback);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                Log.e(TAG, "onCurrentAccessTokenChanged: ");
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                Log.e(TAG, "onCurrentProfileChanged: ");
                // displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


/*
    private void displayMessage(Profile profile) {
        Log.e(TAG, "displayMessage: ");
        if (profile != null) {
            activityFacebookloginBinding.textView.setText("Graph id: " + profile.getId());
            Log.d(TAG, "First name: " + profile.getFirstName());
            Log.d(TAG, "Last name: " + profile.getLastName());
            Log.d(TAG, " name: " + profile.getId());

            Uri url = Profile.getCurrentProfile().getProfilePictureUri(256, 256);
            Glide.with(this)
                    .load(url)
                    .circleCrop()
                    .into(activityFacebookloginBinding.imageView);

        }
    }
*/


    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        //displayMessage(profile);
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            handleFacebook(loginResult.getAccessToken());
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel: ");

        }

        @Override
        public void onError(FacebookException e) {

            Log.d(TAG, "onError: " + e);

        }

    };


    private void handleFacebook(AccessToken token) {
        Log.d(TAG, "handleFacebook: " + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Sign: ");
                    FirebaseUser user = auth.getCurrentUser();
                    updateUI(user);
                } else {
                    Log.d(TAG, "Sign with auth:failure ", task.getException());
                    Toast.makeText(FacebookLogin.this, "Auth is failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);


                }
            }
        });


    }

    @SuppressLint("ResourceType")
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            activityFacebookloginBinding.textView.setText(user.getDisplayName());
            if (user.getPhotoUrl() != null) {
                Uri url = Profile.getCurrentProfile().getProfilePictureUri(256, 256);
                Glide.with(this)
                        .load(url)
                        .circleCrop()
                        .into(activityFacebookloginBinding.imageView);
            } else {
                activityFacebookloginBinding.textView.setText("");
                activityFacebookloginBinding.imageView.setImageResource(R.id.image_view);
            }
        }
    }


    public void onStart() {
        super.onStart();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            }
        };


    }
  /*  public void onStop() {
        super.onStop();
        if (authStateListener !=null){
            auth.removeAuthStateListener(authStateListener);
        }
    }*/

}