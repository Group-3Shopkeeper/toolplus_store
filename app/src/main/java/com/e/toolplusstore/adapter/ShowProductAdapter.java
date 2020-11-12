package com.e.toolplusstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplusstore.beans.Category;
import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.databinding.ShowCategoryBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShowProductAdapter  extends RecyclerView.Adapter<ShowProductAdapter.ShowProductViewHolder> {
    Context context;
    ArrayList<Product> productList;
    OnRecyclerViewClick listener;
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
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION && listener!=null ){
                        Product p=productList.get(position);
                        listener.onItemClick(p,position);
                    }
                }
            });
        }
    }
    public interface OnRecyclerViewClick{
        public void onItemClick(Product product, int position);
    }
    public  void setOnItemClickListener(OnRecyclerViewClick listener){
        this.listener=listener;
    }
}
