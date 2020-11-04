package com.e.toolplusstore.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("shopKeeperId")
    @Expose
    private String shopKeeperId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("qtyInStock")
    @Expose
    private Long qtyInStock;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;

    public Product() {
    }
    public Product(String productId, String categoryId, String shopKeeperId, String name, Double price, Double discount, String brand, Long qtyInStock, String imageUrl, String description, Long timestamp) {
        super();
        this.productId = productId;
        this.categoryId = categoryId;
        this.shopKeeperId = shopKeeperId;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.brand = brand;
        this.qtyInStock = qtyInStock;
        this.imageUrl = imageUrl;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(Long qtyInStock) {
        this.qtyInStock = qtyInStock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}