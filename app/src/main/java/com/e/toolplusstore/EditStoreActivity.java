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
import com.e.toolplusstore.beans.Store;
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
        final Store store = gson.fromJson(json,Store.class);
        binding.storeNumber.setText(store.getContactNumber());
        binding.storeEmail.setText(store.getEmail());
        Picasso.get().load(store.getImageUrl()).into(binding.storeImage);
        binding.storeName.setText(store.getShopName());
        binding.storeAddress.setText(store.getAddress());
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
                    shopKeeperId = store.getShopKeeperId();
                    token = store.getToken();
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
                        Call<Store> call = serviceApi.updateStore(body,storeName,storeNumber,storeAddress,storeEmail,shopkeeperId,storeToken);

                        call.enqueue(new Callback<Store>() {
                            @Override
                            public void onResponse(Call<Store> call, Response<Store> response) {
                                if(response.code()==200) {
                                    pd.dismiss();
                                    Store s = response.body();
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
                            public void onFailure(Call<Store> call, Throwable t) {

                            }
                        });
                    }else{
                        Toast.makeText(EditStoreActivity.this, "Please select store image", Toast.LENGTH_SHORT).show();

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
