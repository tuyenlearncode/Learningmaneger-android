package com.example.learningtoolsmanager.database_product;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){
            super(context, "STATIONERYMANAGER", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String qUser = "CREATE TABLE USER(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, username TEXT UNIQUE, password TEXT, fullname TEXT)";
        db.execSQL(qUser);
        String qProduct = "CREATE TABLE PRODUCT(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER, quantity INTEGER, image TEXT)";
        db.execSQL(qProduct);
        String qCart = "CREATE TABLE CART(userId INTEGER, productId INTEGER, quantity INTEGER, FOREIGN KEY(userId) REFERENCES USER(id), FOREIGN KEY(productId) REFERENCES PRODUCT(id))";
        db.execSQL(qCart);

        String dUser = "INSERT INTO USER VALUES(0,'admin','thanhtuyen','123','Thanh Tuyen')";
        db.execSQL(dUser);
        String dUser2 = "INSERT INTO USER VALUES(1,'user','2','2','Thanh Tuyen')";
        db.execSQL(dUser2);
        String dProduct = "INSERT INTO PRODUCT VALUES(1, 'Vở ô ly tập viết 1', 20000, 10, 'https://res.cloudinary.com/dyy3jd2p5/image/upload/v1714044999/z5383027444250_c5066204114438c28b354d0b55ad882d_j2hjit.jpg')";
        db.execSQL(dProduct);


        //String dCart = "INSERT INTO CART VALUES(1, 'Your Lie In April', 120000, 1, 'http://res.cloudinary.com/di30nlgd3/image/upload/v1713630763/szdjhvvoxmxm8vjwplrc.jpg')";
        //db.execSQL(dCart);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS USER");
            db.execSQL("DROP TABLE IF EXISTS PRODUCT");
            db.execSQL("DROP TABLE IF EXISTS CART");
            onCreate(db);
        }
    }
}
