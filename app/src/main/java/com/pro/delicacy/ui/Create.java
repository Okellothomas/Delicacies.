package com.pro.delicacy.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.pro.delicacy.Delicacies;
import com.pro.delicacy.R;

import java.util.Objects;

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
    @BindView(R.id.firebaseProgress) ProgressBar mMyProgessBar;
    @BindView(R.id.loginProgressText) TextView mloadingMessage;

    private String mName;

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

    private void ProgressBar(){
        mMyProgessBar.setVisibility(View.VISIBLE);
        mloadingMessage.setVisibility(View.VISIBLE);
        mloadingMessage.setText("Wait as we sign you in.");
    }

    private void hideProgressBar(){
        mMyProgessBar.setVisibility(View.GONE);
        mloadingMessage.setVisibility(View.GONE);
    }

    private void createAUser() {
        final String name = mNameEdit.getText().toString().trim();
        final String email = mEmailEdit.getText().toString().trim();
        String passWord = mPassWord.getText().toString().trim();
        String confirmPassWord = mConfirmPassWord.getText().toString().trim();
        mName = mNameEdit.getText().toString().trim();

        // implement the validation
        boolean validName = isValidName(mName);
        boolean validUserEmail = isValidEmail(email);
        boolean validUserName = isValidName(name);
        boolean validUserPassword = isValidPassWord(passWord, confirmPassWord);
        if (!validUserName || !validUserEmail || !validUserPassword) return;

        ProgressBar();

        mAuthenticate.createUserWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(this, task -> {
                    hideProgressBar();
                    if (task.isSuccessful()){
                        Toast.makeText(Create.this, "Authentication successful.", Toast.LENGTH_LONG).show();
                        createUserProfile(Objects.requireNonNull(task.getResult().getUser()));
                    } else {
                        Toast.makeText(Create.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void createUserProfile(final FirebaseUser user){
        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName)
                .build();
        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Toast.makeText(Create.this, "Your profile name has been set", Toast.LENGTH_LONG).show();
                       }
                    }
                });
    }

    private boolean isValidEmail(String email){
        boolean validEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!validEmail){
            mEmailEdit.setError("Please enter a valid email address");
            return false;
        }
        return validEmail;
    }

    private boolean isValidName(String name){
        if (name.equals("")){
            mNameEdit.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassWord(String password, String confirmPassword){
        if (password.length()<6){
            mPassWord.setError("Please create a password containing at 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)){
            mPassWord.setError("Passwords do not match");
            return false;
        }
        return true;
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