package com.example.learningtoolsmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.learningtoolsmanager.database_product.DBHelper;
import com.example.learningtoolsmanager.model.User;

import java.util.ArrayList;

public class UserDAO {
    private DBHelper dbHelper;

    public UserDAO(Context context){
        dbHelper = new DBHelper(context);
    }

    //Login
    public int CheckLogin(String username, String password){
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM USER WHERE username = ? AND password = ?", new String[]{username, password});

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            int userId = cursor.getInt(0 );
            return userId;
        }
        return -1;
//        return cursor.getCount() > 0;
    }

    //Register
    public boolean Register(String username, String password, String fullname){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("type","user");
        contentValues.put("username",username);
        contentValues.put("password",password);
        contentValues.put("fullname",fullname);

        long check =  sqLiteDatabase.insert("USER", null, contentValues);
        return check != -1;
    }

    //Forgot
    public String ForgotPassword(String email){
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT password FROM USER WHERE username = ?", new String[]{email});
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            return cursor.getString(0);
        }else{
            return "";
        }
    }

    //Lay ds sp
    public ArrayList<User> getList(){
        ArrayList<User> list =new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM USER WHERE type = ?", new String[]{"user"});
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                list.add(new User(cursor.getString(4), cursor.getString(2),cursor.getString(3)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public boolean DeleteUser(String username){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        int check = sqLiteDatabase.delete("USER", "username = ?", new String[]{username});
        return check > 0;
    }
}
