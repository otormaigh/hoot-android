package ie.pennylabs.hoot.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import ie.pennylabs.hoot.BuildConfig
import ie.pennylabs.hoot.api.ImgurApi
import ie.pennylabs.hoot.api.ImgurAuthInterceptor
import ie.pennylabs.hoot.api.LastFmApi
import ie.pennylabs.hoot.api.LastFmAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object ApiModule {

  @Provides
  @JvmStatic
  fun provideRetrofit(moshi: Moshi): Retrofit.Builder =
    Retrofit.Builder()
      .addConverterFactory(MoshiConverterFactory.create(moshi))

  @Provides
  @JvmStatic
  fun provideOkHttp(): OkHttpClient.Builder =
    OkHttpClient.Builder()
      .connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
      .addNetworkInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
        if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
      })

  @Provides
  @Singleton
  @JvmStatic
  fun provideImgurApi(retrofit: Retrofit.Builder, okHttpClient: OkHttpClient.Builder): ImgurApi =
    retrofit
      .baseUrl(BuildConfig.IMGUR_URL)
      .client(okHttpClient.addInterceptor(ImgurAuthInterceptor()).build())
      .build()
      .create(ImgurApi::class.java)

  @Provides
  @Singleton
  @JvmStatic
  fun provideLastFmApi(retrofit: Retrofit.Builder, okHttpClient: OkHttpClient.Builder): LastFmApi =
    retrofit
      .baseUrl(BuildConfig.LAST_FM_URL)
      .client(okHttpClient.addInterceptor(LastFmAuthInterceptor()).build())
      .build()
      .create(LastFmApi::class.java)
}

private const val API_TIMEOUT = 30_000L