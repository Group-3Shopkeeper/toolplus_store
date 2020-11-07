package com.e.toolplusstore;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.e.toolplusstore.api.ProductService;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.databinding.AddProductScreenBinding;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    AddProductScreenBinding binding;
    Uri imageUri;
    String title,categoryId;
    InternetConnectivity connectivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddProductScreenBinding.inflate(LayoutInflater.from(AddProductActivity.this));
        setContentView(binding.getRoot());
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},11);
        }
        initComponent();
        binding.productImage.setOnClickListener(new View.OnClickListener() {
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
                PopupMenu popupMenu = new PopupMenu(AddProductActivity.this,binding.productCategory);
                popupMenu.getMenuInflater().inflate(R.menu.category_list_menu,popupMenu.getMenu());
                popupMenu.setGravity(Gravity.END);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        title = menuItem.getTitle().toString();
                        switch (title) {
                            case "Plumbing": binding.productCategory.setText(title);
                                categoryId ="qqM4MWpGJt68FHRgnBI7";break;
                            case "Nut and Bolts": binding.productCategory.setText(title);
                                categoryId ="x11uXtVIGI26k6zSokZI";break;
                            case "Paints": binding.productCategory.setText(title);
                                categoryId ="97lzoB3Npd5Vn7cdsJW9";break;
                            case "Electric Supplies": binding.productCategory.setText(title);
                                categoryId ="FS4xDKrIb2xotx2VMNJq";break;
                            case "Wire and Cable": binding.productCategory.setText(title);
                                categoryId ="LFPMJJAa56dvJEcdMWIj";break;
                            case "Tools": binding.productCategory.setText(title);
                                categoryId ="v2MQlC43M2gxTwZpkbcH";break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        binding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectivity = new InternetConnectivity();
                if (!connectivity.isConnected(AddProductActivity.this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                    builder.setMessage("Please connect to the Internet to Proceed Further").setCancelable(false);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.exit(0);
                        }
                    }).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent in = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                            startActivity(in);
                        }
                    });
                    builder.show();
                } else {
                    final ProgressDialog pd = new ProgressDialog(AddProductActivity.this);
                    pd.setTitle("Saving");
                    pd.setMessage("Please wait");
                    pd.show();
                    String name = binding.productName.getText().toString();
                    String brand = binding.productBrand.getText().toString();
                    Long qtyInStock = Long.parseLong(binding.productQuantity.getText().toString());
                    Double price = Double.parseDouble(binding.productPrice.getText().toString());
                    Double discount = Double.parseDouble(binding.productDiscount.getText().toString());
                    String description = binding.productDescription.getText().toString();
                    String shopKeeperId = "ddfvfjhgbgcv";
                    if (TextUtils.isEmpty(name)) {
                        binding.productName.setError("Enter Product Name");
                        return;
                    }
                    if (TextUtils.isEmpty(brand)) {
                        binding.productBrand.setError("Enter Brand Name");
                        return;
                    }
                    if (TextUtils.isEmpty(qtyInStock.toString())) {
                        binding.productQuantity.setError("Enter Quantity In Stock");
                        return;
                    }
                    if (TextUtils.isEmpty(price.toString())) {
                        binding.productPrice.setError("Enter Price");
                        return;
                    }
                    if (TextUtils.isEmpty(discount.toString())) {
                        discount = 0.0;
                        return;
                    }
                    if (TextUtils.isEmpty(description)) {
                        description = "";
                        return;
                    }
                    if (imageUri != null) {
                        File file = FileUtils.getFile(AddProductActivity.this, imageUri);
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
                                okhttp3.MultipartBody.FORM, String.valueOf(qtyInStock));
                        RequestBody productCategoryId = RequestBody.create(
                                okhttp3.MultipartBody.FORM, categoryId);
                        RequestBody productDescription = RequestBody.create(
                                okhttp3.MultipartBody.FORM, description);
                        RequestBody shopkeeperId = RequestBody.create(okhttp3.MultipartBody.FORM, shopKeeperId);

                        ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                        Call<Product> call = productApi.saveProduct(body, productName, productQtyInStock, productPrice
                                , productDescription, productDiscount, shopkeeperId, productBrand, productCategoryId);

                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                if (response.code() == 200) {
                                    pd.dismiss();
                                    Product product = response.body();
                                    Toast.makeText(AddProductActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (response.code() == 404) {
                                    Toast.makeText(AddProductActivity.this, "404", Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(AddProductActivity.this, "500", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {
                                Toast.makeText(AddProductActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else
                        Toast.makeText(AddProductActivity.this, "Please select Profile pic", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(binding.productImage);
            Toast.makeText(this, ""+imageUri, Toast.LENGTH_SHORT).show();
        }
    }
    private void initComponent() {
        binding.toolbar.setTitle("Add Product");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
