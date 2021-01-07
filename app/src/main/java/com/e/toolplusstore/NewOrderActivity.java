package com.e.toolplusstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e.toolplusstore.adapter.OrderHistoryAdapter;
import com.e.toolplusstore.apis.OrderService;
import com.e.toolplusstore.beans.OrderItemList;
import com.e.toolplusstore.beans.PurchaseOrder;
import com.e.toolplusstore.databinding.ActivityOrderHistoryBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOrderActivity extends AppCompatActivity {
    ActivityOrderHistoryBinding binding;
    OrderHistoryAdapter adapter;
    String shopKeeperId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOrderHistoryBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initComponent();
        Sprite doubleBounce = new Wave();
        binding.spinKit.setIndeterminateDrawable(doubleBounce);
        shopKeeperId= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        Log.e("==================",shopKeeperId);
        OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
        Call<ArrayList<PurchaseOrder>> call = orderApi.getNewOreder(shopKeeperId);
        call.enqueue(new Callback<ArrayList<PurchaseOrder>>() {
            @Override
            public void onResponse(Call<ArrayList<PurchaseOrder>> call, Response<ArrayList<PurchaseOrder>> response) {

                Log.e("Response", "=====>" + response.code());
                if (response.body().isEmpty()){
                    binding.spinKit.setVisibility(View.INVISIBLE);
                    binding.notavailble.setVisibility(View.VISIBLE);
                }
                if (response.code() == 200) {
                    final ArrayList<PurchaseOrder> orderList = response.body();
                    Log.e("====", "" + orderList);
                    adapter = new OrderHistoryAdapter(NewOrderActivity.this, orderList);
                    binding.rvOrderHistory.setAdapter(adapter);
                    binding.rvOrderHistory.setLayoutManager(new LinearLayoutManager(NewOrderActivity.this));
                    binding.spinKit.setVisibility(View.INVISIBLE);
                    adapter.setOnItemClickListener(new OrderHistoryAdapter.OnRecyclerViewClick() {
                        @Override
                        public void onItemClick(PurchaseOrder o, int position) {
                            List<OrderItemList> itemList = o.getOrderItem();
                            Intent in = new Intent(NewOrderActivity.this, OrderItemActivity.class);
                            in.putExtra("orderItem", (Serializable) itemList);
                            startActivity(in);
                        }
                    });
                }
                if (response.code() == 404) {
                    Toast.makeText(NewOrderActivity.this, "400", Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 500) {
                    Toast.makeText(NewOrderActivity.this, "500", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<PurchaseOrder>> call, Throwable t) {
                Toast.makeText(NewOrderActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initComponent() {
        binding.toolbar.setTitle("New Order");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
