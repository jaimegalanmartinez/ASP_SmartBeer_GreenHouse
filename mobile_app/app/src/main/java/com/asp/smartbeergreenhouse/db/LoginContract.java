package com.asp.smartbeergreenhouse.db;

import android.provider.BaseColumns;

public class LoginContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private LoginContract() {}

    /* Inner class that defines the table contents */
    public static class UsersTable implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_TYPE = "type";

    }

}
