package ie.pennylabs.hoot.api

import com.squareup.moshi.Moshi
import ie.pennylabs.hoot.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
  private var IMGUR_INSTANCE: ImgurApi? = null
  private var LAST_FM_INSTANCE: LastFmApi? = null

  private fun provideMoshi(): Moshi =
    Moshi.Builder()
      .build()

  private fun provideRetrofit(moshi: Moshi = provideMoshi(), okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
    Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()


  private fun provideOkHttp(authInterceptor: Interceptor): OkHttpClient =
    OkHttpClient.Builder()
      .connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .addInterceptor(authInterceptor)
      .addNetworkInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
        if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
      })
      .build()

  private fun provideImgurApi(retrofit: Retrofit = provideRetrofit(
    okHttpClient = provideOkHttp(ImgurAuthInterceptor()),
    baseUrl = BuildConfig.IMGUR_URL)): ImgurApi = retrofit.create(ImgurApi::class.java)

  private fun provideLastFmApi(retrofit: Retrofit = provideRetrofit(
    okHttpClient = provideOkHttp(LastFmAuthInterceptor()),
    baseUrl = BuildConfig.LAST_FM_URL)): LastFmApi = retrofit.create(LastFmApi::class.java)

  fun imgur(): ImgurApi {
    if (IMGUR_INSTANCE == null) IMGUR_INSTANCE = provideImgurApi()
    return IMGUR_INSTANCE!!
  }

  fun lastFm(): LastFmApi {
    if (LAST_FM_INSTANCE == null) LAST_FM_INSTANCE = provideLastFmApi()
    return LAST_FM_INSTANCE!!
  }
}

private const val API_TIMEOUT = 30_000L