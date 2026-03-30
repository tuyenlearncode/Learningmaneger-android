package com.example.learningtoolsmanager.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learningtoolsmanager.R;
import com.example.learningtoolsmanager.dao.CartDAO;
import com.example.learningtoolsmanager.dao.ProductDAO;
import com.example.learningtoolsmanager.model.CartItem;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    private Context context;
    private ArrayList<CartItem> list;
    private CartDAO cartDAO;
    //private ProductAdapter productAdapter;

    private int userId;

    public CartAdapter(Context context, ArrayList<CartItem> list, CartDAO cartDAO, int userId) {
        this.context = context;
        this.list = list;
        this.cartDAO = cartDAO;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_user_cart,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(list.get(position).getProductName());

        NumberFormat numberFormat = new DecimalFormat("#,###VNĐ");
        double price = list.get(position).getPrice();
        String formattedPrice = numberFormat.format(price);


        holder.txtPrice.setText("Giá: "+formattedPrice);
        holder.txtQuantity.setText("Số lượng: "+list.get(position).getQuantity());

        //xu ly hinh anh
        if(!list.get(position).getImage().equals("")){
            Glide.with(context).load(list.get(position).getImage()).into(holder.ivImage);
        }

        holder.txtBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBuyDialog(list.get(holder.getAdapterPosition()).getProductId(),list.get(holder.getAdapterPosition()).getProductName(),list.get(holder.getAdapterPosition()).getQuantity());
            }
        });

        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(list.get(holder.getAdapterPosition()).getProductId(),list.get(holder.getAdapterPosition()).getProductName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtPrice, txtQuantity, txtBuy, txtDelete;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //anh xa
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtBuy = itemView.findViewById(R.id.txtBuy);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }

    private void showBuyDialog(int id, String name, int quantity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ĐẶT HÀNG");
        builder.setMessage("Bạn có muốn đặt hàng sản phẩm \"" + name + "\"?");
        builder.setIcon(R.drawable.baseline_attach_money_24);

        builder.setNegativeButton("HỦY", null);
        builder.setPositiveButton("XÁC NHẬN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (quantity > 0) {
//                    // Trừ đi 1 đơn vị số lượng sản phẩm
//                    int newQuantity = quantity - 1;
                    CartDAO cartDAO = new CartDAO(context);
                    ProductDAO productDAO = new ProductDAO(context);
//                    boolean checkCartItem = cartDAO.UpdateQuantity(id, 0);

                    boolean checkProductItem = productDAO.UpdateBuyQuantity(id, quantity);
                    if(checkProductItem){
                        boolean checkCartItem = cartDAO.deleteProduct(id);
                        if (checkCartItem) {
                            Toast.makeText(context, "Đơn hàng đã được xác nhận", Toast.LENGTH_SHORT).show();
                            loadData();
                        } else {
                            Toast.makeText(context, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(context, "Sản phẩm không đáp ứng số lượng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private  void showDeleteDialog(int id, String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("THÔNG BÁO");
        builder.setMessage("Bạn xác nhận xóa sản phẩm \""+name+"\" khỏi giỏ hàng?");
        builder.setIcon(R.drawable.baseline_delete_24);

        builder.setNegativeButton("HỦY", null);
        builder.setPositiveButton("XÁC NHẬN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean check = cartDAO.deleteProduct(id);
                if(check){
                    Toast.makeText(context, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                    loadData();
                }else{
                    Toast.makeText(context, "Xóa khôgn thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadData(){
        list.clear();
        list = cartDAO.getCartByUserId(userId);
        notifyDataSetChanged();
    }
}
