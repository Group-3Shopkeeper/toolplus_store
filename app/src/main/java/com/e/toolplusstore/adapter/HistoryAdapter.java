package com.e.toolplusstore.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplusstore.beans.History;
import com.e.toolplusstore.beans.PurchaseOrder;
import com.e.toolplusstore.databinding.OrderHistoryBinding;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.OrderHistoryViewHolder> {
    Context context;
    ArrayList<History> orderList;
    OnRecyclerViewClick listener;
    public HistoryAdapter(Context context, ArrayList<History> orderList){
        this.context=context;
        this.orderList=orderList;
    }

    @NonNull
    @Override
    public HistoryAdapter.OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderHistoryBinding binding=OrderHistoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new OrderHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.OrderHistoryViewHolder holder, int position) {
        History o = orderList.get(position);
        holder.binding.orderId.setText(o.getOrderId());
        holder.binding.date.setText(o.getDate());
        holder.binding.amount.setText(o.getTotalAmount()+"");
        holder.binding.shippingStatus.setText(o.getShippingStatus());
        //Log.e("==============", "onBindViewHolder: "+o.getOrderItemList().size() );
        holder.binding.itemsInOrder.setText(o.getOrderItem().size()+"");
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
                       History o= orderList.get(position);
                       Log.e("orderitem","=============>"+o.getOrderItem());
                        Log.e("==============", "onClick: "+orderList );
                        listener.onItemClick(o,position);
                    }
                }
            });
        }
    }
    public interface OnRecyclerViewClick{
        public void onItemClick(History o, int position);
    }
    public  void setOnItemClickListener(OnRecyclerViewClick listener){
        this.listener=listener;
    }
}
