package com.e.toolplusstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.e.toolplusstore.adapter.ShowProductAdapter;
import com.e.toolplusstore.apis.CategoryService;
import com.e.toolplusstore.apis.ProductService;
import com.e.toolplusstore.beans.Category;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.databinding.ProductActivityBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductActivity extends AppCompatActivity {
    ProductActivityBinding binding;
    ShowProductAdapter adapter;
    String currentUserId;
    String categoryName;
    Category category;
    ArrayList<Category> al;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Sprite doubleBounce = new Wave();
        binding.spinKit.setIndeterminateDrawable(doubleBounce);
        initComponent();
        Intent in = getIntent();
        String productName = in.getStringExtra("name");
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        InternetConnectivity connectivity = new InternetConnectivity();
        if (!connectivity.isConnected(SearchProductActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchProductActivity.this);
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
            final Call<ArrayList<Product>> productList = productApi.getProductList(currentUserId,productName);
            productList.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().isEmpty()) {
                            ArrayList<Product> productList = response.body();
                            adapter = new ShowProductAdapter(SearchProductActivity.this, productList);
                            binding.rv1.setAdapter(adapter);
                            binding.rv1.setLayoutManager(new GridLayoutManager(SearchProductActivity.this, 2));
                            adapter.setOnItemClickListener(new ShowProductAdapter.OnRecyclerViewClick() {
                                @Override
                                public void onItemClick(final Product product, int position) {
                                    final Intent intent=new Intent(SearchProductActivity.this,ProductDetailsActivty.class);
                                    intent.putExtra("product", product);
                                    startActivity(intent);
                                }
                            });
                            binding.spinKit.setVisibility(View.INVISIBLE);
                        }
                        else {
                            binding.spinKit.setVisibility(View.INVISIBLE);
                            binding.noMatch.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                        Toast.makeText(SearchProductActivity.this, "somethig went wrong", Toast.LENGTH_SHORT).show();

                }
                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                    Toast.makeText(SearchProductActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void initComponent() {
        binding.toolbar.setTitle("Search Product");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}