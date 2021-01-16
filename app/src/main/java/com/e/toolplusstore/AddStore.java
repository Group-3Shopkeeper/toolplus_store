package com.e.toolplusstore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import static android.content.ContentValues.TAG;
import com.e.toolplusstore.apis.StoreService;
import com.e.toolplusstore.beans.Shopkeeper;
import com.e.toolplusstore.databinding.ActivityAddStoreBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStore extends AppCompatActivity {
     ActivityAddStoreBinding binding;
     String currentUserId;
     InternetConnectivity connectivity;
     Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStoreBinding.inflate(LayoutInflater.from(this));
        final SharedPreferences mPref = getSharedPreferences("MyStore",MODE_PRIVATE);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setContentView(binding.getRoot());
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},11);
        }
        initComponent();
        binding.storeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                startActivityForResult(Intent.createChooser(in,"Select image"),111);
            }
        });
        binding.btnAddStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectivity = new InternetConnectivity();
                if (!connectivity.isConnected(AddStore.this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddStore.this);
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
                    String address = binding.storeAddress.getText().toString();
                    String name = binding.storeName.getText().toString();
                    String email = binding.storeEmail.getText().toString();
                    String number = binding.storeNumber.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        binding.storeName.setError("Enter Store Name");
                        return;
                    }
                    if (TextUtils.isEmpty(address)) {
                        binding.storeAddress.setError("Enter Address");
                        return;
                    }
                    if (TextUtils.isEmpty(email)) {
                        binding.storeEmail.setError("Enter Email");
                        return;
                    }
                    if (TextUtils.isEmpty(number)) {
                        binding.storeNumber.setError("Enter Number");
                        return;
                    }
                    final ProgressDialog pd = new ProgressDialog(AddStore.this);
                    pd.setTitle("Saving");
                    pd.setMessage("Please wait");
                    pd.show();
                    String token = FirebaseInstanceId.getInstance().getToken();
                    if (imageUri != null) {
                        File file = FileUtils.getFile(AddStore.this, imageUri);
                        RequestBody requestFile =
                                RequestBody.create(
                                        MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))),
                                        file
                                );

                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                        RequestBody storeName = RequestBody.create(okhttp3.MultipartBody.FORM, name);
                        RequestBody storeNumber = RequestBody.create(okhttp3.MultipartBody.FORM, number);
                        RequestBody storeEmail = RequestBody.create(okhttp3.MultipartBody.FORM, email);
                        RequestBody storeAddress = RequestBody.create(okhttp3.MultipartBody.FORM, address);
                        RequestBody storeToken = RequestBody.create(okhttp3.MultipartBody.FORM, token);
                        RequestBody shopKeeperId = RequestBody.create(okhttp3.MultipartBody.FORM, currentUserId);


                        StoreService.ServiceApi serviceApi = StoreService.getStoreApiInstance();
                        Call<Shopkeeper> call = serviceApi.saveStore(body, storeName, storeNumber, storeAddress, storeEmail,shopKeeperId, storeToken);

                        call.enqueue(new Callback<Shopkeeper>() {
                            @Override
                            public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {
                                if (response.code() == 200) {
                                    pd.dismiss();
                                    Shopkeeper shopkeeper = response.body();
                                    SharedPreferences.Editor editor = mPref.edit();
                                    //Gson gson = new Gson();
                                    //String json = gson.toJson(shopkeeper);
                                    editor.putString("userId",shopkeeper.getShopKeeperId());
                                    editor.putString("address",shopkeeper.getAddress());
                                    editor.putString("email",shopkeeper.getEmail());
                                    editor.putString("contact",shopkeeper.getContactNumber());
                                    editor.putString("token",shopkeeper.getToken());
                                    editor.putString("imageUrl",shopkeeper.getImageUrl());
                                    editor.putString("name",shopkeeper.getShopName());
                                    editor.commit();
                                    Toast.makeText(AddStore.this, "Saved", Toast.LENGTH_SHORT).show();
                                    Intent inte=new Intent(AddStore.this,HomeActivity.class);
                                    startActivity(inte);
                                    finish();
                                    Log.e("=========", "200");
                                } else if (response.code() == 404) {
                                    pd.dismiss();
                                    Toast.makeText(AddStore.this, "404", Toast.LENGTH_SHORT).show();
                                    Log.e("=========", "404");
                                } else if (response.code() == 500) {
                                    pd.dismiss();
                                    Toast.makeText(AddStore.this, "500", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onResponse:========================> "+response.errorBody());
                                }
                            }

                            @Override
                            public void onFailure(Call<Shopkeeper> call, Throwable t) {
                                pd.dismiss();
                                Toast.makeText(AddStore.this, "" + t, Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onFailure: =====================>"+t );
                            }
                        });
                    } else {
                        Toast.makeText(AddStore.this, "Please select store image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void initComponent() {
        binding.toolbar.setTitle("Add Store");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(binding.storeImage);
        }
    }
}