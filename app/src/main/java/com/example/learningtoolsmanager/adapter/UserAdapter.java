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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningtoolsmanager.R;
import com.example.learningtoolsmanager.dao.UserDAO;
import com.example.learningtoolsmanager.model.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> list;
    private UserDAO userDAO;

    public UserAdapter(Context context, ArrayList<User> list, UserDAO userDAO) {
        this.context = context;
        this.list = list;
        this.userDAO = userDAO;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtUsername.setText(list.get(position).getUsername());
        holder.txtEmail.setText(list.get(position).getEmail());

//        holder.txtEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showUpdateDialog(list.get(holder.getAdapterPosition()));
//            }
//        });
        holder.txtDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailDialog(list.get(holder.getAdapterPosition()));
            }
        });
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(list.get(holder.getAdapterPosition()).getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtEmail, txtEdit, txtDelete, txtDetail;
//        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //anh xa
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtEdit = itemView.findViewById(R.id.txtEdit);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            txtDetail = itemView.findViewById(R.id.txtDetail);
//            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }

    private  void showDeleteDialog(String username){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("THÔNG BÁO");
        builder.setMessage("Bạn xác nhận xóa người  \""+username+"\"?");
        builder.setIcon(R.drawable.baseline_warning_24);

        builder.setNegativeButton("Hủy", null);
        builder.setPositiveButton("XÁC NHẬN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean check = userDAO.DeleteUser(username);
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

    private void showDetailDialog(User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_user_detail, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtFullname = view.findViewById(R.id.txtFullname);
        TextView txtEmail = view.findViewById(R.id.txtEmail);
        TextView txtPassword = view.findViewById(R.id.txtPassword);

        Button btnCancel = view.findViewById(R.id.btnCancel);

        //Hien thi du lieu
        txtFullname.setText(user.getUsername());
        txtEmail.setText(String.valueOf(user.getEmail()));
        txtPassword.setText(String.valueOf(user.getPassword()));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void loadData(){
        list.clear();
        list = userDAO.getList();
        notifyDataSetChanged();
    }
}
