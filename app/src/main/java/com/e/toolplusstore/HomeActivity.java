package com.e.toolplusstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.e.toolplusstore.adapter.CategoryAdapter;
import com.e.toolplusstore.apis.CategoryService;

import com.e.toolplusstore.beans.Category;
import com.e.toolplusstore.databinding.ActivityHomeBinding;
import com.firebase.ui.auth.AuthUI;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    ActionBarDrawerToggle toggle;
    CategoryAdapter adapter;
    String currentUserId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(LayoutInflater.from(HomeActivity.this));
        setContentView(binding.getRoot());

        InternetConnectivity connectivity = new InternetConnectivity();
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
            Sprite doubleBounce = new Wave();
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
                    if (title.equals("ToolPlus")) {
                        Intent intent=new Intent(HomeActivity.this,EditStoreActivity.class);
                        startActivity(intent);


                    } else if (title.equals("Add Product")) {
                        Intent intent = new Intent(HomeActivity.this, AddProductActivity.class);
                        startActivity(intent);

                    } else if (title.equals("New Order")) {
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
            binding.etSearchBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.btnSeach.setVisibility(View.VISIBLE);
                    binding.btnSeach.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String name = "";
                            Intent intent = new Intent(HomeActivity.this, SearchProductActivity.class);
                            name = binding.etSearchBar.getText().toString();
                            if (TextUtils.isEmpty(name)) {
                                binding.etSearchBar.setError("write here and than search..");
                            } else {
                                intent.putExtra("name", name);
                                startActivity(intent);
                                binding.btnSeach.setVisibility(View.GONE);
                            }
                        }
                    });

                }

            });

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
                Intent intent = new Intent(HomeActivity.this, EditStoreActivity.class);
                startActivity(intent);
            }
            if (title.equals("Logout")) {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                                finish();;
                            }
                        });
            }
            return super.onOptionsItemSelected(item);
        }

    @Override
    protected void onStart() {
        super.onStart();
        final SharedPreferences mPref = getSharedPreferences("MyStore",MODE_PRIVATE);
        if(!mPref.contains(currentUserId))
            startActivity(new Intent(HomeActivity.this, AddStore.class));
    }
}
