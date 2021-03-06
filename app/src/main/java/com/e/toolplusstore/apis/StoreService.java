package com.e.toolplusstore.apis;


import com.e.toolplusstore.beans.Shopkeeper;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class StoreService {
    public static StoreService.ServiceApi storeApi;
    public  static StoreService.ServiceApi getStoreApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(storeApi==null)
            storeApi=retrofit.create(StoreService.ServiceApi.class);
        return storeApi;
    }
    public  interface ServiceApi{
        @Multipart
        @POST("store/save")
        public Call<Shopkeeper> saveStore(@Part MultipartBody.Part file,
                                          @Part("shopName") RequestBody shopName,
                                          @Part("contactNumber") RequestBody contactNumber,
                                          @Part("address") RequestBody address,
                                          @Part("email") RequestBody email,
                                          @Part("shopKeeperId") RequestBody shopKeeperId,
                                          @Part("token") RequestBody token);
        @Multipart
        @POST("store/update")
        public Call<Shopkeeper> updateStore(@Part MultipartBody.Part file,
                                            @Part("shopName") RequestBody shopName,
                                            @Part("contactNumber") RequestBody contactNumber,
                                            @Part("address") RequestBody address,
                                            @Part("email") RequestBody email,
                                            @Part("shopKeeperId") RequestBody shopKeeperId,
                                            @Part("token") RequestBody token);
        @POST("store/update/withoutImage")
        public Call<Shopkeeper> updateStoreWithoutImage(@Body Shopkeeper shopkeeper);

        @GET("store/{id}")
        public Call<Shopkeeper> getStoreProfile(@Path("id") String id);
    }
}
