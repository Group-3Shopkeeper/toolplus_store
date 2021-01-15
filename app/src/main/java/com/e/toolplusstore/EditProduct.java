package com.e.toolplusstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.toolplusstore.apis.CategoryService;
import com.e.toolplusstore.apis.ProductService;
import com.e.toolplusstore.beans.Category;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.beans.Shopkeeper;
import com.e.toolplusstore.databinding.AddProductScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProduct extends AppCompatActivity {
    AddProductScreenBinding binding;
    String currentUserId,categoryId=null,title;
    String categoryName,cN;
    Uri imageUri=null;
    ArrayList<Category> al;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddProductScreenBinding.inflate(LayoutInflater.from(EditProduct.this));
        setContentView(binding.getRoot());
        initComponent();
        binding.productName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        binding.productBrand.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        binding.productQuantity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        binding.productPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        final Intent in = getIntent();
        final Product product = (Product) in.getSerializableExtra("product");
        categoryName = in.getStringExtra("categoryName");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final SharedPreferences mPref = getSharedPreferences("MyStore",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPref.getString(currentUserId,"");
        final Shopkeeper shopkeeper = gson.fromJson(json, Shopkeeper.class);

        binding.productName.setText("Product name : "+product.getName());
        binding.productBrand.setText("Brand : "+product.getBrand());
        binding.productQuantity.setText("Quantity : "+""+product.getQtyInStock()+".0");
        binding.productPrice.setText("Price : "+"₹"+product.getPrice()+"");
        binding.productDescription.setText("Description : "+product.getDescription());
        binding.productDiscount.setText("Discount : "+product.getDiscount()+"%");
        Picasso.get().load(product.getImageUrl()).into(binding.iv1);
        Picasso.get().load(product.getSecondImageUrl()).into(binding.iv2);
        Picasso.get().load(product.getThirdImageurl()).into(binding.iv3);

        binding.btnAddProduct.setText("Update Product");
        binding.productCategory.setText(categoryName);
        categoryId = product.getCategoryId();
        binding.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                startActivityForResult(Intent.createChooser(in,"Select image"),111);
            }
        });
         binding.productCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            setCategoryCode();

            }
         });
        binding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = binding.productName.getText().toString();
                    String brand = binding.productBrand.getText().toString();
                    String description = binding.productDescription.getText().toString();
                    String productId = product.getProductId();
                    Integer quantity = Integer.parseInt(binding.productQuantity.getText().toString());
                    Double price = Double.parseDouble(binding.productPrice.getText().toString());
                    Double discount = Double.parseDouble(binding.productDiscount.getText().toString());
                    if (TextUtils.isEmpty(name)) {
                        binding.productName.setError("Enter Product Name");
                        return;
                    }
                    if (TextUtils.isEmpty(brand)) {
                        binding.productBrand.setError("Enter Brand");
                        return;
                    }
                    if (categoryId == null) {
                        categoryId = product.getCategoryId();
                    }
                    if (TextUtils.isEmpty(description)) {
                        description = "";
                    }
                    if (TextUtils.isEmpty(price.toString())) {
                        binding.productPrice.setError("Enter Price");
                        return;
                    }
                    if (TextUtils.isEmpty(discount.toString())) {
                        discount = 0.0;
                    }
                    if (quantity<=0) {
                        binding.productQuantity.setError("Quantity can't be 0");
                        return;
                    }
                    if (imageUri != null) {
                        final ProgressDialog pd = new ProgressDialog(EditProduct.this);
                        pd.setTitle("Updating");
                        pd.setMessage("Please wait...");
                        pd.show();
                        File file = FileUtils.getFile(EditProduct.this, imageUri);
                        RequestBody requestFile =
                                RequestBody.create(
                                        MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))),
                                        file
                                );
                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                        RequestBody productName = RequestBody.create(
                                okhttp3.MultipartBody.FORM, name);

                        RequestBody productBrand = RequestBody.create(okhttp3.MultipartBody.FORM, brand);
                        RequestBody productPrice = RequestBody.create(
                                okhttp3.MultipartBody.FORM, String.valueOf(price));
                        RequestBody productDiscount = RequestBody.create(
                                okhttp3.MultipartBody.FORM, String.valueOf(discount));
                        RequestBody productQtyInStock = RequestBody.create(
                                okhttp3.MultipartBody.FORM, String.valueOf(quantity));
                        RequestBody productCategoryId = RequestBody.create(
                                okhttp3.MultipartBody.FORM, categoryId);
                        RequestBody productid = RequestBody.create(
                                okhttp3.MultipartBody.FORM, productId);
                        RequestBody productDescription = RequestBody.create(
                                okhttp3.MultipartBody.FORM, description);
                        RequestBody shopkeeperId = RequestBody.create(okhttp3.MultipartBody.FORM, currentUserId);

                        ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                        Call<Product> call = productApi.updateProduct(body, productName, productQtyInStock, productPrice, productDescription, productDiscount, shopkeeperId, productBrand, productCategoryId, productid);
                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                if (response.code() == 200) {
                                    pd.dismiss();
                                    Product p = response.body();
                                    Toast.makeText(EditProduct.this, "Updated", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (response.code() == 404)
                                    Toast.makeText(EditProduct.this, "404", Toast.LENGTH_SHORT).show();
                                else if (response.code() == 500) {
                                    Toast.makeText(EditProduct.this, "500", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {
                                Toast.makeText(EditProduct.this, "" + t, Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        final ProgressDialog progressDialog = new ProgressDialog(EditProduct.this);
                        progressDialog.setTitle("Updating");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();

                        String shopkeeperId = currentUserId;
                        String imageUrl = product.getImageUrl();
                        Product p = new Product();
                        p.setDescription(description);
                        p.setBrand(brand);
                        p.setCategoryId(categoryId);
                        p.setDiscount(discount);
                        p.setName(name);
                        p.setQtyInStock(quantity);
                        p.setImageUrl(imageUrl);
                        p.setPrice(price);
                        p.setProductId(product.getProductId());
                        p.setShopKeeperId(shopkeeperId);

                        ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                        Call<Product> call = productApi.updateProductWithoutImage(p);
                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                if (response.code() == 200) {
                                    progressDialog.dismiss();
                                    Product p = response.body();
                                    Log.e("=======================", "onResponse: "+p );
                                    Toast.makeText(EditProduct.this, "Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditProduct.this, HomeActivity.class));
                                } else if (response.code() == 404)
                                    Toast.makeText(EditProduct.this, "404", Toast.LENGTH_SHORT).show();
                                else if (response.code() == 500) {
                                    Toast.makeText(EditProduct.this, "500", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {
                                Toast.makeText(EditProduct.this, "" + t, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(EditProduct.this, "Enter All Valid Input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(binding.iv1);
                Toast.makeText(this, ""+imageUri, Toast.LENGTH_SHORT).show();
            }
        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {
        binding.toolbar.setTitle("Product");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void setCategoryCode(){

                final PopupMenu popupMenu = new PopupMenu(EditProduct.this, binding.productCategory);
                popupMenu.setGravity(Gravity.END);
                CategoryService.CategoryApi userapi= CategoryService.getCategoryApiInstance();
                Call<ArrayList<Category>> call = userapi.getCategoryList();
                call.enqueue(new Callback<ArrayList<Category>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                        al = response.body();
                        for(Category c : al){
                            popupMenu.getMenu().add(c.getCategoryName());
                        }
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                String  categoryName=item.getTitle().toString();
                                binding.productCategory.setText(categoryName);
                                for(Category c : al){
                                    if(c.getCategoryName()==categoryName){
                                        categoryId = c.getCategoryId();
                                    }
                                }
                                popupMenu.dismiss();
                                return false;
                            }
                        });
                        popupMenu.show();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

                    }
                });
            }

}
