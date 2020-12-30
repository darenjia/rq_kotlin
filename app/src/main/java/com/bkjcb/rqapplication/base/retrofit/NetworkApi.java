package com.bkjcb.rqapplication.base.retrofit;


import com.bkjcb.rqapplication.Constants;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkApi {
    private static HashMap<String, Retrofit> retrofitHashMap = new HashMap<>();
    private static String mBaseUrl = Constants.BASE_URL;

    protected Retrofit getRetrofit(Class service) {
        if (retrofitHashMap.get(mBaseUrl + service.getName()) != null) {
            return retrofitHashMap.get(mBaseUrl + service.getName());
        }
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(mBaseUrl);
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        retrofitBuilder.client(new OkHttpClient.Builder()
                .connectTimeout(20000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .callTimeout(60000, TimeUnit.MILLISECONDS)
                .build());
        Retrofit retrofit = retrofitBuilder.build();
        retrofitHashMap.put(mBaseUrl + service.getName(), retrofit);
        return retrofit;
    }

    private static volatile NetworkApi sInstance;

    public static NetworkApi getInstance() {
        if (sInstance == null) {
            synchronized (NetworkApi.class) {
                if (sInstance == null) {
                    sInstance = new NetworkApi();
                }
            }
        }
        return sInstance;
    }

    public static <T> T getService(Class<T> service) {
        return getInstance().getRetrofit(service).create(service);
    }
}
