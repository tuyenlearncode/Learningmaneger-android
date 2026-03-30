package com.example.learningtoolsmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.learningtoolsmanager.database_product.DBHelper;
import com.example.learningtoolsmanager.model.CartItem;

import java.util.ArrayList;

public class CartDAO {
    private Context context;
    private DBHelper dbHelper;

    public CartDAO(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public boolean addToCart(CartItem cartItem, int userId) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng của userId hay chưa
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM CART WHERE productId = ? AND userId = ?", new String[]{String.valueOf(cartItem.getProductId()), String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            // Sản phẩm đã có trong giỏ hàng
            int currentQuantity = cursor.getInt(2);
            int newQuantity = currentQuantity + cartItem.getQuantity();

            ContentValues contentValues = new ContentValues();
            contentValues.put("quantity", newQuantity);

            int rowsAffected = sqLiteDatabase.update("CART", contentValues, "productId = ? AND userId = ?", new String[]{String.valueOf(cartItem.getProductId()), String.valueOf(userId)});
            cursor.close();
            return rowsAffected > 0;
        } else {
            // Sản phẩm chưa có trong giỏ hàng, thêm mới
            ContentValues contentValues = new ContentValues();
            contentValues.put("userId", userId);
            contentValues.put("productId", cartItem.getProductId());
            contentValues.put("quantity", cartItem.getQuantity());

            long result = sqLiteDatabase.insert("CART", null, contentValues);
            cursor.close();
            return result != -1;
        }
    }

    //Lay ds sp
//    public ArrayList<CartItem> getList(){
//        ArrayList<CartItem> list =new ArrayList<>();
//        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM CART", null);
//        if(cursor.getCount() > 0){
//            cursor.moveToFirst();
//            do {
//                list.add(new CartItem(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),cursor.getInt(3), cursor.getString(4)));
//            }while (cursor.moveToNext());
//        }
//        return list;
//    }

    public ArrayList<CartItem> getCartByUserId(int userId) {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String query = "SELECT PRODUCT.id, PRODUCT.name, PRODUCT.price, CART.quantity, PRODUCT.image " +
                "FROM CART " +
                "JOIN USER ON CART.userId = USER.id " +
                "JOIN PRODUCT ON CART.productId = PRODUCT.id " +
                "WHERE CART.userId = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
//                String username = cursor.getString(cursor.getInt(0));
                int productId = cursor.getInt(0);
                String productName = cursor.getString(1);
                int price = cursor.getInt(2);
                int quantity = cursor.getInt(3);
                String image = cursor.getString(4);

                // Tạo đối tượng CartItem và thêm vào danh sách
                CartItem cartItem = new CartItem(productId, productName, price, quantity, image);
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return cartItems;
    }

//    public boolean UpdateQuantity(int id, int newQuantity) {
//        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("quantity", newQuantity);
//
//        int rowsAffected = sqLiteDatabase.update("CART", contentValues, "id = ?", new String[]{String.valueOf(id)});
//        return rowsAffected > 0;
//    }

    public boolean deleteProduct(int productId) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        int rowsAffected = sqLiteDatabase.delete("CART", "productId = ?", new String[]{String.valueOf(productId)});
        return rowsAffected > 0;
    }
}
