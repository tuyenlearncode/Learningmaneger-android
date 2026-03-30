package com.example.learningtoolsmanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningtoolsmanager.R;
import com.example.learningtoolsmanager.adapter.ProductUserAdapter;
import com.example.learningtoolsmanager.dao.ProductDAO;
import com.example.learningtoolsmanager.model.Product;

import java.util.ArrayList;

public class ProductUserFragment extends Fragment {
    private RecyclerView recyclerProduct;
    private ProductDAO productDAO;
    private int userID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_user, container, false);

        //anh xa
        recyclerProduct = view.findViewById(R.id.recycleProduct);
        productDAO = new ProductDAO(getContext());
        // Trích xuất tên người dùng từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            userID = bundle.getInt("ACCESS_EXTRA");
        }

        loadData();

        return view;
    }

    private void loadData(){
        ArrayList<Product> list = productDAO.getList();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerProduct.setLayoutManager(linearLayoutManager);

        ProductUserAdapter adapter = new ProductUserAdapter(getContext(),list, productDAO, userID);
        recyclerProduct.setAdapter(adapter);
    }
}
