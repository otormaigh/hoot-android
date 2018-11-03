package ie.pennylabs.hoot.api

import ie.pennylabs.hoot.data.model.api.lastfm.TrackInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApi {
  @GET("/2.0/?method=track.getInfo&format=json")
  fun trackInfo(
    @Query("artist") artist: String,
    @Query("track") track: String)
    : Call<TrackInfo>
}