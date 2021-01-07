package com.e.toolplusstore.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderItemList implements Serializable {

    @SerializedName("orderItemId")
    @Expose
    private String orderItemId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("shopKeeperId")
    @Expose
    private String shopKeeperId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("qty")
    @Expose
    private int qty;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("total")
    @Expose
    private double total;

    public OrderItemList() {
    }

    public OrderItemList(String orderItemId, String productId, String shopKeeperId, String name, int qty, String imageUrl, double price, double total) {
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.shopKeeperId = shopKeeperId;
        this.name = name;
        this.qty = qty;
        this.imageUrl = imageUrl;
        this.price = price;
        this.total = total;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getShopKeeperId() {
        return shopKeeperId;
    }

    public void setShopKeeperId(String shopKeeperId) {
        this.shopKeeperId = shopKeeperId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

