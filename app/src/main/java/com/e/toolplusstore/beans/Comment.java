
package com.e.toolplusstore.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("shopKeeperId")
    @Expose
    private String shopKeeperId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("commentId")
    @Expose
    private String commentId;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("userImageUrl")
    @Expose
    private String userImageUrl;
    @SerializedName("rating")
    @Expose
    private int rating;

    public Comment() {
    }

    public Comment(String shopKeeperId, String userId, String commentId, String userName, Long timestamp, String productId, String comment, String userImageUrl, int rating) {
        super();
        this.shopKeeperId = shopKeeperId;
        this.userId = userId;
        this.commentId = commentId;
        this.userName = userName;
        this.timestamp = timestamp;
        this.productId = productId;
        this.comment = comment;
        this.userImageUrl = userImageUrl;
        this.rating = rating;
    }

    public String getShopKeeperId() {
        return shopKeeperId;
    }

    public void setShopKeeperId(String shopKeeperId) {
        this.shopKeeperId = shopKeeperId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
