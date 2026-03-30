package com.example.learningtoolsmanager.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.learningtoolsmanager.R;
import com.example.learningtoolsmanager.adapter.ProductAdapter;
import com.example.learningtoolsmanager.dao.ProductDAO;
import com.example.learningtoolsmanager.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductManagerFragment extends Fragment {
    private RecyclerView recyclerProduct;
    private FloatingActionButton floatAddButton;
    private String filePath;
    private ImageView ivProductImage;
    private TextView txtStateImage;
    private String linkImage = "";

    private ProductDAO productDAO;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_manager, container, false);

        //anh xa
        recyclerProduct = view.findViewById(R.id.recycleProduct);
        floatAddButton = view.findViewById(R.id.floatAddButton);

        productDAO = new ProductDAO(getContext());
        loadData();

        //Loi xung dot khi khoi tao nhieu mediaManager
        //configCloudinary();

        floatAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd();
            }
        });

        return view;
    }

    private void loadData(){
        ArrayList<Product> list = productDAO.getList();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerProduct.setLayoutManager(linearLayoutManager);

        ProductAdapter adapter = new ProductAdapter(getContext(),list, productDAO);
        recyclerProduct.setAdapter(adapter);
    }

    private  void showDialogAdd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_product,null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.show();
        alertDialog.setCancelable(false);

        //anh xa
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPrice = view.findViewById(R.id.edtPrice);
        EditText edtQuantity = view.findViewById(R.id.edtQuantity);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        ivProductImage = view.findViewById(R.id.ivImageProduct);
        txtStateImage = view.findViewById(R.id.txtStateImage);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String price = edtPrice.getText().toString();
                String quantity = edtQuantity.getText().toString();

                if(name.equals("")||price.equals("")||quantity.equals("")){
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                }else{
                    Product product = new Product(name,Integer.parseInt(price),Integer.parseInt(quantity), linkImage);
                    boolean check = productDAO.AddProduct(product);
                    if(check){
                        Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        loadData();
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(getContext(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessTheGallery();
            }
        });
    }

    //Lay hinh anh
    public void accessTheGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        myLauncher.launch(i);
    }

    private ActivityResultLauncher<Intent> myLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                // Hình ảnh đã được chọn
                Uri selectedImageUri = result.getData().getData();
                if (selectedImageUri != null) {
                    filePath = getRealPathFromUri(selectedImageUri, getActivity());
                    try {
                        // Đặt hình ảnh đã chọn vào ImageView
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                        ivProductImage.setImageBitmap(bitmap);

                        uploadToCloudinary(filePath);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

    private String getRealPathFromUri(Uri imageUri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(imageUri, null, null, null, null);

        if (cursor == null) {
            return imageUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    HashMap<String, String> config = new HashMap<>();

    private void configCloudinary() {
        config.put("cloud_name", "dyy3jd2p5");
        config.put("api_key", "823428547289647");
        config.put("api_secret", "duIPaQEZBBfxQDTj79nF0-GdiJo");
        MediaManager.init(getActivity(), config);

    }

    private void uploadToCloudinary(String filePath) {
        Log.d("A", "sign up uploadToCloudinary- ");
        MediaManager.get().upload(filePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                txtStateImage.setText("Bắt đầu upload");
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                txtStateImage.setText("Đang upload... ");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                linkImage = resultData.get("url").toString();
                txtStateImage.setText("Thành công: " + resultData.get("url").toString());
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                txtStateImage.setText("Lỗi " + error.getDescription());
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                txtStateImage.setText("Reshedule " + error.getDescription());
            }
        }).dispatch();
    }
}
