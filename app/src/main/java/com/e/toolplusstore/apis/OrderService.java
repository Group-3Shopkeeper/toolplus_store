package com.e.toolplusstore.apis;

import com.e.toolplusstore.beans.PurchaseOrder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class OrderService {
    public static OrderApi orderApi;
    public  static OrderApi getOrderApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2000, TimeUnit.SECONDS)
                .readTimeout(2000, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(orderApi==null)
            orderApi=retrofit.create(OrderApi.class);
        return  orderApi;
    }
    public  interface OrderApi{
        @GET("order/orderHistory/{shopKeeperId}")
        public Call<ArrayList<PurchaseOrder>> getOrderList(@Path("shopKeeperId") String shopKeeperId);

        @GET("order/newOrder/{shopKeeperId}")
        public Call<ArrayList<PurchaseOrder>> getNewOreder(@Path("shopKeeperId") String shopKeeperId);
    }

}
