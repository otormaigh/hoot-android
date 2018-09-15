package ie.pennylabs.hoot.extension

import java.text.SimpleDateFormat
import java.util.*

fun Long.toIso8601(): String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).format(this)
