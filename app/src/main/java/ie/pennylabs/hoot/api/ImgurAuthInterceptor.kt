package ie.pennylabs.hoot.api

import ie.pennylabs.hoot.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ImgurAuthInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response =
    chain.proceed(
      chain.request()
        .newBuilder()
        .addHeader("Authorization", "Client-ID ${BuildConfig.IMGUR_CLIENT_ID}")
        .build())
}