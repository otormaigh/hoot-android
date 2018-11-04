package ie.pennylabs.hoot.api

import ie.pennylabs.hoot.data.model.api.Gallery
import ie.pennylabs.hoot.data.model.api.Sort
import ie.pennylabs.hoot.data.model.api.Window
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ImgurApi {
  @GET("gallery/r/fakealbumcovers/{sort}/{window}/{page}.json")
  fun getAlbumCovers(
    @Path("sort") sort: Sort = Sort.time,
    @Path("window") window: Window = Window.all,
    @Path("page") page: Int
  ): Call<Gallery>
}