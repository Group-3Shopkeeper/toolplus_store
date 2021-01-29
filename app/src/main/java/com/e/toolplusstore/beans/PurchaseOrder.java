package com.e.toolplusstore.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseOrder implements Serializable {

    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("totalAmount")
    @Expose
    private Long totalAmount;
    @SerializedName("orderItemList")
    @Expose
    private ArrayList<OrderItemList> orderItemList = null;

    public PurchaseOrder(String orderId, String date, Long totalAmount, ArrayList<OrderItemList> orderItemList) {
        this.orderId = orderId;
        this.date = date;
        this.totalAmount = totalAmount;
        this.orderItemList = orderItemList;
    }

    public PurchaseOrder() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<OrderItemList> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(ArrayList<OrderItemList> orderItemList) {
        this.orderItemList = orderItemList;
    }
}

