package com.e.toolplusstore.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class History implements Serializable {

    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("totalAmount")
    @Expose
    private Long totalAmount;
    @SerializedName("shippingStatus")
    @Expose
    private String shippingStatus;
    @SerializedName("orderItem")
    @Expose
    private ArrayList<OrderItem> orderItem = null;

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public History(String orderId, String date, Long totalAmount, String shippingStatus, ArrayList<OrderItem> orderItem) {
        this.orderId = orderId;
        this.date = date;
        this.totalAmount = totalAmount;
        this.shippingStatus = shippingStatus;
        this.orderItem = orderItem;
    }

    public History(String orderId, String date, Long totalAmount, ArrayList<OrderItem> orderItem) {
        this.orderId = orderId;
        this.date = date;
        this.totalAmount = totalAmount;
        this.orderItem = orderItem;
    }

    public History() {
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

    public ArrayList<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(ArrayList<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }
}

