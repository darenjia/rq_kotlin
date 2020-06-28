package com.bkjcb.rqapplication.retrofit

import com.bkjcb.rqapplication.Constants
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class NetworkApi private constructor() {
    private fun getRetrofit(service: Class<*>): Retrofit {
        if (retrofitHashMap[mBaseUrl + service.name] != null) {
            return retrofitHashMap[mBaseUrl + service.name]!!
        }
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.baseUrl(mBaseUrl)
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        retrofitBuilder.client(OkHttpClient.Builder().connectTimeout(20000, TimeUnit.MILLISECONDS).build())
        val retrofit = retrofitBuilder.build()
        retrofitHashMap[mBaseUrl + service.name] = retrofit
        return retrofit
    }

    companion object {
        private val retrofitHashMap = HashMap<String, Retrofit>()
        private val mBaseUrl = Constants.BASE_URL
        private val instance = SingletonHolder.holder
//        private val instance: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
//            NetworkApi()
//        }

        fun <T> getService(service: Class<T>): T {
            return instance.getRetrofit(service).create(service)
        }
    }

    private object SingletonHolder {
        var holder = NetworkApi()
    }
}