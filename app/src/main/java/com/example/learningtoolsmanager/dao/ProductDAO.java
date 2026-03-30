package com.example.learningtoolsmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.learningtoolsmanager.database_product.DBHelper;
import com.example.learningtoolsmanager.model.Product;

import java.util.ArrayList;

public class ProductDAO {
    private DBHelper dbHelper;

    public ProductDAO(Context context){
        dbHelper = new DBHelper(context);
    }

    //Lay ds sp
    public ArrayList<Product> getList(){
        ArrayList<Product> list =new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM PRODUCT", null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                list.add(new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),cursor.getInt(3), cursor.getString(4)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public boolean AddProduct(Product product){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name",product.getName());
        contentValues.put("price",product.getPrice());
        contentValues.put("quantity",product.getQuantity());
        contentValues.put("image",product.getImage());

        long check = sqLiteDatabase.insert("PRODUCT",null, contentValues);
        return check != -1;
    }

    public boolean EditProduct(Product product){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", product.getName());
        contentValues.put("price", product.getPrice());
        contentValues.put("quantity", product.getQuantity());
//        contentValues.put("image",product.getImage());

        int check = sqLiteDatabase.update("PRODUCT", contentValues, "id = ?", new String[]{String.valueOf(product.getId())});
        return check > 0;
    }

    public boolean DeleteProduct(int id){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        int check = sqLiteDatabase.delete("PRODUCT", "id = ?", new String[]{String.valueOf(id)});
        return check > 0;
    }

    public boolean UpdateQuantity(int id, int newQuantity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", newQuantity);

        int rowsAffected = sqLiteDatabase.update("PRODUCT", contentValues, "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    public boolean UpdateBuyQuantity(int id, int quantity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        int currentQuantity = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT quantity FROM PRODUCT WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            currentQuantity = cursor.getInt(0);
        }
        cursor.close();

        if (currentQuantity > 0) {
            if(currentQuantity >= quantity){
                int newQuantity = currentQuantity - quantity;

                ContentValues contentValues = new ContentValues();
                contentValues.put("quantity", newQuantity);

                int rowsAffected = sqLiteDatabase.update("PRODUCT", contentValues, "id = ?", new String[]{String.valueOf(id)});
                return rowsAffected > 0;
            }else{
                return false;
            }
        } else {
            return false; // Sản phẩm đã hết hàng
        }
    }
}
