package com.asp.smartbeergreenhouse.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.asp.smartbeergreenhouse.R;
import com.asp.smartbeergreenhouse.databinding.ActivityLoginBinding;
import com.asp.smartbeergreenhouse.databinding.ActivitySignUpBinding;
import com.asp.smartbeergreenhouse.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button signupButton = binding.welcomeSignupBtn;
        Button loginButton = binding.welcomeLoginBtn;

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WelcomeActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });



    }
}