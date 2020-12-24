package com.e.toolplusstore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.e.toolplusstore.apis.CommentService;
import com.e.toolplusstore.apis.ProductService;
import com.e.toolplusstore.beans.Comment;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.databinding.ActivityProductDetailsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        final String categoryName = in.getStringExtra("categoryName");

        if(p.getDiscount()<1) {
            binding.tvDiscount.setVisibility(View.GONE);
            binding.tvMRP.setVisibility(View.GONE);
            binding.tvPrice.setText("Price : "+p.getPrice()+"");
        }else{
            binding.tvDiscount.setText("Off : ("+p.getDiscount()+"%)");
            binding.tvMRP.setText("MRP : "+p.getPrice()+"");
            binding.tvMRP.setPaintFlags(binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Double ap = p.getPrice() -(p.getPrice()*p.getDiscount()/100);
            binding.tvPrice.setText(""+ap);
        }
        Picasso.get().load(p.getImageUrl()).into(binding.iv);
        binding.tvBrand.setText("Brand : "+p.getBrand());
        binding.tvDescription.setText("Description : "+p.getDescription());
        binding.tvProductName.setText("Product Name : "+p.getName());
        binding.tvQtyInStock.setText("Quantity In Stock : "+p.getQtyInStock());
        binding.rlBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ProductDetailsActivty.this);
                ab.setTitle("Are You Sure?");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog pd =new ProgressDialog(ProductDetailsActivty.this);
                        pd.setTitle("Deleting");
                        pd.setMessage("Please wait..");
                        ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                        Call<Product> call =productApi.deleteProduct(p.getProductId());
                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                pd.dismiss();
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
        binding.rlBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ProductDetailsActivty.this,EditProduct.class);
                in.putExtra("product",p);
                in.putExtra("categoryName",categoryName);
                startActivity(in);
            }
        });
        binding.tvViewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ProductDetailsActivty.this,ViewComments.class);
                in.putExtra("product",p);
                startActivity(in);
            }
        });

    }
}