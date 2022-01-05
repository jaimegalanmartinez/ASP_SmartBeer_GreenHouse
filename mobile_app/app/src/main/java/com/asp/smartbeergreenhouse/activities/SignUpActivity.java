package com.asp.smartbeergreenhouse.activities;

import androidx.annotation.NonNull;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.asp.smartbeergreenhouse.databinding.ActivitySignUpBinding;
import com.asp.smartbeergreenhouse.db.DBHelper;
import com.asp.smartbeergreenhouse.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private RadioGroup radioGroup;
    private Button registerBtn;

    private String username;
    private String password;
    private String confirmPassword;
    private User.Type typeUser;

    private TextWatcherSignUp textWatcherSignUp;
    private DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editUsername = binding.signupTextInputUsername.getEditText();
        editPassword = binding.signupTextInputPassword.getEditText();
        editConfirmPassword = binding.signupTextConfirmPassword.getEditText();

        radioGroup = binding.signupFormRadiogroup;
        Button loginButton = binding.signupLoginBtn;
        registerBtn = binding.signupFormRegisterBtn;

        myDB = new DBHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create userProfile object with the information provide by the user
                User user = new User(username, password, typeUser);
                Log.d("USERINFO",user.getName());
                Log.d("USERINFO",user.getPassword());
                Log.d("USERINFO",user.getType().name());

                Boolean userCheckResult = myDB.checkUsername(username);
                if (!userCheckResult){
                    Boolean registrationResult = myDB.insertData(username, password, typeUser);
                    if (registrationResult){
                        Toast.makeText(SignUpActivity.this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                        Intent userRegistration = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(userRegistration);
                    }
                }else{
                    Toast.makeText(SignUpActivity.this, "User already exists.\n Please log in", Toast.LENGTH_SHORT).show();
                }
                //Send the user object using an implicit intent
                /*Intent userData = new Intent(SignUpActivity.this, LoginActivity.class);
                userData.putExtra("userObj", user);
                setResult(RESULT_OK, userData);
                startActivity(userData);
                finish(); //Finish activity and go to Profile Fragment
                */

            }
        });

        if (savedInstanceState != null){
            ArrayList<String> editFieldsRetrieved = savedInstanceState.getStringArrayList("signup_list_editTexts");
            if(editFieldsRetrieved != null){
                editUsername.setText(editFieldsRetrieved.get(0));
                editPassword.setText(editFieldsRetrieved.get(1));
                editConfirmPassword.setText(editFieldsRetrieved.get(2));
            }
        }

        textWatcherSignUp = new TextWatcherSignUp();
        //EditTexts listeners
        editUsername.addTextChangedListener(textWatcherSignUp);
        editPassword.addTextChangedListener(textWatcherSignUp);
        editConfirmPassword.addTextChangedListener(textWatcherSignUp);

        registerBtn.setEnabled(false);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton radioButton = radioGroup.findViewById(checkedId);
                String selectedOption = radioButton.getText().toString();
                switch (selectedOption) {
                    case "Farmer":
                        typeUser = User.Type.Farmer;
                        if(isValidUserInfo()){
                            registerBtn.setEnabled(true);
                        }else{
                            registerBtn.setEnabled(false);
                        }
                        break;

                    case "Brewery":
                        typeUser = User.Type.Brewery;
                        if(isValidUserInfo()){
                            registerBtn.setEnabled(true);
                        }else{
                            registerBtn.setEnabled(false);
                        }
                        break;
                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> editFields = new ArrayList<>();
        editFields.add(editUsername.getText().toString());
        editFields.add(editPassword.getText().toString());
        editFields.add(editConfirmPassword.getText().toString());
        //Save editTexts value to survive orientation changes
        outState.putStringArrayList("signup_list_editTexts",editFields);
    }

    public boolean isValidUserInfo(){
        boolean isValid = false;
        //If edit text fields are not empty
        if(!(TextUtils.isEmpty(editUsername.getText().toString())) &&
                !(TextUtils.isEmpty(editPassword.getText().toString())) &&
                !(TextUtils.isEmpty(editConfirmPassword.getText().toString()))){

            if(radioGroup.getCheckedRadioButtonId() != -1) {
                if(password.equals(confirmPassword))
                    isValid = true;

            }


        }
        return isValid;
    }

    private class TextWatcherSignUp implements TextWatcher {

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
                    Log.d("SIGNINFO", username);
                }

            } else if (editable == editPassword.getEditableText()) {
                if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    editPassword.setError("Enter your password");
                }else if(editPassword.getText().length() < 8){
                    editPassword.setError("Password should have at least 8 characters");
                } else {
                    password = editPassword.getText().toString().trim();
                    Log.d("SIGNINFO", password);
                }

            } else if (editable == editConfirmPassword.getEditableText()) {
                if (TextUtils.isEmpty(editConfirmPassword.getText().toString())) {
                    editConfirmPassword.setError("Re-type your password");

                }else if(editPassword.getText().toString().length() < 8){
                    editConfirmPassword.setError("Password should have at least 8 characters");

                }else if (!editPassword.getText().toString().trim().equals(editConfirmPassword.getText().toString().trim())) {
                    editConfirmPassword.setError("Passwords does not match");
                }else{
                    confirmPassword = editConfirmPassword.getText().toString().trim();
                    Log.d("SIGNINFO", confirmPassword);
                }

            }
            if(isValidUserInfo()){
                registerBtn.setEnabled(true);
            }else{
                registerBtn.setEnabled(false);
            }

        }
    }

}