package com.example.learningtoolsmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learningtoolsmanager.dao.UserDAO;
import com.example.learningtoolsmanager.util.SendMail;

public class LoginActivity extends AppCompatActivity {
    private UserDAO userDAO;
    private SendMail sendMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPass = findViewById(R.id.edtPass);
        CheckBox ckbRemember = findViewById(R.id.ckbRemember);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txtForgot = findViewById(R.id.txtForgot);
        TextView txtSignup = findViewById(R.id.txtSignup);
        userDAO = new UserDAO(this);
        sendMail = new SendMail();

        boolean check;

        //Kiem tra co remember khong?
        SharedPreferences sharedPreferences = getSharedPreferences("INFO", MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean("isRemember",false);
        if(isRemember){
            String user = sharedPreferences.getString("emailLogin", "");
            String pass = sharedPreferences.getString("passLogin", "");
            edtEmail.setText(user);
            edtPass.setText(pass);
            ckbRemember.setChecked(true);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailLogin = edtEmail.getText().toString();
                String passLogin = edtPass.getText().toString();

                int userID = userDAO.CheckLogin(emailLogin, passLogin);

                if(userID > 0){

                    //checked RememberMe
                    if(ckbRemember.isChecked()){
                        SharedPreferences preferences = getSharedPreferences("INFO", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("emailLogin",emailLogin);
                        editor.putString("passLogin",passLogin);
                        editor.putBoolean("isRemember", ckbRemember.isChecked());
                        editor.apply();
                    }else{
                        SharedPreferences preferences = getSharedPreferences("INFO", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                    }
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, ProductActivity.class);
                    intent.putExtra("ACCESS_EXTRA", userID);
                    startActivity(intent);
                }else if(userID == 0){
                    Toast.makeText(LoginActivity.this, "Đăng nhập với quyền admin", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                }
                else{
                    Toast.makeText(LoginActivity.this, "Vui lòng kiểm tra lại mật khẩu & tài khoản", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogForgot();
            }
        });
    }

    private void ShowDialogForgot(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_forgot, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //ánh xạ
        EditText edtEmail = view.findViewById(R.id.edtEmail);
        Button btnSend = view.findViewById(R.id.btnSend);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = userDAO.ForgotPassword(email);
                //Toast.makeText(LoginActivity.this, password, Toast.LENGTH_SHORT).show();

                if(password.equals("")){
                    Toast.makeText(LoginActivity.this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
                }else{
                    sendMail.Send(LoginActivity.this, email, "LẤY LẠI MẬT KHẨU", "Mật khẩu: "+password);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}