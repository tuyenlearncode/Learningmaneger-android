package com.example.learningtoolsmanager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.learningtoolsmanager.fragment.CartFragment;
import com.example.learningtoolsmanager.fragment.IntroduceFragment;
import com.example.learningtoolsmanager.fragment.ProductUserFragment;
import com.google.android.material.navigation.NavigationView;

public class ProductActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Anh xa
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolBar);
        navigationView = findViewById(R.id.navigationView);

        // Trích xuất tên người dùng từ Intent
        int userID = getIntent().getIntExtra("ACCESS_EXTRA", -1);

        // Đặt đối số vào Bundle
        Bundle bundle = new Bundle();
        bundle.putInt("ACCESS_EXTRA", userID);

        // Set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);
        Fragment fragment= new ProductUserFragment();
        fragment = new ProductUserFragment();
        // Đặt Bundle vào Fragment
        fragment.setArguments(bundle);
        // Set fragment mặc định
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.linearLayout, fragment)
                .commit();

        // Set navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.menuProductUser) {
                    fragment = new ProductUserFragment();
                    // Đặt Bundle vào Fragment
                    fragment.setArguments(bundle);
                } else if (item.getItemId() == R.id.menuIntroduce) {
                    fragment = new IntroduceFragment();
                } else if (item.getItemId() == R.id.menuCart) {
                    fragment = new CartFragment();
                    // Đặt Bundle vào Fragment
                    fragment.setArguments(bundle);
                } else {
                    exitApplication();
                    Toast.makeText(ProductActivity.this, "Thoát ứng dụng", Toast.LENGTH_SHORT).show();
                    return true;
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.linearLayout, fragment)
                        .commit();
                getSupportActionBar().setTitle(item.getTitle());
                drawerLayout.closeDrawer(GravityCompat.START);
                //return false;
                return true;
            }
        });
    }

    private void exitApplication() {
        // Kết thúc tất cả các hoạt động (activities) của ứng dụng
        finishAffinity();

        // Đóng ứng dụng
        finish();
//        Toast.makeText(ProductActivity.this, "Đã đăng xuất tài khoản", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(ProductActivity.this, LoginActivity.class);
//        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}