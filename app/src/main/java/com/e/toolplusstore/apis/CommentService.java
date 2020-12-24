package com.e.toolplusstore.apis;

import com.e.toolplusstore.beans.Category;
import com.e.toolplusstore.beans.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class CommentService {

    public static CommentApi commentApi;
    public static CommentApi getCommentApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2000, TimeUnit.SECONDS)
                .readTimeout(2000, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(commentApi==null)
           commentApi= retrofit.create(CommentApi.class);
        return commentApi;
    }
    public interface CommentApi{

        @GET("comment/{productId}")
        public Call<List<Comment>> getProductByComment(@Path("productId") String productId);
    }
}
