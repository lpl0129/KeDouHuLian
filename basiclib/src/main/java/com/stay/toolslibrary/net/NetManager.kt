package com.stay.toolslibrary.net


import com.stay.toolslibrary.net.log.LoggingInterceptor
import com.google.gson.GsonBuilder
import com.stay.basicres.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


object NetManager {

     val DEFAULT_CONNECT_TIMEOUT: Long = 60
     val DEFAULT_WRITE_TIMEOUT: Long = 60
     val DEFAULT_READ_TIMEOUT: Long = 30
     val retrofitMap = HashMap<String, Retrofit>()
    /**
     * 获取retorfit
     */
    fun getRetrofit(baseUrl: String, httpClient: OkHttpClient): Retrofit {
        if (retrofitMap[baseUrl] != null) {
            return retrofitMap[baseUrl]!!
        }
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()

        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))

        val retrofit = builder.build()
        retrofitMap.put(baseUrl, retrofit)
        return retrofit
    }

    fun getClient(): OkHttpClient {
        if (httpClientBuilder == null) {
            var builder = OkHttpClient.Builder()
            //设置超时时间
            builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            builder.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
            builder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
//                val loggingInterceptor = LoggingInterceptor.Builder()
//                        .loggable(true)
//                        .request()
//                        .requestTag("Request")
//                        .response()
//                        .responseTag("Response")
//                        //.hideVerticalLine()// 隐藏竖线边框
//                        .build()

                val logInterceptor = HttpLoggingInterceptor()
                logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                //设置拦截器
                //设置拦截器
                builder.addInterceptor(logInterceptor)
            }
            return builder.build()
        } else {
            return httpClientBuilder!!.build()
        }
    }

    var httpClientBuilder: OkHttpClient.Builder? = null
        private set

    /**
     * 注入http配置
     */
    fun registerHttpBuilder(httpClientBuilder: OkHttpClient.Builder) {
        NetManager.httpClientBuilder = httpClientBuilder
    }

    fun getRetrofitMap(): Map<String, Retrofit> {
        return retrofitMap
    }

    fun clearCache() {
        retrofitMap.clear()
    }

}
