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
import com.example.learningtoolsmanager.adapter.CartAdapter;
import com.example.learningtoolsmanager.dao.CartDAO;
import com.example.learningtoolsmanager.model.CartItem;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    private RecyclerView recyclerProduct;
    private CartDAO cartDAO;
    private int userID = -1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Trích xuất id người dùng từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            userID = bundle.getInt("ACCESS_EXTRA");
        }
        //anh xa
        recyclerProduct = view.findViewById(R.id.recycleProduct);
        cartDAO = new CartDAO(getContext());
        loadData();

        return view;
    }
    private void loadData() {
        ArrayList<CartItem> list = cartDAO.getCartByUserId(userID);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerProduct.setLayoutManager(linearLayoutManager);

        CartAdapter adapter = new CartAdapter(getContext(), list, cartDAO, userID);
        recyclerProduct.setAdapter(adapter);
    }

}
