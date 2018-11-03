package ie.pennylabs.hoot.data.model.api.lastfm

import com.squareup.moshi.Json

data class Image(
  @field:Json(name = "#text") val text: String,
  val size: String)
