package com.e.toolplusstore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.e.toolplusstore.apis.ProductService;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.databinding.ActivityProductDetailsBinding;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivty extends AppCompatActivity {
    private static final String TAG ="" ;
    ActivityProductDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        final Product p = (Product) in.getSerializableExtra("product");
        Picasso.get().load(p.getImageUrl()).into(binding.iv);
        binding.tvBrand.setText("Brand : "+p.getBrand());
        binding.tvDescription.setText("Description : "+p.getDescription());
        binding.tvPrice.setText("Price : "+p.getPrice()+"");
        binding.tvProductName.setText("Product Name : "+p.getName());
        binding.tvDiscount.setText("Discount : "+p.getDiscount()+"");
        binding.tvQtyInStock.setText("Quantity In Stock : "+p.getQtyInStock());
        binding.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ProductDetailsActivty.this);
                ab.setTitle("Are You Sure?");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                        Call<Product> call =productApi.deleteProduct(p.getProductId());
                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                Toast.makeText(ProductDetailsActivty.this, "Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ProductDetailsActivty.this,HomeActivity.class));
                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {
                                Toast.makeText(ProductDetailsActivty.this, ""+t, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                ab.show();
            }
        });
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ProductDetailsActivty.this,EditProduct.class);
                in.putExtra("product",p);
                startActivity(in);
            }
        });
    }
}