package com.e.toolplusstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

        Intent in = getIntent();
        String name = in.getStringExtra("name");
        InternetConnectivity connectivity = new InternetConnectivity();
        if (!connectivity.isConnected(ProductActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
            builder.setMessage("Please connect to the Internet to Proceed Further").setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            }).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });
            builder.show();
        } else {

            ProductService.ProductApi productApi = ProductService.getProductApiInstance();
            final Call<ArrayList<Product>> productList = productApi.getProductList(name);
            productList.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<Product> productList = response.body();
                        adapter = new ShowProductAdapter(ProductActivity.this, productList);
                        binding.rv1.setAdapter(adapter);
                        binding.rv1.setLayoutManager(new GridLayoutManager(ProductActivity.this, 2));
                    } else {
                        Toast.makeText(ProductActivity.this, "something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                    Toast.makeText(ProductActivity.this, "" + t, Toast.LENGTH_SHORT).show();


                }
            });

        }
    }
}