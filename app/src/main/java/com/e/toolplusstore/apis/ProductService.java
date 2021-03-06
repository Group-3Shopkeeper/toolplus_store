package com.e.toolplusstore.apis;

import com.e.toolplusstore.beans.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ProductService {
    public static ProductApi productApi;
    public  static ProductApi getProductApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .build();
                 Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(productApi==null)
            productApi=retrofit.create(ProductApi.class);
        return productApi;
    }
    public  interface ProductApi{
        @GET("product/name/{name}")
        public Call<ArrayList<Product>> getProductList(@Path("name") String name);

    

        @GET("product/{shopKeeperId}/{name}")
        public Call<ArrayList<Product>> getProductList(@Path("shopKeeperId") String shopKeeperId,@Path("name") String name);

        @GET("product/c/{categoryId}")
        public Call<ArrayList<Product>> getProductByCategory(@Path("categoryId") String categoryId);

        @GET("product/t/{shopKeeperId}/{categoryId}")
        public Call<List<Product>> getProductByCategoryAndShopkeeper(@Path("categoryId") String categoryId, @Path("shopKeeperId") String shopKeeperId);

        @DELETE("product/{id}")
        public Call<Product> deleteProduct(@Path("id") String id);

        @Multipart
        @POST("product/")
        public Call<Product> saveProduct(@Part List<MultipartBody.Part> file,
                                         @Part("name")RequestBody name,
                                         @Part("qtyInStock") RequestBody qtyInStock,
                                         @Part("price") RequestBody price,
                                         @Part("description") RequestBody description,
                                         @Part("discount") RequestBody discount,
                                         @Part("shopKeeperId") RequestBody shopKeeperId,
                                         @Part("brand") RequestBody brand,
                                         @Part("categoryId") RequestBody categoryId);
        @Multipart
        @POST("product/update")
        public Call<Product> updateProduct(@Part  MultipartBody.Part file,
                                         @Part("name")RequestBody name,
                                         @Part("qtyInStock") RequestBody qtyInStock,
                                         @Part("price") RequestBody price,
                                         @Part("description") RequestBody description,
                                         @Part("discount") RequestBody discount,
                                         @Part("shopKeeperId") RequestBody shopKeeperId,
                                         @Part("brand") RequestBody brand,
                                         @Part("categoryId") RequestBody categoryId,
                                         @Part("productId") RequestBody productId);

        @POST("product/update/withoutImage")
        public Call<Product> updateProductWithoutImage(@Body Product product);

        @Multipart
        @POST("product/updateImages")
        public Call<Product> updateProductImage(@Part List<MultipartBody.Part> file,
                                                @Part ("urlFirst") RequestBody urlFirst,
                                                @Part("urlSecond") RequestBody urlSecond,
                                                @Part("urlThird") RequestBody urlThird,
                                                @Part("productId") RequestBody productId);


    }
}
