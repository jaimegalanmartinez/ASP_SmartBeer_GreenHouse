package com.asp.smartbeergreenhouse.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.asp.smartbeergreenhouse.model.User;

public class DBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LoginContract.UsersTable.TABLE_NAME + " (" +
                    LoginContract.UsersTable._ID + " INTEGER PRIMARY KEY," +
                    LoginContract.UsersTable.COLUMN_NAME_USERNAME + " TEXT," +
                    LoginContract.UsersTable.COLUMN_NAME_PASSWORD + " TEXT," +
                    LoginContract.UsersTable.COLUMN_NAME_TYPE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LoginContract.UsersTable.TABLE_NAME;


    public DBHelper (Context context){
        super(context, "Login.db", null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
    }

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
