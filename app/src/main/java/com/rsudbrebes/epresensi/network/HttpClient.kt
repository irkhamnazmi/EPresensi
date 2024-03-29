package com.rsudbrebes.epresensi.network




import com.rsudbrebes.epresensi.BuildConfig
import com.rsudbrebes.epresensi.utils.Helpers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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
//
        if (token != null){
            builder.addInterceptor(getInterceptoreWithHeader("Authorization", "Bearer ${token}"))
        }

        val okHttpClient = builder.build()
        client = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL+"api/absensi/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Helpers.getDefaultGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        endPoint = null
    }

    private fun getInterceptoreWithHeader(headerName : String, headerValue: String) : Interceptor{
        val header = HashMap<String, String>()
        header.put(headerName, headerValue)
        return getInterceptoreWithHeader(header)

    }

    private fun getInterceptoreWithHeader(headers : Map<String, String>) : Interceptor{
        return Interceptor {
            val original = it.request()
            val builder = original.newBuilder()
            for ((key, value) in headers) {
                builder.addHeader(key, value)

            }
            builder.method(original.method, original.body)
            it.proceed(builder.build())
        }

    }


}