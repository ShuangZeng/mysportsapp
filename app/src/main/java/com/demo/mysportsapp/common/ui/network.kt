package com.demo.mysportsapp.common.ui

import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit

import com.google.gson.GsonBuilder

import com.google.gson.Gson

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit


object HttpClient {

    private var retrofit: Retrofit? = null
    val instance: Retrofit?
        get() {
            if (retrofit != null) return retrofit
            val httpClient = OkHttpClient.Builder()
            httpClient.connectTimeout(150, TimeUnit.SECONDS)
            httpClient.readTimeout(150, TimeUnit.SECONDS)
            httpClient.writeTimeout(150, TimeUnit.SECONDS)
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpClient.interceptors().add(logging)
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create()
            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://www.thesportsdb.com/api/v1/json/50130162/")
                .client(httpClient.build())
                .build()
            return retrofit
        }



}
