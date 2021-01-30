package com.e.toolplusstore.apis;

import com.e.toolplusstore.beans.Category;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class CategoryService {
    public static CategoryApi categoryApi;
    public  static CategoryApi getCategoryApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

           if(categoryApi==null)
               categoryApi=retrofit.create(CategoryApi.class);
           return  categoryApi;
    }
    public  interface CategoryApi{
      @GET("category/list")
      public Call<ArrayList<Category>> getCategoryList();

      @GET("category/{categoryId}")
      public Call<Category> getCategoryById(@Path("categoryId") String categoryId);
    }
}
