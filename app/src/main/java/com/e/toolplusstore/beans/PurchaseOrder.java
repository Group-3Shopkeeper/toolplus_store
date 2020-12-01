package com.e.toolplusstore.beans;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseOrder implements Serializable {

    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("totalAmount")
    @Expose
    private Long totalAmount;
    @SerializedName("orderItemList")
    @Expose
    private List<OrderItemList> orderItemList = null;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItemList> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemList> orderItemList) {
        this.orderItemList = orderItemList;
    }

}

