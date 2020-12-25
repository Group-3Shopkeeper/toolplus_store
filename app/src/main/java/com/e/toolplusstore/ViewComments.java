package com.e.toolplusstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.e.toolplusstore.adapter.ViewCommentAdapter;
import com.e.toolplusstore.apis.CommentService;
import com.e.toolplusstore.beans.Comment;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.databinding.ActivityViewCommentsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewComments extends AppCompatActivity {
    ActivityViewCommentsBinding binding;
    ViewCommentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewCommentsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initComponent();
        Intent in = getIntent();
        Product p = (Product) in.getSerializableExtra("product");
        if(p.getDiscount()<1) {
            binding.tvDiscount.setVisibility(View.GONE);
            binding.tvMRP.setVisibility(View.GONE);
            binding.tvPrice.setText("Price : "+p.getPrice()+"");
        }else{
            binding.tvDiscount.setText("Off : ("+p.getDiscount()+"%)");
            binding.tvMRP.setText("MRP : "+p.getPrice()+"");
            binding.tvMRP.setPaintFlags(binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Double ap = p.getPrice() -(p.getPrice()*p.getDiscount()/100);
            binding.tvPrice.setText(""+ap);
        }
        Picasso.get().load(p.getImageUrl()).into(binding.iv);
        binding.tvProductName.setText("Product Name : "+p.getName());
        CommentService.CommentApi commentApi = CommentService.getCommentApiInstance();
        Call<List<Comment>> c = commentApi.getProductByComment(p.getProductId());
        c.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.code()==200) {
                    List<Comment> arrayList = response.body();

                    adapter = new ViewCommentAdapter(ViewComments.this,arrayList);
                    binding.rv.setAdapter(adapter);
                    binding.rv.setLayoutManager(new LinearLayoutManager(ViewComments.this));
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }
    private void initComponent() {
        binding.toolbar.setTitle("Comments");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}