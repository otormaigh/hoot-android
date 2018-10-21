package ie.pennylabs.hoot.api

import com.squareup.moshi.Moshi
import ie.pennylabs.hoot.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
  private var INSTANCE: ImgurApi? = null

  private fun provideMoshi(): Moshi =
    Moshi.Builder()
      .build()

  private fun provideRetrofit(moshi: Moshi = provideMoshi(), okHttpClient: OkHttpClient = provideOkHttp()): Retrofit =
    Retrofit.Builder()
      .baseUrl(BuildConfig.IMGUR_URL)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()


  private fun provideOkHttp(): OkHttpClient =
    OkHttpClient.Builder()
      .protocols(listOf(Protocol.HTTP_1_1))
      .connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .addInterceptor(ImgurAuthInterceptor())
      .addNetworkInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
        if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
      }).build()

  private fun provideImgurApi(retrofit: Retrofit = provideRetrofit()): ImgurApi = retrofit.create(ImgurApi::class.java)

  fun instance(): ImgurApi {
    if (INSTANCE == null) INSTANCE = provideImgurApi()
    return INSTANCE!!
  }
}

private const val API_TIMEOUT = 30_000L