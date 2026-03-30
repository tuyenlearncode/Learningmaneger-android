package com.example.learningtoolsmanager.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learningtoolsmanager.R;
import com.example.learningtoolsmanager.dao.ProductDAO;
import com.example.learningtoolsmanager.model.Product;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Product> list;
    private ProductDAO productDAO;

    public ProductAdapter(Context context, ArrayList<Product> list, ProductDAO productDAO) {
        this.context = context;
        this.list = list;
        this.productDAO = productDAO;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_product,parent,false);

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

        holder.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDialog(list.get(holder.getAdapterPosition()));
            }
        });
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(list.get(holder.getAdapterPosition()).getId(),list.get(holder.getAdapterPosition()).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtPrice, txtQuantity, txtEdit, txtDelete;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //anh xa
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtEdit = itemView.findViewById(R.id.txtEdit);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }

    private void showUpdateDialog(Product product){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_product, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPrice = view.findViewById(R.id.edtPrice);
        EditText edtQuantity = view.findViewById(R.id.edtQuantity);

        Button btnEdit = view.findViewById(R.id.btnEdit);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        //Hien thi du lieu
        edtName.setText(product.getName());
        edtPrice.setText(String.valueOf(product.getPrice()));
        edtQuantity.setText(String.valueOf(product.getQuantity()));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = product.getId();
                String name = edtName.getText().toString();
                String price = edtPrice.getText().toString();
                String quantity = edtQuantity.getText().toString();

                if(edtName.equals("")||edtPrice.equals("")||edtQuantity.equals("")){
                    Toast.makeText(context, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    Product editedProduct = new Product(id, name, Integer.parseInt(price), Integer.parseInt(quantity));
                    boolean check = productDAO.EditProduct(editedProduct);
                    if(check){
                        Toast.makeText(context, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show();
                        loadData();
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(context, "Chỉnh sửa thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private  void showDeleteDialog(int id, String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("THÔNG BÁO");
        builder.setMessage("Bạn xác nhận xóa sản phẩm \""+name+"\"?");
        builder.setIcon(R.drawable.baseline_warning_24);

        builder.setNegativeButton("Hủy", null);
        builder.setPositiveButton("XÁC NHẬN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean check = productDAO.DeleteProduct(id);
                if(check){
                    Toast.makeText(context, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                    loadData();
                }else{
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void UpdateData(){
        for (Product product : list) {
            if (product.getQuantity() == 0) {
                boolean deleteResult = productDAO.DeleteProduct(product.getId());
                if (deleteResult) {
                    Toast.makeText(context, "Sản phẩm đã hết hàng và đã được xóa", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void UpdateQuantity(int id) {
        for (Product product : list) {
            if (product.getId() == id) {
                product.setQuantity(product.getQuantity()-1);
                break;
            }
        }
        notifyDataSetChanged();
    }
    private void loadData(){
        list.clear();
        list = productDAO.getList();
        notifyDataSetChanged();
    }
}
