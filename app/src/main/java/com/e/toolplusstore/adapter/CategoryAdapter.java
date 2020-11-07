package com.e.toolplusstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplusstore.beans.Category;
import com.e.toolplusstore.databinding.ShowCategoryBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    ArrayList<Category> categoryList;
    Context context;
    OnRecyclerViewClick listener;
    public CategoryAdapter(Context context,ArrayList<Category> categoryList){
        this.context=context;
        this.categoryList=categoryList;
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShowCategoryBinding binding=ShowCategoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category c=categoryList.get(position);
        Picasso.get().load(c.getImageUrl()).into(holder.binding.ivCategory);
        holder.binding.tvCategoryName.setText(c.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ShowCategoryBinding binding;
        public CategoryViewHolder(ShowCategoryBinding binding) {
            super(binding.getRoot());
             this.binding=binding;
             binding.getRoot().setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     int position=getAdapterPosition();
                     if (position!=RecyclerView.NO_POSITION && listener!=null ){
                         Category c=categoryList.get(position);
                         listener.onItemClick(c,position);
                     }
                 }
             });
        }
    }
    public interface OnRecyclerViewClick{
       public void onItemClick(Category c,int position);
    }
    public  void setOnItemClickListener(OnRecyclerViewClick listener){
        this.listener=listener;
    }
}
