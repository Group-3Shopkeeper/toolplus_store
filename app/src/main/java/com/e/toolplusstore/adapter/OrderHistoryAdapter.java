package com.e.toolplusstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplusstore.beans.PurchaseOrder;
import com.e.toolplusstore.databinding.OrderHistoryBinding;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
    Context context;
    ArrayList<PurchaseOrder> orderList;
    OnRecyclerViewClick listener;
    public OrderHistoryAdapter(Context context, ArrayList<PurchaseOrder> orderList){
        this.context=context;
        this.orderList=orderList;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderHistoryBinding binding=OrderHistoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new OrderHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.OrderHistoryViewHolder holder, int position) {
        PurchaseOrder o = orderList.get(position);
        holder.binding.orderId.setText(o.getOrderId());
        holder.binding.date.setText(o.getDate());
        holder.binding.amount.setText(o.getTotalAmount()+"");



    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }
    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder{
        OrderHistoryBinding binding;
        public OrderHistoryViewHolder(@NonNull OrderHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION && listener!=null ){
                       PurchaseOrder o= orderList.get(position);
                        listener.onItemClick(o,position);
                    }
                }
            });
        }
    }
    public interface OnRecyclerViewClick{
        public void onItemClick(PurchaseOrder o, int position);
    }
    public  void setOnItemClickListener(OnRecyclerViewClick listener){
        this.listener=listener;
    }
}
