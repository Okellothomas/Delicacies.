package com.pro.delicacy.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.pro.delicacy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class login extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = login.class.getSimpleName();

    @BindView(R.id.registerNow) TextView mRegisterNow;
    @BindView(R.id.passWord) EditText mPassWord;
    @BindView(R.id.emailEdit) EditText mEmailEdit;
    @BindView(R.id.loginText) TextView mLoginText;

    private FirebaseAuth mAuthenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuthenticator = FirebaseAuth
                .getInstance();

        mRegisterNow.setOnClickListener(this);
        mLoginText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mRegisterNow){
            Intent intent = new Intent(login.this, Create.class);
            startActivity(intent);
            finish();
        }

        if (v == mLoginText){
            userLoggedIn();
        }
    }

    private void userLoggedIn() {

    }
}