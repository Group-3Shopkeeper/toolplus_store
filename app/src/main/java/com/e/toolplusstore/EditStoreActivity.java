package com.e.toolplusstore;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.e.toolplusstore.apis.StoreService;
import com.e.toolplusstore.beans.Shopkeeper;
import com.e.toolplusstore.databinding.ActivityAddStoreBinding;
import com.google.firebase.auth.FirebaseAuth;
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

public class EditStoreActivity extends AppCompatActivity {
    ActivityAddStoreBinding binding;
    String currentUserId,shopKeeperId,token;
    Uri imageUri;
    ProgressDialog pd;
    InternetConnectivity connectivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStoreBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initComponent();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final SharedPreferences mPref = getSharedPreferences("MyStore",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPref.getString(currentUserId,"");
        final Shopkeeper shopkeeper = gson.fromJson(json, Shopkeeper.class);
        binding.storeNumber.setText(shopkeeper.getContactNumber());
        binding.storeEmail.setText(shopkeeper.getEmail());
        Picasso.get().load(shopkeeper.getImageUrl()).into(binding.storeImage);
        binding.storeName.setText(shopkeeper.getShopName());
        binding.storeAddress.setText(shopkeeper.getAddress());
        binding.btnAddStore.setText("Update Store");
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
                if (!connectivity.isConnected(EditStoreActivity.this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditStoreActivity.this);
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
                    try{
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
                    shopKeeperId = shopkeeper.getShopKeeperId();
                    token = shopkeeper.getToken();
                    if (imageUri != null) {
                        pd = new ProgressDialog(EditStoreActivity.this);
                        pd.setTitle("Updating");
                        pd.setMessage("Please wait");
                        pd.show();
                        File file = FileUtils.getFile(EditStoreActivity.this, imageUri);
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
                        RequestBody shopkeeperId = RequestBody.create(okhttp3.MultipartBody.FORM,shopKeeperId);

                        StoreService.ServiceApi serviceApi = StoreService.getStoreApiInstance();
                        Call<Shopkeeper> call = serviceApi.updateStore(body,storeName,storeNumber,storeAddress,storeEmail,shopkeeperId,storeToken);

                        call.enqueue(new Callback<Shopkeeper>() {
                            @Override
                            public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {
                                if(response.code()==200) {
                                    pd.dismiss();
                                    Shopkeeper s = response.body();
                                    SharedPreferences.Editor editor = mPref.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(s);
                                    editor.putString(currentUserId,json);
                                    editor.commit();
                                    Toast.makeText(EditStoreActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Shopkeeper> call, Throwable t) {

                            }
                        });
                    }else{
                        final ProgressDialog progressDialog = new ProgressDialog(EditStoreActivity.this);
                        progressDialog.setTitle("Updating");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();

                        Shopkeeper s = new Shopkeeper(shopKeeperId,name,number,address, shopkeeper.getImageUrl(),email,token);
                        StoreService.ServiceApi serviceApi = StoreService.getStoreApiInstance();

                        Call<Shopkeeper> call = serviceApi.updateStoreWithoutImage(s);
                        call.enqueue(new Callback<Shopkeeper>() {
                            @Override
                            public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {
                                if(response.code()==200) {
                                    progressDialog.dismiss();
                                    Shopkeeper shopkeeper1 = response.body();
                                    SharedPreferences.Editor editor = mPref.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(shopkeeper1);
                                    editor.putString(currentUserId,json);
                                    editor.commit();
                                    Toast.makeText(EditStoreActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                            @Override
                            public void onFailure(Call<Shopkeeper> call, Throwable t) {

                            }
                        });
                    }
                }catch (Exception e){
                        Toast.makeText(EditStoreActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();}
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(binding.storeImage);
            Toast.makeText(this, ""+imageUri, Toast.LENGTH_SHORT).show();
        }
    }
    private void initComponent() {
        binding.toolbar.setTitle("Store");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
