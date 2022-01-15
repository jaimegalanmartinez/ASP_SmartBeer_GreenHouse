package com.asp.smartbeergreenhouse.db;

import android.provider.BaseColumns;

/**
 * Login Contract class
 * <p> Defines how the local SQLite DB is set up. Used a local DB only for demo purposes.</p>
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class LoginContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private LoginContract() {}

    /* Inner class that defines the table contents */

    /**
     * UsersTable class
     *
     * <p>Defines the structure of users table:</p>
     * <p>It has three columns apart from id:</p>
     * <p>- username</p>
     * <p>- password</p>
     * <p>- type</p>
     */
    public static class UsersTable implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_TYPE = "type";

    }

}
