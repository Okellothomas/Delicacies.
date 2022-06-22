package com.pro.delicacy.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pro.delicacy.Delicacies;
import com.pro.delicacy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

public class login extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = login.class.getSimpleName();

    private FirebaseAuth.AuthStateListener mAuthenticationListener;

    @BindView(R.id.firebaseProgress) ProgressBar mLoginProgress;
    @BindView(R.id.loadingText) TextView mLoadingText;
    @BindView(R.id.registerNow) TextView mRegisterNow;
    @BindView(R.id.passwordEditText) EditText mPassWord;
    @BindView(R.id.emailEditText) EditText mEmailEdit;
    @BindView(R.id.passwordLogin) TextView mLoginText;
    @BindView(R.id.landscape) TextView mLandscape;
    @BindView(R.id.potrait) TextView mPotrait;

    private FirebaseAuth mAuthenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuthenticator = FirebaseAuth
                .getInstance();

        mAuthenticationListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user !=null){
                    Intent intent = new Intent(login.this, Delicacies.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mRegisterNow.setOnClickListener(this);
        mLoginText.setOnClickListener(this);
        mPotrait.setOnClickListener(this);
        mLandscape.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mRegisterNow){
            Intent intent = new Intent(login.this, Create.class);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            startActivity(intent);
            finish();
        }

        if (v == mLoginText){
            userLoggedIn();
            ProgressBar();
        }

        if (v == mLandscape){
            mPotrait.setVisibility(View.VISIBLE);
            mLandscape.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        if (v == mPotrait){
            mPotrait.setVisibility(View.GONE);
            mLandscape.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void userLoggedIn() {
        String email = mEmailEdit.getText().toString().trim();
        String password = mPassWord.getText().toString().trim();
        if (email.equals("")){
            mEmailEdit.setError("Please enter your email");
            return;
        }
        if (password.equals("")){
            mPassWord.setError("Enter your Password");
            return;
        }

        mAuthenticator.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hidProgress();
                        if (!task.isSuccessful()){
                            Toast.makeText(login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void ProgressBar(){
        mLoginProgress.setVisibility(View.VISIBLE);
    }
    private void hidProgress(){
        mLoginProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuthenticator.addAuthStateListener(mAuthenticationListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mAuthenticator.removeAuthStateListener(mAuthenticationListener);
    }
}