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

/**
 * Sign Up Activity.
 *
 * Shows the sign up UI screen
 * <p>
 *    The user that wants to register in the application needs to fill the following fields:
 * </p>
 *  <p>- Username</p>
 *  <p>- Password</p>
 *  <p>- Confirm password</p>
 *  <p>- Choose if is a farmer or a brewery.</p>
 *  <p>Once the user finished to enter all the fields,
 *  the register button will be enabled for registering the user.</p>
 *
 *  <p>If the user clicks the register button:</p>
 *  <p>- If is a new user, it will appear a message indicating: "Registration successful</p>
 *  <p>- If the user already has an account it will appear a message indicating: "User already exists! Please log in"</p>
 *
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class SignUpActivity extends AppCompatActivity {

    /**
     * Represents the View binding of the Sign Up Activity
     */
    private ActivitySignUpBinding binding;
    //Form UI fields
    /**
     * Represents the EditText for entering the username for register a user
     */
    private EditText editUsername;
    /**
     * Represents the EditText for entering the password for register a user
     */
    private EditText editPassword;
    /**
     * Represents the EditText for entering the password a second time  for register a user
     */
    private EditText editConfirmPassword;
    /**
     * Represents the RadioGroup for choosing the type of user (Farmer or Brewery)
     */
    private RadioGroup radioGroup;
    /**
     * Represents the Button for registering a user.
     */
    private Button registerBtn;
    //Variables
    /**
     * Username selected for the signup of a new user
     */
    private String username;
    /**
     * Password selected for the signup of a new user
     */
    private String password;
    /**
     * Password retyped selected for confirm the password of the signup.
     */
    private String confirmPassword;
    /**
     * Type of user selected for the signup of a new user
     */
    private User.Type typeUser;

    /**
     * TextWatcher used to detect if the sign up fields are empty, password less than 8 characters,
     * passwords does not match.
     * <p>If all fields are valid, the variables (username, password, confirmPassword will be updated, and enable the register button</p>
     */
    private TextWatcherSignUp textWatcherSignUp;
    /**
     *  Represents the DB Helper for manage DB operations (reading and writing in the DB)
     */
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

        //Log in button ("Already have an account? Log in"). Go to LoginActivity
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        /*
          Register button.
          If registration is successful, it will appear the Login Activity.
          If not it wi
          */
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