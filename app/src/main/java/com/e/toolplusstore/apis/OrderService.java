package com.e.toolplusstore.apis;

import com.e.toolplusstore.beans.History;
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
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(orderApi==null)
            orderApi=retrofit.create(OrderApi.class);

            orderApi=retrofit.create(OrderService.OrderApi.class);
        return  orderApi;
    }
    public  interface OrderApi{
        @GET("order/historyShopkeeper/{currentUserId}")
        public Call<ArrayList<PurchaseOrder>> getOrderList1(@Path("currentUserId") String currentUserId);

        @GET("order/historyShopkeeper/{currentUserId}")
        public Call<ArrayList<History>> getOrderList(@Path("currentUserId") String currentUserId);

        @GET("order/orderHistory/{shopKeeperId}")
        public Call<ArrayList<PurchaseOrder>> getNewOreder(@Path("shopKeeperId") String shopKeeperId);

    }

}
