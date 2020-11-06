package com.e.toolplusstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
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
import com.e.toolplusstore.adapter.ShowProductAdapter;
import com.e.toolplusstore.apis.CategoryService;

import com.e.toolplusstore.beans.Category;
import com.e.toolplusstore.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    ActionBarDrawerToggle toggle;
    CategoryAdapter adapter;
    ShowProductAdapter adapter1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(LayoutInflater.from(HomeActivity.this));
        setContentView(binding.getRoot());

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
            CategoryService.CategoryApi categoryApi = CategoryService.getCategoryApiInstance();
            Call<ArrayList<Category>> call = categoryApi.getCategoryList();
            call.enqueue(new Callback<ArrayList<Category>>() {
                @Override
                public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                    ArrayList<Category> categoryList = response.body();

                    adapter = new CategoryAdapter(HomeActivity.this, categoryList);
                    binding.rv.setAdapter(adapter);
                    binding.rv.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
                    adapter.setOnItemClickListener(new CategoryAdapter.OnRecyclerViewClick() {
                        @Override
                        public void onItemClick(Category c, int position) {
                            Toast.makeText(HomeActivity.this, c.getCategoryName(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(HomeActivity.this, "add product", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(HomeActivity.this, "toolpus", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
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
                Intent intent = new Intent(HomeActivity.this, ViewProfileActivity.class);
                startActivity(intent);
            } else if (title.equals("Logout")) {

            }
            return super.onOptionsItemSelected(item);
        }


}
