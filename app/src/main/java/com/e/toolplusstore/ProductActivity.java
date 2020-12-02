package com.e.toolplusstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.e.toolplusstore.adapter.ShowProductAdapter;
import com.e.toolplusstore.apis.ProductService;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.databinding.ProductActivityBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    ProductActivityBinding binding;
    ShowProductAdapter adapter;
    String currentUserId,categoryName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        Log.e("===========", "onCreate: "+currentUserId );
        Intent intent = getIntent();
        String categoryId = intent.getStringExtra("categoryId");
         categoryName = intent.getStringExtra("categoryName");
        initComponent();
        Log.e("===========", "categoryId: "+categoryId );
        String shopKeeperId = currentUserId;
        ProductService.ProductApi productApis = ProductService.getProductApiInstance();
        final Call<List<Product>> call = productApis.getProductByCategoryAndShopkeeper(categoryId,shopKeeperId);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.code()==200) {
                    List<Product> productArrayList = response.body();
                    Log.e("=============", "product: " + productArrayList);
                    if (productArrayList.size() == 0)
                        Toast.makeText(ProductActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    else {
                        adapter = new ShowProductAdapter(ProductActivity.this, productArrayList);
                        binding.rv1.setAdapter(adapter);
                        binding.rv1.setLayoutManager(new GridLayoutManager(ProductActivity.this, 2));
                        adapter.setOnItemClickListener(new ShowProductAdapter.OnRecyclerViewClick() {
                            @Override
                            public void onItemClick(Product product, int position) {
                                Intent in = new Intent(ProductActivity.this, ProductDetailsActivty.class);
                                in.putExtra("product", product);
                                in.putExtra("categoryName", categoryName);
                                startActivity(in);
                            }
                        });
                    }
                }
                else if(response.code()==404){
                    Toast.makeText(ProductActivity.this, "404", Toast.LENGTH_SHORT).show();
                }
                else if(response.code()==500)
                    Toast.makeText(ProductActivity.this, "500", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
            }
        });
    }

    private void initComponent() {
        binding.toolbar.setTitle(categoryName);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
