package com.e.toolplusstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.toolplusstore.apis.OrderService;
import com.e.toolplusstore.beans.PurchaseOrder;
import com.e.toolplusstore.beans.Shopkeeper;
import com.e.toolplusstore.databinding.ViewStoreBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewStoreActivity extends AppCompatActivity {
    ViewStoreBinding binding;
    String currentUserId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewStoreBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initComponent();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final SharedPreferences mPref = getSharedPreferences("MyStore", MODE_PRIVATE);
        binding.tvStoreNumber.setText(mPref.getString("contact","Contact number"));
        binding.tvStoreEmail.setText(mPref.getString("email","Email"));
        Picasso.get().load(mPref.getString("imageUrl","")).into(binding.cvstoreImage);
        binding.tvStoreName.setText(mPref.getString("name","Name"));
        binding.tvStoreAddress.setText(mPref.getString("address","address"));
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ViewStoreActivity.this,EditStoreActivity.class);
                startActivity(in);
            }
        });
        OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
        Call<ArrayList<PurchaseOrder>> call = orderApi.getOrderList(currentUserId);
        call.enqueue(new Callback<ArrayList<PurchaseOrder>>() {
            @Override
            public void onResponse(Call<ArrayList<PurchaseOrder>> call, Response<ArrayList<PurchaseOrder>> response) {
                Log.e("Response", "=====>" + response.code());
                long sum=0,count=0;
                if (response.code() == 200) {
                    final ArrayList<PurchaseOrder> orderList = response.body();
                    for(PurchaseOrder order : orderList){
                        long totalAmount = order.getTotalAmount();
                        sum = sum+totalAmount;
                        count++;
                    }
                    binding.tvBillingAmount.setText(""+sum);
                    binding.tvOrderCount.setText(""+count);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PurchaseOrder>> call, Throwable t) {

            }
        });
    }
    private void initComponent() {
        binding.toolbar.setTitle("My Store");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
