package com.example.elf.yulu2;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by chivu on 23/3/18.
 */

public interface ApiInterface {


    @GET()
    Call<ResponseBody> getJSON();



   /* @GET("top-headlines?sources=google-news-in&apiKey=24e5d521314646b38aca31ab13a74ff5")
    Call<ResponseBody> getJSON();*/
}
