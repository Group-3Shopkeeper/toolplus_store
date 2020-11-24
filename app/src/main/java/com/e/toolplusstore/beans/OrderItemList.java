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
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("qty")
    @Expose
    private Long qty;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("price")
    @Expose
    private Long price;
    @SerializedName("amount")
    @Expose
    private Long amount;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

}

