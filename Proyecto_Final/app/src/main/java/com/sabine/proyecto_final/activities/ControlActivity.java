package com.sabine.proyecto_final.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sabine.proyecto_final.R;

public class ControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Button btnRegisterPaymentActivity = findViewById(R.id.btnRegisterPaymentActivity);
        Button btnRegisterPurchaseActivity = findViewById(R.id.btnRegisterPurchaseActivity);
        Button btnRegisterActivity = findViewById(R.id.btnRegisterActivity);
        Button btnOnBoardingActivity = findViewById(R.id.btnOnBoardingActivity);
        Button btnProfileActivity = findViewById(R.id.btnProfileActivity);
        Button btnLoginSuccessActivity = findViewById(R.id.btnLoginSuccessActivity);
        Button btnChangePasswordActivity = findViewById(R.id.btnChangePasswordActivity);
        Button btnLoginActivity = findViewById(R.id.btnLoginActivity);
        Button btnEditProfileActivity = findViewById(R.id.btnEditProfileActivity);
        Button btnHomeActivity = findViewById(R.id.btnHomeActivity);
        Button btnregisterCardActivity = findViewById(R.id.btnregisterCardActivity);

        btnRegisterPaymentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, RegisterPaymentActivity.class));
            }
        });

        btnRegisterPurchaseActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, RegisterPurchaseActivity.class));
            }
        });

        btnRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, RegisterActivity.class));
            }
        });

        btnOnBoardingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, OnBoardingActivity.class));
            }
        });

        btnProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, ProfileActivity.class));
            }
        });

        btnLoginSuccessActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, LoginSuccessActivity.class));
            }
        });

        btnChangePasswordActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, ChangePasswordActivity.class));
            }
        });

        btnLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, LoginActivity.class));
            }
        });

        btnEditProfileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, EditProfileActivity.class));
            }
        });

        btnHomeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, HomeActivity.class));
            }
        });


        btnregisterCardActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlActivity.this, RegisterCardActivity.class));
            }
        });
    }
}
