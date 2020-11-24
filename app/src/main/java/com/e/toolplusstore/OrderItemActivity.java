package com.e.toolplusstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e.toolplusstore.adapter.OrderItemAdapter;
import com.e.toolplusstore.beans.OrderItemList;
import com.e.toolplusstore.databinding.ActivityOrderItemBinding;

import java.util.List;

public class OrderItemActivity extends AppCompatActivity {
    ActivityOrderItemBinding binding;
    OrderItemAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderItemBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        List<OrderItemList> itemList = (List<OrderItemList>) in.getSerializableExtra("orderItem");
        adapter = new OrderItemAdapter(this,itemList);
        binding.rv.setAdapter(adapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
    }
    }

