package com.e.toolplusstore.apis;

import com.e.toolplusstore.beans.Product;
import com.e.toolplusstore.beans.ProductName;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ProductNameService {
    public static ProductNameService.ProductNameApi productNameApi;
    public  static ProductNameService.ProductNameApi getProductNameApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(productNameApi==null)
            productNameApi=retrofit.create(ProductNameService.ProductNameApi.class);
        return productNameApi;
    }
    public  interface ProductNameApi {
        @GET("productName/c/{categoryId}")
        public Call<List<ProductName>> getProductNameByCategory(@Path("categoryId") String categoryId);
    }
}
