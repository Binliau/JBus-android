package com.kevinraihanjbusrd.jbus_android.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String baseUrl){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient())
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(getCustomGson()))
                    .build();
        }
        return retrofit;
    }
    private static OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request newRequest = originalRequest.newBuilder()
//ganti value header di bawah ini dengan nama kalian
                            .addHeader("Client-Name", "KevinRaihan")
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();
    }
    private static Gson getCustomGson() {
        return new GsonBuilder()
                .setDateFormat("MMMM dd, yyyy hh:mm:ss")
                .create();
    }
}

