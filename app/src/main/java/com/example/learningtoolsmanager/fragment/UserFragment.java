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
import com.example.learningtoolsmanager.adapter.UserAdapter;
import com.example.learningtoolsmanager.dao.UserDAO;
import com.example.learningtoolsmanager.model.User;

import java.util.ArrayList;

public class UserFragment extends Fragment {
    private RecyclerView recyclerProduct;
    private UserDAO userDAO;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_manager, container, false);

        //anh xa
        recyclerProduct = view.findViewById(R.id.recycleProduct);
        userDAO = new UserDAO(getContext());
        loadData();

        return view;
    }
    private void loadData(){
        ArrayList<User> list = userDAO.getList();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerProduct.setLayoutManager(linearLayoutManager);

        UserAdapter adapter = new UserAdapter(getContext(),list, userDAO);
        recyclerProduct.setAdapter(adapter);
    }

}
