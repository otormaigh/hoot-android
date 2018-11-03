package ie.pennylabs.hoot.api

import ie.pennylabs.hoot.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class LastFmAuthInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val url = chain.request()
      .url()
      .newBuilder()
      .addQueryParameter("api_key", BuildConfig.LAST_FM_API_KEY)
      .build()

    return chain.proceed(
      chain.request()
        .newBuilder()
        .url(url)
        .build())
  }
}