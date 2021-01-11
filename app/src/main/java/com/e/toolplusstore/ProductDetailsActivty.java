package com.e.toolplusstore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.e.toolplusstore.adapter.SliderAdapterExample;
import com.e.toolplusstore.apis.CategoryService;
import com.e.toolplusstore.apis.CommentService;
import com.e.toolplusstore.apis.ProductService;
import com.e.toolplusstore.beans.Category;
import com.e.toolplusstore.beans.Comment;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.beans.SliderItem;
import com.e.toolplusstore.databinding.ActivityProductDetailsBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ProductDetailsActivty extends AppCompatActivity {
    private static final String TAG = "";
    ActivityProductDetailsBinding binding;
    private SliderAdapterExample adapter;
    Product p;
    String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(LayoutInflater.from(this));
        initComponant();
        setContentView(binding.getRoot());
        Intent in = getIntent();
        p = (Product) in.getSerializableExtra("product");

        CategoryService.CategoryApi categoryApi=CategoryService.getCategoryApiInstance();
        Call<ArrayList<Category>> call1=categoryApi.getCategoryList();
        call1.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                ArrayList<Category> arrayList=response.body();
                for (Category c:arrayList){
                    if (c.getCategoryId().equals(p.getCategoryId())){
                        CategoryService.CategoryApi categoryApi1=CategoryService.getCategoryApiInstance();
                        Call<Category> call2=categoryApi1.getCategoryById(c.getCategoryId());
                        call2.enqueue(new Callback<Category>() {
                            @Override
                            public void onResponse(Call<Category> call, Response<Category> response) {
                                Category category=response.body();
                                 categoryName=category.getCategoryName().toString();
                            }

                            @Override
                            public void onFailure(Call<Category> call, Throwable t) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

            }
        });
        if (p.getDiscount() < 1) {
            binding.tvDiscount.setVisibility(View.GONE);
            binding.tvMRP.setVisibility(View.GONE);
            binding.tvPrice.setText("Price : " + p.getPrice() + "");
        } else {
            binding.tvDiscount.setText("Off : (" + p.getDiscount() + "%)");
            binding.tvMRP.setText("MRP : " + p.getPrice() + "");
            binding.tvMRP.setPaintFlags(binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Double ap = p.getPrice() - (p.getPrice() * p.getDiscount() / 100);
            binding.tvPrice.setText("" + ap);
        }
        adapter = new SliderAdapterExample(this);
        binding.iv.setSliderAdapter(adapter);
        binding.iv.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.iv.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.iv.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.iv.setIndicatorSelectedColor(Color.BLUE);
        binding.iv.setIndicatorMargin(12);
        binding.iv.setIndicatorUnselectedColor(Color.GRAY);
        binding.iv.setScrollTimeInSec(3);
        binding.iv.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {

            }
        });
        binding.tvBrand.setText("Brand : " + p.getBrand());
        binding.tvDescription.setText("Description : " + p.getDescription());
        binding.tvProductName.setText("Product Name : " + p.getName());
        binding.tvQtyInStock.setText("Quantity In Stock : " + p.getQtyInStock());
        renewItems(binding.getRoot());

        binding.rlBtnRemove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ProductDetailsActivty.this);
                ab.setTitle("Are You Sure?");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog pd = new ProgressDialog(ProductDetailsActivty.this);
                        pd.setTitle("Deleting");
                        pd.setMessage("Please wait..");
                        ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                        Call<Product> call = productApi.deleteProduct(p.getProductId());
                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                pd.dismiss();
                                startActivity(new Intent(ProductDetailsActivty.this, HomeActivity.class));
                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {
                                Toast.makeText(ProductDetailsActivty.this, "" + t, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                ab.show();
            }
        });
        binding.rlBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ProductDetailsActivty.this, EditProduct.class);
                in.putExtra("product", p);
                in.putExtra("categoryName", categoryName);
                startActivity(in);
            }
        });
        binding.tvViewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ProductDetailsActivty.this, ViewComments.class);
                in.putExtra("product", p);
                startActivity(in);
            }
        });
    }
    public void renewItems(View view) {
        List<SliderItem> sliderItemList = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            SliderItem sliderItem = new SliderItem();
            if (i == 1) {
                sliderItem.setImageUrl(p.getImageUrl());
            } else if (i == 2) {
                    sliderItem.setImageUrl(p.getSecondImageUrl());
            } else if (i == 3) {
                    sliderItem.setImageUrl(p.getThirdImageurl());
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private  void initComponant(){
        binding.productDescriptionToolbar.setTitle("Product Detail");
        setSupportActionBar(binding.productDescriptionToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}