package com.example.learningtoolsmanager;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learningtoolsmanager.dao.UserDAO;

public class SignupActivity extends AppCompatActivity {
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //anh xa
        EditText edtUsername = findViewById(R.id.edtUsername);
        EditText edtPassword = findViewById(R.id.edtPassword);
        EditText edtRePassword = findViewById(R.id.edtRePassword);
        EditText edtFullname = findViewById(R.id.edtFullname);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        Button btnBack = findViewById(R.id.btnBack);

        userDAO = new UserDAO(this);

        btnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick (View v){
                String user = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();
                String rePass = edtRePassword.getText().toString();
                String fullname = edtFullname.getText().toString();

                //validate
                if(user.equals("") || pass.equals("") || rePass.equals("") || fullname.equals("")){
                    Toast.makeText(SignupActivity.this, "Nhập đầy đủ thông tin tài khoản",Toast.LENGTH_SHORT).show();
                } else if(!isValidEmail(user)){
                    Toast.makeText(SignupActivity.this,"Email không hợp lệ",Toast.LENGTH_SHORT).show();
                }else if(!pass.equals(rePass)){
                    Toast.makeText(SignupActivity.this, "Mật khẩu không trùng khớp",Toast.LENGTH_SHORT).show();
                }else{
                    boolean check = userDAO.Register(user, pass, fullname );
                    if(check){
                        Toast.makeText(SignupActivity.this, "Đăng kí thành công",Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(SignupActivity.this, "Đăng kí thất bại",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}