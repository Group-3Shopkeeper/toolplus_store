package com.e.toolplusstore;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.e.toolplusstore.adapter.CategoryAdapter;
import com.e.toolplusstore.adapter.ShowProductAdapter;
import com.e.toolplusstore.apis.CategoryService;

import com.e.toolplusstore.apis.ProductService;
import com.e.toolplusstore.apis.StoreService;
import com.e.toolplusstore.beans.Category;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.beans.Shopkeeper;
import com.e.toolplusstore.databinding.ActivityHomeBinding;
import com.firebase.ui.auth.AuthUI;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    ActionBarDrawerToggle toggle;
    CategoryAdapter adapter;
    String currentUserId;
    String name = "";
    ShowProductAdapter adapter1;
    ArrayList<Category> al;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(LayoutInflater.from(HomeActivity.this));
        setContentView(binding.getRoot());

        binding.ivsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        binding.ivmic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeechInput(binding.getRoot());
                search();
            }
        });
        InternetConnectivity connectivity = new InternetConnectivity();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (!connectivity.isConnected(HomeActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("Please connect to the Internet to Proceed Further").setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            }).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });
            builder.show();
        } else {
            Sprite doubleBounce = new FadingCircle();
            binding.spinKit.setIndeterminateDrawable(doubleBounce);
            CategoryService.CategoryApi categoryApi = CategoryService.getCategoryApiInstance();
            Call<ArrayList<Category>> call = categoryApi.getCategoryList();
            call.enqueue(new Callback<ArrayList<Category>>() {
                @Override
                public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {

                    ArrayList<Category> categoryList = response.body();

                    adapter = new CategoryAdapter(HomeActivity.this, categoryList);
                    binding.rv.setAdapter(adapter);
                    binding.rv.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
                    binding.spinKit.setVisibility(View.INVISIBLE);
                    adapter.setOnItemClickListener(new CategoryAdapter.OnRecyclerViewClick() {
                        @Override
                        public void onItemClick(Category c, int position) {
                            Intent in = new Intent(HomeActivity.this, ProductActivity.class);
                            in.putExtra("categoryId",c.getCategoryId());
                            in.putExtra("categoryName",c.getCategoryName());
                            startActivity(in);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                }
            });
        }
            binding.nav.setItemIconTintList(null);
            setSupportActionBar(binding.toolbar);
            binding.addProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(HomeActivity.this,AddProductActivity.class);
                    startActivity(in);
                }
            });
            getSupportActionBar().setTitle("ToolPlus");
            toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open, R.string.close);
            toggle.syncState();

            binding.nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    binding.drawer.closeDrawer(GravityCompat.START);
                    String title = item.getTitle().toString();
                    if (title.equals("Edit Store")) {
                        Intent intent=new Intent(HomeActivity.this,EditStoreActivity.class);
                        startActivity(intent);


                    } else if (title.equals("Add Product")) {
                        Intent intent = new Intent(HomeActivity.this, AddProductActivity.class);
                        startActivity(intent);

                    }
                    else if (title.equals("Store Profile")) {
                        Intent intent = new Intent(HomeActivity.this, ViewStoreActivity.class);
                        startActivity(intent);
                    }

                    else if (title.equals("New Order")) {
                        Intent intent = new Intent(HomeActivity.this, NewOrderActivity.class);
                        startActivity(intent);

                    } else if (title.equals("Order History")) {
                        Intent intent = new Intent(HomeActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);

                    } else if (title.equals("Contact Us")) {
                        Toast.makeText(HomeActivity.this, "contact us", Toast.LENGTH_SHORT).show();
                    } else if (title.equals("About Us")) {
                        Toast.makeText(HomeActivity.this, "about us", Toast.LENGTH_SHORT).show();
                    } else if (title.equals("Help Us")) {
                        Toast.makeText(HomeActivity.this, "help us", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });

    }
    public void  search(){

                binding.etSearchBar.setVisibility(View.VISIBLE);
                binding.etSearchBar.setFocusableInTouchMode(true);
                binding.etSearchBar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        name=s.toString();
                        binding.addProduct.setVisibility(View.GONE);
                        binding.rv.setVisibility(View.GONE);
                        binding.rv1.setVisibility(View.VISIBLE);
                        searchProduct();
                        if (name.isEmpty()){
                            binding.rv.setVisibility(View.VISIBLE);
                            binding.rv1.setVisibility(View.GONE);
                            binding.etSearchBar.setVisibility(View.GONE);
                            binding.addProduct.setVisibility(View.VISIBLE);
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(binding.etSearchBar.getWindowToken(), 0);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                binding.rv.setVisibility(View.VISIBLE);
                binding.rv1.setVisibility(View.GONE);
                binding.addProduct.setVisibility(View.VISIBLE);

            }

    private void searchProduct() {
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        InternetConnectivity connectivity = new InternetConnectivity();
        if (!connectivity.isConnected(HomeActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("Please connect to the Internet to Proceed Further").setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            }).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });
            builder.show();
        } else {
            ProductService.ProductApi productApi = ProductService.getProductApiInstance();
            final Call<ArrayList<Product>> productList = productApi.getProductList(currentUserId,name);
            productList.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().isEmpty()) {
                            ArrayList<Product> productList = response.body();
                            adapter1 = new ShowProductAdapter(HomeActivity.this, productList);
                            binding.rv1.setAdapter(adapter1);
                            binding.rv1.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
                            adapter1.setOnItemClickListener(new ShowProductAdapter.OnRecyclerViewClick() {
                                @Override
                                public void onItemClick(final Product product, int position) {
                                    final Intent intent=new Intent(HomeActivity.this,ProductDetailsActivty.class);
                                    intent.putExtra("product", product);
                                    startActivity(intent);
                                }
                            });
                            binding.spinKitt.setVisibility(View.INVISIBLE);
                        }
                        else {
                            binding.spinKitt.setVisibility(View.INVISIBLE);
                            binding.noMatch.setVisibility(View.VISIBLE);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
        public boolean onCreateOptionsMenu (Menu menu){
            menu.add("Settings");
            menu.add("Logout");

            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item){
            String title = item.getTitle().toString();
            if (title.equals("Settings")) {
                Intent intent = new Intent(HomeActivity.this, ViewStoreActivity.class);
                startActivity(intent);
            }
            if (title.equals("Logout")) {
                final AlertDialog.Builder ab = new AlertDialog.Builder(HomeActivity.this);
                ab.setTitle("Are You Sure?");

                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AuthUI.getInstance()
                                .signOut(HomeActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                                        finish();;
                                    }
                                });
                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                ab.show();
            }
            return super.onOptionsItemSelected(item);
        }

    private void checkUserProfile(){
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final SharedPreferences sp = getSharedPreferences("MyStore",MODE_PRIVATE);
        String id = sp.getString("shpKeeperId","Not found");

        if(!id.equals("Not found")){
                if(!id.equals(currentUserId)){
                    StoreService.ServiceApi storeApi = StoreService.getStoreApiInstance();
                    Call<Shopkeeper> call = storeApi.getStoreProfile(currentUserId);
                    call.enqueue(new Callback<Shopkeeper>() {
                        @Override
                        public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {
                            if(response.code() == 200){
                                Shopkeeper shopkeeper = response.body();
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("shopKeeperId",shopkeeper.getShopKeeperId());
                                editor.putString("address",shopkeeper.getAddress());
                                editor.putString("email",shopkeeper.getEmail());
                                editor.putString("contact",shopkeeper.getContactNumber());
                                editor.putString("token",shopkeeper.getToken());
                                editor.putString("imageUrl",shopkeeper.getImageUrl());
                                editor.putString("name",shopkeeper.getShopName());
                                editor.commit();
                            }
                            else if(response.code() == 404){
                                sendUserToAddStoreActivity();
                            }
                        }

                        @Override
                        public void onFailure(Call<Shopkeeper> call, Throwable t) {

                        }
                    });
                }
            }
        else{

            StoreService.ServiceApi storeApi = StoreService.getStoreApiInstance();
            Call<Shopkeeper> call = storeApi.getStoreProfile(currentUserId);
            call.enqueue(new Callback<Shopkeeper>() {
                @Override
                public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {
                    if(response.code() == 200){
                        Shopkeeper shopkeeper = response.body();

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("shopKeeperId",shopkeeper.getShopKeeperId());
                        editor.putString("address",shopkeeper.getAddress());
                        editor.putString("email",shopkeeper.getEmail());
                        editor.putString("contact",shopkeeper.getContactNumber());
                        editor.putString("token",shopkeeper.getToken());
                        editor.putString("imageUrl",shopkeeper.getImageUrl());
                        editor.putString("name",shopkeeper.getShopName());
                        editor.commit();

                    }
                    else if(response.code() == 404){
                        sendUserToAddStoreActivity();
                    }
                }

                @Override
                public void onFailure(Call<Shopkeeper> call, Throwable t) {

                }
            });
        }

    }
    private void sendUserToAddStoreActivity(){
        Intent in = new Intent(this,AddStore.class);
        startActivity(in);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserProfile();
    }
    public void getSpeechInput(View view) {
        binding.etSearchBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(intent, 10);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if (resultCode==RESULT_OK && data != null){
                    ArrayList<String> al=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    binding.etSearchBar.setText(al.get(0));
                }
                break;
        }
    }
}
