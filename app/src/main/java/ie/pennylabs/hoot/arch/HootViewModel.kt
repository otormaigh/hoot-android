package ie.pennylabs.hoot.arch

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class HootViewModel : ViewModel(), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO
}