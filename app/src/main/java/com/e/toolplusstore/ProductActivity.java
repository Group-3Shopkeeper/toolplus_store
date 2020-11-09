package com.e.toolplusstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.e.toolplusstore.adapter.ShowProductAdapter;
import com.e.toolplusstore.apis.ProductService;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.databinding.ProductActivityBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    ProductActivityBinding binding;
    ShowProductAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        String categoryId = intent.getStringExtra("categoryId");
        ProductService.ProductApi productApis = ProductService.getProductApiInstance();
        final Call<ArrayList<Product>> call = productApis.getProductByCategory(categoryId);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                ArrayList<Product> productArrayList = response.body();
                adapter = new ShowProductAdapter(ProductActivity.this, productArrayList);
                binding.rv1.setAdapter(adapter);
                binding.rv1.setLayoutManager(new GridLayoutManager(ProductActivity.this, 2));
            }
            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
            }
        });
    }
}
