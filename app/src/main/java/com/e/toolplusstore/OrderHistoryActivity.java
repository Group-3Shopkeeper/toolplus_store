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

import com.e.toolplusstore.adapter.HistoryAdapter;
import com.e.toolplusstore.adapter.OrderHistoryAdapter;
import com.e.toolplusstore.apis.OrderService;
import com.e.toolplusstore.beans.History;
import com.e.toolplusstore.beans.OrderItem;
import com.e.toolplusstore.beans.OrderItemList;
import com.e.toolplusstore.beans.PurchaseOrder;
import com.e.toolplusstore.databinding.ActivityOrderHistoryBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    ActivityOrderHistoryBinding binding;
    HistoryAdapter adapter;
    String currentUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initComponent();
        Sprite doubleBounce = new FadingCircle();
        binding.spinKit.setIndeterminateDrawable(doubleBounce);
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
        Call<ArrayList<History>> call = orderApi.getOrderList(currentUserId);


        call.enqueue(new Callback<ArrayList<History>>() {
            @Override
            public void onResponse(Call<ArrayList<History>> call, Response<ArrayList<History>> response) {
                Log.e("Response","=====>"+response.code());
                if (response.body().isEmpty()){
                    binding.spinKit.setVisibility(View.INVISIBLE);
                    binding.notFound.setVisibility(View.VISIBLE);
                }
                if(response.code() == 200) {
                    final ArrayList<History> orderList = response.body();

                    if(orderList.size()!=0) {
                        Log.e("====", "order" + orderList);
                        adapter = new HistoryAdapter(OrderHistoryActivity.this, orderList);
                        binding.rvOrderHistory.setAdapter(adapter);
                        binding.rvOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
                        binding.spinKit.setVisibility(View.INVISIBLE);
                        adapter.setOnItemClickListener(new HistoryAdapter.OnRecyclerViewClick() {
                            @Override
                            public void onItemClick(History o, int position) {
                                Log.e("history ","============>"+o.getOrderId());
                                ArrayList<OrderItem> itemLists = o.getOrderItem();
                                Log.e("sljflsdjfls ","======>"+itemLists.size());
                                Intent intent2 = new Intent(OrderHistoryActivity.this,OrderItemActivity.class);
                                intent2.putExtra("orderItem", itemLists);
                                startActivity(intent2);

                            }
                        });
                    }
                }
                if(response.code()==404){
                    Toast.makeText(OrderHistoryActivity.this, "400", Toast.LENGTH_SHORT).show();
                }
                if (response.code()==500){
                    Toast.makeText(OrderHistoryActivity.this, "500", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ArrayList<History>> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void initComponent() {
        binding.toolbar.setTitle("Purchase Order");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}


