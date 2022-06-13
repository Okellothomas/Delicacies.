package com.pro.delicacy.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pro.delicacy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class login extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.registerNow) TextView mRegisterNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mRegisterNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mRegisterNow){
            Intent intent = new Intent(login.this, Create.class);
            startActivity(intent);
            finish();
        }
    }
}