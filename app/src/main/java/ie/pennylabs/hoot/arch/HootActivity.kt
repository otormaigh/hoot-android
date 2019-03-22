package ie.pennylabs.hoot.arch

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

@SuppressLint("Registered")
open class HootActivity : AppCompatActivity() {
  @Inject
  lateinit var viewModelFactory: ViewModelFactory
}