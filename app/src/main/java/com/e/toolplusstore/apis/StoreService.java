package com.e.toolplusstore.apis;


import com.e.toolplusstore.beans.Store;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class StoreService {
    public static StoreService.ServiceApi storeApi;
    public  static StoreService.ServiceApi getStoreApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2000, TimeUnit.SECONDS)
                .readTimeout(2000, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(storeApi==null)
            storeApi=retrofit.create(StoreService.ServiceApi.class);
        return storeApi;
    }
    public  interface ServiceApi{
        @Multipart
        @POST("store/save")
        public Call<Store> saveStore(@Part MultipartBody.Part file,
                                     @Part("shopName") RequestBody shopName,
                                     @Part("contactNumber") RequestBody contactNumber,
                                     @Part("address") RequestBody address,
                                     @Part("email") RequestBody email,
                                     @Part("token") RequestBody token);
        @Multipart
        @POST("store/update")
        public Call<Store> updateStore(@Part MultipartBody.Part file,
                                     @Part("shopName") RequestBody shopName,
                                     @Part("contactNumber") RequestBody contactNumber,
                                     @Part("address") RequestBody address,
                                     @Part("email") RequestBody email,
                                     @Part("shopKeeperId") RequestBody shopKeeperId,
                                     @Part("token") RequestBody token);
    }
}
