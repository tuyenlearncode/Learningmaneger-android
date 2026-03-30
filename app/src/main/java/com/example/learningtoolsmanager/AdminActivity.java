package com.example.learningtoolsmanager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.cloudinary.android.MediaManager;
import com.example.learningtoolsmanager.fragment.IntroduceFragment;
import com.example.learningtoolsmanager.fragment.ProductManagerFragment;
import com.example.learningtoolsmanager.fragment.UserFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private final int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //anh xa
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolBar);
        navigationView = findViewById(R.id.navigationView);

        //set cloudinary
        configCloudinary();
        //Cap quyen
        requestPermission();

        //set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);

        //set fragment mac dinh
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.linearLayout, new ProductManagerFragment())
                .commit();

        //set navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if(item.getItemId() == R.id.menuProductManager){
                    fragment = new ProductManagerFragment();
                } else if (item.getItemId() == R.id.menuIntroduce) {
                    fragment = new IntroduceFragment();
                }else if(item.getItemId() == R.id.menuUserManager){
                    fragment = new UserFragment();
                }else{
                    exitApplication();
                    Toast.makeText(AdminActivity.this, "Thoát ứng dụng", Toast.LENGTH_SHORT).show();
                    return true;
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.linearLayout, fragment)
                        .commit();
                getSupportActionBar().setTitle(item.getTitle());
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void exitApplication() {
        // Kết thúc tất cả các hoạt động (activities) của ứng dụng
        finishAffinity();

        // Đóng ứng dụng
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Cấp quyền thành công", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Cấp quyền thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    HashMap<String, String> config = new HashMap<>();

    private void configCloudinary() {
        config.put("cloud_name", "dyy3jd2p5");
        config.put("api_key", "823428547289647");
        config.put("api_secret", "duIPaQEZBBfxQDTj79nF0-GdiJo");
        MediaManager.init(AdminActivity.this, config);

    }
}
