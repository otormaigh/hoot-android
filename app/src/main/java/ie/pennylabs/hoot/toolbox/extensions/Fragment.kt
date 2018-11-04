package ie.pennylabs.hoot.toolbox.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

@Suppress("UNCHECKED_CAST")
inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
  mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
  crossinline provider: () -> VM) = lazy(mode) {

  val factory = object : ViewModelProvider.Factory {
    override fun <M : ViewModel> create(klass: Class<M>) = provider() as M
  }
  ViewModelProviders.of(this, factory).get(VM::class.java)
}