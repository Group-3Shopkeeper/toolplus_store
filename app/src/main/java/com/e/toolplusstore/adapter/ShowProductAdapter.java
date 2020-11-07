package com.e.toolplusstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.databinding.ShowCategoryBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowProductAdapter  extends RecyclerView.Adapter<ShowProductAdapter.ShowProductViewHolder> {
    Context context;
    ArrayList<Product> productList;
    public  ShowProductAdapter(Context context, ArrayList<Product> productList){
        this.context=context;
        this.productList=productList;
    }

    @NonNull
    @Override
    public ShowProductAdapter.ShowProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShowCategoryBinding binding=ShowCategoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ShowProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowProductAdapter.ShowProductViewHolder holder, int position) {
        Product p=productList.get(position);
        Picasso.get().load(p.getImageUrl()).into(holder.binding.ivCategory);
        holder.binding.tvCategoryName.setText(p.getName());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public  class ShowProductViewHolder extends RecyclerView.ViewHolder{
       ShowCategoryBinding binding;
        public ShowProductViewHolder(@NonNull ShowCategoryBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
