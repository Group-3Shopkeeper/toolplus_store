package com.e.toolplusstore;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.e.toolplusstore.apis.ProductService;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.beans.Shopkeeper;
import com.e.toolplusstore.databinding.AddProductScreenBinding;
import com.e.toolplusstore.utility.OnSwipeTouchListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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
    Uri secondImageUri ;
    Uri thirdImageuri;
    String title,categoryId=null,currentUserId;
    URL url;
    ProgressDialog pd;
    MultipartBody.Part body2,body3,body;
    InternetConnectivity connectivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddProductScreenBinding.inflate(LayoutInflater.from(AddProductActivity.this));
        setContentView(binding.getRoot());
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final SharedPreferences mPref = getSharedPreferences("MyStore",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPref.getString(currentUserId,"");
        try {
            url = new URL("https://image.shutterstock.com/image-vector/vector-graphic-no-thumbnail-symbol-260nw-1391095985.jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final Shopkeeper shopkeeper = gson.fromJson(json, Shopkeeper.class);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},11);
        }
        initComponent();
        binding.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in,"Select image"),111);
            }
        });
        binding.iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in,"Select image"),112);
            }
        });
        binding.iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in,"Select image"),113);
                //binding.btnAddMore.setVisibility(View.GONE);
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
                                categoryId ="qqM4MWpGJt68FHRgnBI7";
                                break;
                            case "Nut and Bolts": binding.productCategory.setText(title);
                                categoryId ="x11uXtVIGI26k6zSokZI";
                                break;
                            case "Paints": binding.productCategory.setText(title);
                                categoryId ="97lzoB3Npd5Vn7cdsJW9";
                                break;
                            case "Electric Supplies": binding.productCategory.setText(title);
                                categoryId ="FS4xDKrIb2xotx2VMNJq";
                                break;
                            case "Wire and Cable": binding.productCategory.setText(title);
                                categoryId ="LFPMJJAa56dvJEcdMWIj";
                                break;
                            case "Tools": binding.productCategory.setText(title);
                                categoryId ="v2MQlC43M2gxTwZpkbcH";
                                break;
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
                            //Intent in = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                            //startActivity(in);
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setClassName("com.android.phone","com.android.phone.NetworkSetting");
                            startActivity(intent);
                        }
                    });
                    builder.show();
                } else {
                    try {
                        String name = binding.productName.getText().toString();
                        String brand = binding.productBrand.getText().toString();
                        Integer qtyInStock = Integer.parseInt(binding.productQuantity.getText().toString());
                        Double price = Double.parseDouble(binding.productPrice.getText().toString());
                        Double discount = Double.parseDouble(binding.productDiscount.getText().toString());
                        String description = binding.productDescription.getText().toString();
                        String shopKeeperId = currentUserId;
                        if (TextUtils.isEmpty(name)) {
                            binding.productName.setError("Enter Product Name");
                            return;
                        }
                        if (categoryId == null) {
                            Toast.makeText(AddProductActivity.this, "SELECT ATLEAST ONE CATEGORY", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(brand)) {
                            binding.productBrand.setError("Enter Brand Name");
                            return;
                        }
                        if (qtyInStock <= 0) {
                            binding.productQuantity.setError("Quantity can't be zero");
                            return;
                        }
                        if (TextUtils.isEmpty(price.toString())) {
                            binding.productPrice.setError("Enter Price");
                            return;
                        }
                        if (TextUtils.isEmpty(discount.toString())) {
                            discount = 0.0;
                        }
                        if (TextUtils.isEmpty(description)) {
                            description = "";
                        }

                        if (imageUri != null && secondImageUri != null && thirdImageuri != null) {
                            pd = new ProgressDialog(AddProductActivity.this);
                            pd.setTitle("Saving");
                            pd.setMessage("Please wait");
                            pd.show();
                            if (imageUri != null && secondImageUri != null && thirdImageuri != null) {
                                File file = FileUtils.getFile(AddProductActivity.this, imageUri);
                                RequestBody requestFile =
                                        RequestBody.create(
                                                MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))),
                                                file
                                        );
                                body =
                                        MultipartBody.Part.createFormData("file", file.getName(), requestFile);


                                File file2 = FileUtils.getFile(AddProductActivity.this, secondImageUri);
                                RequestBody requestFile2 =
                                        RequestBody.create(
                                                MediaType.parse(Objects.requireNonNull(getContentResolver().getType(secondImageUri))),
                                                file2
                                        );
                                body2 =
                                        MultipartBody.Part.createFormData("file2", file2.getName(), requestFile2);

                                File file3 = FileUtils.getFile(AddProductActivity.this, thirdImageuri);
                                RequestBody requestFile3 =
                                        RequestBody.create(
                                                MediaType.parse(Objects.requireNonNull(getContentResolver().getType(thirdImageuri))),
                                                file3
                                        );
                                body3 =
                                        MultipartBody.Part.createFormData("file3", file3.getName(), requestFile3);

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
                                Call<Product> call = productApi.saveProduct(body, body2, body3, productName, productQtyInStock, productPrice
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
                                            pd.dismiss();
                                            Toast.makeText(AddProductActivity.this, "404", Toast.LENGTH_SHORT).show();
                                        } else if (response.code() == 500) {
                                            Toast.makeText(AddProductActivity.this, "500", Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Product> call, Throwable t) {
                                        Toast.makeText(AddProductActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(AddProductActivity.this, "Select All Images", Toast.LENGTH_SHORT).show();
                        }
                    }catch(NumberFormatException e){
                        Toast.makeText(AddProductActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

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
        if (requestCode == 112 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            secondImageUri = data.getData();
            Picasso.get().load(secondImageUri).into(binding.iv2);
            Toast.makeText(this, ""+secondImageUri, Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 113 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            thirdImageuri = data.getData();
            Picasso.get().load(thirdImageuri).into(binding.iv3);
            Toast.makeText(this, ""+thirdImageuri, Toast.LENGTH_SHORT).show();
        }
    }
    private void initComponent() {
        binding.toolbar.setTitle("Add Product");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
