package com.e.toolplusstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e.toolplusstore.adapter.HistoryOrderItemAdapter;
import com.e.toolplusstore.adapter.OrderItemAdapter;
import com.e.toolplusstore.beans.History;
import com.e.toolplusstore.beans.OrderItem;
import com.e.toolplusstore.beans.OrderItemList;
import com.e.toolplusstore.databinding.ActivityOrderItemBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderItemActivity extends AppCompatActivity {
    ActivityOrderItemBinding binding;
    HistoryOrderItemAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderItemBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initComponent();
        Intent in = getIntent();
        ArrayList<OrderItem> list = (ArrayList<OrderItem>) in.getSerializableExtra("orderItem");
        Log.e("Size", "=====================>cLLWSSSS.....");
        Log.e("itemlist", "=============>>>>>" + list.size());
        adapter = new HistoryOrderItemAdapter(this, list);
        binding.rv.setAdapter(adapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initComponent() {
        binding.toolbar.setTitle("Purchase Order details");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}