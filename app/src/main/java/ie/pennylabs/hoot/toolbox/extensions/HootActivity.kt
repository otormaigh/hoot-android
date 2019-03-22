package ie.pennylabs.hoot.toolbox.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import ie.pennylabs.hoot.arch.HootActivity

@Suppress("UNCHECKED_CAST")
inline fun <reified VM : ViewModel> HootActivity.viewModelProvider(
  mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
  crossinline provider: () -> Class<VM>) = lazy(mode) {

  ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)
}