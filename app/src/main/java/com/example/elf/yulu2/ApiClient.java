package com.example.elf.yulu2;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chivu on 23/3/18.
 */

public class ApiClient {

    private static Retrofit retrofit=null;
    public static Retrofit getClient(){

        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        if (retrofit==null){
            retrofit  = new Retrofit.Builder()
                    .baseUrl("file:///home/elf/Downloads/Task/andorid_task_json.json")
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofit;
    }

}
