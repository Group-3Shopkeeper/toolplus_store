package com.e.toolplusstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplusstore.beans.OrderItemList;
import com.e.toolplusstore.beans.PurchaseOrder;
import com.e.toolplusstore.databinding.OrderItemBinding;
import com.squareup.picasso.Picasso;


import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    Context context;
    List<OrderItemList> orderItems;
    public OrderItemAdapter(Context context, List<OrderItemList> orderItems){
        this.context = context;
        this.orderItems = orderItems;
    }
    @NonNull
    @Override
    public OrderItemAdapter.OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderItemBinding binding = OrderItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new OrderItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.OrderItemViewHolder holder, int position) {
    OrderItemList p = orderItems.get(position);
    holder.binding.tv1.setText("Product Name : " +p.getName());
    holder.binding.tv2.setText("Quantity : "+p.getQty());
    holder.binding.tv3.setText("Price : "+p.getPrice());
    holder.binding.tv4.setText("Amount : "+p.getTotal());
    Picasso.get().load(p.getImageUrl()).into(holder.binding.iv);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder {
        OrderItemBinding binding;
        public OrderItemViewHolder(@NonNull OrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}