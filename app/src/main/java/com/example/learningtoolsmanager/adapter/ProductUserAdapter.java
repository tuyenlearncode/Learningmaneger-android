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
import com.example.learningtoolsmanager.model.Product;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductUserAdapter extends RecyclerView.Adapter<ProductUserAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Product> list;
    private ProductDAO productDAO;
    private int userID;

    public ProductUserAdapter(Context context, ArrayList<Product> list, ProductDAO productDAO, int userID) {
        this.context = context;
        this.list = list;
        this.productDAO = productDAO;
        this.userID = userID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_product_user,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(list.get(position).getName());

        NumberFormat numberFormat = new DecimalFormat("#,###VNĐ");
        double price = list.get(position).getPrice();
        String formattedPrice = numberFormat.format(price);


        holder.txtPrice.setText("Giá: "+formattedPrice);
        holder.txtQuantity.setText("Số lượng: "+list.get(position).getQuantity());

        //xu ly hinh anh
        if(!list.get(position).getImage().equals("")){
            Glide.with(context).load(list.get(position).getImage()).into(holder.ivImage);
        }

        holder.txtAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCartDialog(list.get(holder.getAdapterPosition()));
            }
        });

        holder.txtBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBuyDialog(list.get(holder.getAdapterPosition()).getId(),list.get(holder.getAdapterPosition()).getName(),list.get(holder.getAdapterPosition()).getQuantity());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtPrice, txtQuantity, txtBuy, txtAddCart;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //anh xa
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtBuy = itemView.findViewById(R.id.txtBuy);
            txtAddCart = itemView.findViewById(R.id.txtAddCart);
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
                    // Trừ đi 1 đơn vị số lượng sản phẩm
                    int newQuantity = quantity - 1;
                    boolean check = productDAO.UpdateQuantity(id, newQuantity);
                    if (check) {
                        Toast.makeText(context, "Đơn hàng đã được xác nhận", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(context, "Đặt hàng không thành công", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showAddCartDialog(final Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thêm vào giỏ hàng");
        builder.setMessage("Bạn có muốn thêm sản phẩm '" + product.getName() + "' vào giỏ hàng không?");
        builder.setIcon(R.drawable.baseline_add_shopping_cart_24);
        builder.setPositiveButton("THÊM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToCart(product, userID);
            }
        });
        builder.setNegativeButton("HỦY", null);
        builder.show();
    }

    private void addToCart(Product product, int userId) {
        if(product.getQuantity() > 0) {
            CartItem cartItem = new CartItem(product.getId(), product.getName(), product.getPrice(), 1, product.getImage());
            CartDAO cartDAO = new CartDAO(context);
            boolean addToCartResult = cartDAO.addToCart(cartItem, userID);
            if (addToCartResult) {
                Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData(){
        list.clear();
        list = productDAO.getList();
        notifyDataSetChanged();
    }
}
