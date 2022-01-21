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

/**
 * Login Activity
 *
 * Shows the login UI screen
 *
 * <p>The user that wants to log in the application needs to fill the following fields:</p>
 * <p>- Username</p>
 * <p>- Password</p>
 *
 * <p>Once the user finished to enter all the fields,
 * the login button will be enabled for the login the user in the app.</p>
 *
 * <p> If the credentials provided by the user are wrong, it will appear a message in the UI indicating that are invalid -> "Invalid credentials".</p>
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Represents the View binding of the Login Activity
     */
    private ActivityLoginBinding binding;
    /**
     *  Represents the DB Helper for manage DB operations (reading and writing in the DB)
     */
    private DBHelper myDB;
    /**
     * TextWatcher used to detect if the login fields are empty.
     * <p>If all fields are filled, the variables (username and password) will be updated, and enable the login button</p>
     */
    private TextWatcherLogin textWatcherLogin;
    /**
     * Represents the EditText for entering the username for login a user
     */
    private EditText editUsername;
    /**
     * Represents the EditText for entering the password for login a user
     */
    private EditText editPassword;
    /**
     * Represents the Button used for login a user
     */
    private Button loginBtn;
    //Variables
    /**
     * Username selected for the user login
     */
    private String username;
    /**
     * Password selected for the user login
     */
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginBtn = binding.loginSubmitBtn;
        editUsername = binding.loginTextInputUsername.getEditText();
        editPassword = binding.loginTextInputPassword.getEditText();

        myDB = new DBHelper(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean checkCredentials = myDB.checkUsernamePassword(username, password);
                if (checkCredentials){
                    String userType = myDB.readUserType(username);

                    if(userType != null){
                        Log.d("LOGIN_INFO","User type read from DB: "+userType);
                        if (userType.equals("Farmer")){
                            Log.d("LOGIN_INFO","User type DB: "+userType);
                            Intent i = new Intent(LoginActivity.this, FarmerActivity.class);
                            startActivity(i);
                            finish();
                        }else if(userType.equals("Brewery")){
                            Log.d("LOGIN_INFO","User type DB: "+userType);
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
                    Log.d("LOGIN_INFO", username);
                }

            } else if (editable == editPassword.getEditableText()) {
                if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    editPassword.setError("Enter your password");
                } else {
                    password = editPassword.getText().toString().trim();
                    Log.d("LOGIN_INFO", password);
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