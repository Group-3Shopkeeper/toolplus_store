package com.e.toolplusstore;

import android.Manifest;
import android.app.ProgressDialog;

import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;

import android.text.TextUtils;
import android.view.GestureDetector;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.e.toolplusstore.apis.CategoryService;
import com.e.toolplusstore.apis.ProductNameService;

import com.e.toolplusstore.apis.ProductService;
import com.e.toolplusstore.beans.Category;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.beans.ProductName;
import com.e.toolplusstore.beans.Shopkeeper;
import com.e.toolplusstore.databinding.AddProductScreenBinding;

import com.e.toolplusstore.databinding.OfflineActivityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    AddProductScreenBinding binding;
    Uri imageUri;
    OfflineActivityBinding offlineActivityBinding;
    ArrayList<Category> al;
    Uri secondImageUri ;
    Uri thirdImageuri;
    String categoryId=null,currentUserId;
    ProgressDialog pd;
    ArrayList<String> arrayList;
    MultipartBody.Part body2,body3,body;
    InternetConnectivity connectivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddProductScreenBinding.inflate(LayoutInflater.from(AddProductActivity.this));
        setContentView(binding.getRoot());
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final SharedPreferences mPref = getSharedPreferences("MyStore", MODE_PRIVATE);
        Toast.makeText(this, ""+categoryId, Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        String json = mPref.getString(currentUserId, "");
        arrayList = new ArrayList<>();
        final Shopkeeper shopkeeper = gson.fromJson(json, Shopkeeper.class);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
        }
        initComponent();
        checkInternetConnection();
        binding.tvProductName.setVisibility(View.GONE);
        binding.tvProductQty.setVisibility(View.GONE);
        binding.productName.setHint("Product Name");
        binding.productBrand.setHint("Brand");
        binding.productQuantity.setHint("Quantity");
        binding.productDiscount.setHint("Product Discount (Optional)");
        binding.tvProductDiscount.setVisibility(View.GONE);
        binding.productDescription.setHint("Product Description (Optional)");
        binding.tvProductDescription.setVisibility(View.GONE);
        binding.tvProductBrand.setVisibility(View.GONE);
        binding.productPrice.setHint("Price");
        binding.tvProductPrice.setVisibility(View.GONE);
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

    }
    private void checkInternetConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        changeActivity(isConnected);
    }

    private void changeActivity(boolean isConnected) {
        if(isConnected){
            setContentView(binding.getRoot());
            binding.productCategory.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (MotionEvent.ACTION_UP == event.getAction())
                    {
                        view.performClick();
                        getAllCategories();
                        return true;
                    }
                    return false;
                }
            });
            binding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        try {
                            String name = binding.productName.getText().toString();
                            String brand = binding.productBrand.getText().toString();
                            Integer qtyInStock = Integer.parseInt(binding.productQuantity.getText().toString());
                            Double price = Double.parseDouble(binding.productPrice.getText().toString());
                            String dis = binding.productDiscount.getText().toString();
                            if(dis.equals("")){
                                dis = "0.0";
                            }
                            Double discount = Double.parseDouble(dis);
                            Toast.makeText(AddProductActivity.this, "disc===."+discount, Toast.LENGTH_SHORT).show();
                            String description = binding.productDescription.getText().toString();
                            if (description.equals("")) {
                                description = "";
                            }
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
                        } catch (Exception e) {
                            Log.e("exception ", "========================>>" + e);
                            Toast.makeText(AddProductActivity.this, "please enter all input", Toast.LENGTH_SHORT).show();
                        }
                    }
            });
        }
        else {
            offlineActivityBinding = OfflineActivityBinding.inflate(LayoutInflater.from(this));
            setContentView(offlineActivityBinding.getRoot());
        }
    }

    private void searchProductName(String c) {
        if(c!=null) {
            ProductNameService.ProductNameApi api = ProductNameService.getProductNameApiInstance();
            Call<List<ProductName>> call = api.getProductNameByCategory(c);
            call.enqueue(new Callback<List<ProductName>>() {
                @Override
                public void onResponse(Call<List<ProductName>> call, Response<List<ProductName>> response) {
                    if (response.code() == 200) {
                        Toast.makeText(AddProductActivity.this, ""+response.body(), Toast.LENGTH_SHORT).show();
                        for (ProductName name : response.body()) {
                            String productName = name.getProductName();
                            arrayList.add(productName);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (AddProductActivity.this, android.R.layout.simple_list_item_1, arrayList);
                        //Getting the instance of AutoCompleteTextView
                        binding.productName.setThreshold(1);//will start working from first character
                        binding.productName.setAdapter(adapter);
                    }else if(response.code()==404){
                        Toast.makeText(AddProductActivity.this, "404", Toast.LENGTH_SHORT).show();
                    }
                    else if(response.code()==500){
                        Toast.makeText(AddProductActivity.this, "500", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<ProductName>> call, Throwable t) {

                }
            });
        }
        else{
            Toast.makeText(this, "Select Category First", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver,intentFilter);
        MyApp.getInstance().setConnectivityListener(this);
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        changeActivity(isConnected);
    }
    public void getAllCategories(){
        final PopupMenu popupMenu = new PopupMenu(AddProductActivity.this, binding.productCategory);
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
                                searchProductName(categoryId);
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
