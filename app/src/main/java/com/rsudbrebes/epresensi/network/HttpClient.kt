package com.rsudbrebes.epresensi.network

import androidx.viewbinding.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class HttpClient {

    private var client: Retrofit? = null
    private var endPoint: EndPoint? = null

    companion object {
        private val mInstance : HttpClient = HttpClient()
        @Synchronized
        fun getInstance(): HttpClient {
            return mInstance
        }
    }

    init {
        buildRetrofitClient()
    }

    fun getApi() : EndPoint? {
        if(endPoint == null){
            endPoint = client!!.create(EndPoint::class.java)
        }
        return endPoint
    }

    private fun buildRetrofitClient(){
        val token  = ""
        buildRetrofitClient(token)
    }

    fun buildRetrofitClient(token: String){
        val builder = OkHttpClient.Builder()
            builder.connectTimeout(2,TimeUnit.MINUTES)
            builder.readTimeout(2,TimeUnit.MINUTES)

        if(BuildConfig.DEBUG) {
            var interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)

        }

        if (token != null){
//            builder.addInterceptor(getInterceptorWithHeader(""))
        }
    }

}