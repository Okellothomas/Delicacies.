package com.pro.delicacy.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.pro.delicacy.Delicacies;
import com.pro.delicacy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Create extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = Create.class.getSimpleName();

    private FirebaseAuth mAuthenticate;

    private FirebaseAuth.AuthStateListener mAuthenticateListiner;

    @BindView(R.id.createUser) TextView mCreateUser;
    @BindView(R.id.nameEdit) EditText mNameEdit;
    @BindView(R.id.emailEdit) EditText mEmailEdit;
    @BindView(R.id.passWord) EditText mPassWord;
    @BindView(R.id.confirmPassWord) EditText mConfirmPassWord;
    @BindView(R.id.loginText) TextView mLoginText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);

        mLoginText.setOnClickListener(this);
        mCreateUser.setOnClickListener(this);

        mAuthenticate = FirebaseAuth
                .getInstance();

        createAuthStateListeners();
    }

    private void createAuthStateListeners() {
        mAuthenticateListiner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(Create.this, Delicacies.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginText){
            Intent intent = new Intent(Create.this, login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if (v == mCreateUser){
            createAUser();
        }
    }

    private void createAUser() {
        final String name = mNameEdit.getText().toString().trim();
        final String email = mEmailEdit.getText().toString().trim();
        String passWord = mPassWord.getText().toString().trim();
        String confirmPassWord = mConfirmPassWord.getText().toString().trim();

        mAuthenticate.createUserWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(Create.this, "Authentication successful.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Create.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuthenticate.addAuthStateListener(mAuthenticateListiner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuthenticate.removeAuthStateListener(mAuthenticateListiner);
    }
}