package com.e.toolplusstore.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductName {

    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("productName")
    @Expose
    private String productName;

    public ProductName() {
    }
    public ProductName(String categoryId, String productName) {
        super();
        this.categoryId = categoryId;
        this.productName = productName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}