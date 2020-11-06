package com.e.toolplusstore.apis;

import com.e.toolplusstore.ServerAddress;
import com.e.toolplusstore.beans.Product;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ProductService {
    public static ProductApi productApi;
    public  static ProductApi getProductApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .build();
                 Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(productApi==null)
            productApi=retrofit.create(ProductApi.class);
        return productApi;
    }
    public  interface ProductApi{
        @GET("product/name/{name}")
        public Call<ArrayList<Product>> getProductList(@Path("name") String name);
    }
}
