package com.asp.smartbeergreenhouse.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.asp.smartbeergreenhouse.model.User;

/**
 * DB Helper class
 * <p> Helper class to manage the DB operations (writing and reading) of the SQLite DB used.</p>
 * @author Jaime Galan Martinez, Victor Aranda Lopez, Akos Zsolt Becsey.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LoginContract.UsersTable.TABLE_NAME + " (" +
                    LoginContract.UsersTable._ID + " INTEGER PRIMARY KEY," +
                    LoginContract.UsersTable.COLUMN_NAME_USERNAME + " TEXT," +
                    LoginContract.UsersTable.COLUMN_NAME_PASSWORD + " TEXT," +
                    LoginContract.UsersTable.COLUMN_NAME_TYPE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LoginContract.UsersTable.TABLE_NAME;

    /**
     * Constructor of the DBHelper class
     * @param context - Needs the context.
     */
    public DBHelper (Context context){
        super(context, "Login.db", null, 1);

    }

    /**
     * onCreate
     * Creates the table users in the DB
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * onUpgrade
     * Deletes the table users
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
    }

    /**
     * insertData
     * Insert the signup information provided for the user: username, password and type.
     * @param username
     * @param password
     * @param type
     * @return true - If data was inserted otherwise false
     */
    public Boolean insertData(String username, String password, User.Type type){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LoginContract.UsersTable.COLUMN_NAME_USERNAME, username);
        contentValues.put(LoginContract.UsersTable.COLUMN_NAME_PASSWORD, password);
        contentValues.put(LoginContract.UsersTable.COLUMN_NAME_TYPE, type.name());

        long result;
        try{
            result = myDB.insert(LoginContract.UsersTable.TABLE_NAME, null, contentValues);
        }finally {
            myDB.close();
        }

        if (result == -1){
            return false;
        }else {
            return true;
        }

    }

    /**
     * checkUsername
     * Check in the DB if the username exists
     * @param username
     * @return
     */
    public Boolean checkUsername(String username){

        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = null;
        boolean isFound;

        try {
            cursor = myDB.rawQuery("select * from "+LoginContract.UsersTable.TABLE_NAME+" where username = ?", new String[] {username});

        }catch (Exception ignored){

        }finally {
            if(cursor.getCount() > 0){
                isFound = true;
            }else{
                isFound = false;
            }
            cursor.close();
            myDB.close();

        }
        return isFound;

    }

    /**
     * checkUsernamePassword
     * <p>Check if credentials (username and password) are stored in DB</p>
     * @param username
     * @param password
     * @return
     */
    public Boolean checkUsernamePassword (String username, String password){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = null;
        boolean isFound;
        try {
            cursor = myDB.rawQuery("select * from "+LoginContract.UsersTable.TABLE_NAME+" where username = ? and password = ?", new String[] {username, password});

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor.getCount() > 0){
                isFound = true;
            }else{
                isFound = false;
            }
            cursor.close();
            myDB.close();
        }
        return isFound;

    }

    /**
     * readUserType
     * Reads the type of a user (Farmer, Brewery) stored in the DB
     * @param username
     * @return
     */
    public String readUserType (String username){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = null;
        String type = null;
        try {
            cursor = myDB.rawQuery("select * from "+LoginContract.UsersTable.TABLE_NAME+" where username = ?", new String[] {username});

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor.getCount() > 0){
                int indexType = cursor.getColumnIndex(LoginContract.UsersTable.COLUMN_NAME_TYPE);
                cursor.moveToFirst();
                type = cursor.getString(indexType);
            }
            cursor.close();
            myDB.close();

        }
        return type;
    }
}
