package com.e.toolplusstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.toolplusstore.apis.CategoryService;
import com.e.toolplusstore.beans.Category;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.databinding.AddProductScreenBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProduct extends AppCompatActivity {
    AddProductScreenBinding binding;
    String categoryName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddProductScreenBinding.inflate(LayoutInflater.from(EditProduct.this));
        setContentView(binding.getRoot());
        initComponent();
        Intent in = getIntent();
        Product product = (Product) in.getSerializableExtra("product");

        binding.productName.setText(product.getName());
        binding.productBrand.setText(product.getBrand());
        binding.productQuantity.setText(product.getQtyInStock()+"");
        binding.productPrice.setText(product.getDiscount()+"");
        binding.productDescription.setText(product.getDescription());
        binding.productDiscount.setText(product.getDiscount()+"");
        Picasso.get().load(product.getImageUrl()).into(binding.productImage);
        binding.btnAddProduct.setText("Update Product");

        final String productCategoryId =product.getCategoryId();

        CategoryService.CategoryApi categoryApi = CategoryService.getCategoryApiInstance();
        Call<ArrayList<Category>> call = categoryApi.getCategoryList();
        call.enqueue(new Callback<ArrayList<Category>>() {
                         @Override
                         public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                             ArrayList<Category> categoryList = response.body();
                             for(Category categories : categoryList){
                                 Log.e("==========",""+categories);
                                 if(productCategoryId==categories.getCategoryId()){
                                     categoryName = categories.getCategoryName();
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

                         }
                     });
        binding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProduct.this, ""+categoryName, Toast.LENGTH_SHORT).show();
            }
        });
    }


            private void initComponent() {
        binding.toolbar.setTitle("Product");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
