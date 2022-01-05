package com.asp.smartbeergreenhouse.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asp.smartbeergreenhouse.databinding.ActivityLoginBinding;
import com.asp.smartbeergreenhouse.db.DBHelper;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private DBHelper myDB;
    private TextWatcherLogin textWatcherLogin;
    private EditText editUsername;
    private EditText editPassword;
    private Button loginBtn;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button signupBtn = binding.loginSignupBtn;
        loginBtn = binding.loginSubmitBtn;
        editUsername = binding.loginTextInputUsername.getEditText();
        editPassword = binding.loginTextInputPassword.getEditText();

        myDB = new DBHelper(this);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean checkCredentials = myDB.checkUsernamePassword(username, password);
                if (checkCredentials){
                    String userType = myDB.readUserType(username);

                    if(userType != null){
                        Log.d("LOGININFO","User type read from DB: "+userType);
                        if (userType.equals("Farmer")){
                            Log.d("LOGININFO","User type DB: "+userType);
                            Intent i = new Intent(LoginActivity.this, FarmerActivity.class);
                            startActivity(i);
                            finish();
                        }else if(userType.equals("Brewery")){
                            Log.d("LOGININFO","User type DB: "+userType);
                            Intent i = new Intent(LoginActivity.this, BreweryActivity.class);
                            startActivity(i);
                            finish();
                        }

                    }
                }else{ //User not found
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });

        textWatcherLogin = new TextWatcherLogin();
        //EditTexts listeners
        editUsername.addTextChangedListener(textWatcherLogin);
        editPassword.addTextChangedListener(textWatcherLogin);

        loginBtn.setEnabled(false);
    }

    public boolean isLoginNotEmpty(){
        boolean isValid = false;
        //If edit text fields are not empty
        if(!(TextUtils.isEmpty(editUsername.getText().toString())) &&
                !(TextUtils.isEmpty(editPassword.getText().toString()))){
                    isValid = true;

        }
        return isValid;
    }

    private class TextWatcherLogin implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable == editUsername.getEditableText()) {
                if (TextUtils.isEmpty(editUsername.getText().toString())) {
                    editUsername.setError("Enter your username");
                } else {
                    //Trim to remove leading and trailing spaces
                    username = editUsername.getText().toString().trim();
                    Log.d("LOGININFO", username);
                }

            } else if (editable == editPassword.getEditableText()) {
                if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    editPassword.setError("Enter your password");
                } else {
                    password = editPassword.getText().toString().trim();
                    Log.d("LOGININFO", password);
                }

            }
            if(isLoginNotEmpty()){
                loginBtn.setEnabled(true);
            }else{
                loginBtn.setEnabled(false);
            }

        }
    }
}