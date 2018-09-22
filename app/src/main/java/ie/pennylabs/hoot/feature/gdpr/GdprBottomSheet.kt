package ie.pennylabs.hoot.feature.gdpr

import android.content.Context
import android.widget.FrameLayout
import androidx.core.view.doOnLayout
import com.crashlytics.android.core.CrashlyticsCore
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.perf.FirebasePerformance
import ie.pennylabs.hoot.R
import ie.pennylabs.hoot.toolbox.enableAnalytics
import ie.pennylabs.hoot.toolbox.enableCrashReporting
import ie.pennylabs.hoot.toolbox.enablePerformance
import ie.pennylabs.hoot.toolbox.hasAcceptedGdpr
import ie.pennylabs.hoot.toolbox.prefs
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.sheet_gdpr.*

class GdprBottomSheet(context: Context) : BottomSheetDialog(context) {
  init {
    setContentView(R.layout.sheet_gdpr)
    setCancelable(false)

    val bottomSheet = window.findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
    bottomSheet.setBackgroundResource(R.drawable.bg_top_rounded_sheet)
    bottomSheet.doOnLayout {
      BottomSheetBehavior.from(bottomSheet).apply {
        state = BottomSheetBehavior.STATE_EXPANDED
        peekHeight = it.height
      }
    }

    switchPerf.setOnCheckedChangeListener { _, isChecked -> context.prefs.enablePerformance = isChecked }
    switchCrash.setOnCheckedChangeListener { _, isChecked -> context.prefs.enableCrashReporting = isChecked }
    switchAnalytics.setOnCheckedChangeListener { _, isChecked -> context.prefs.enableAnalytics = isChecked }
    btnAccept.setOnClickListener {
      context.prefs.hasAcceptedGdpr = true
      dismiss()
    }
  }

  override fun dismiss() {
    super.dismiss()

    FirebasePerformance.getInstance().isPerformanceCollectionEnabled = context.prefs.enablePerformance
    FirebaseAnalytics.getInstance(context).setAnalyticsCollectionEnabled(context.prefs.enableAnalytics)
    Fabric.with(context, CrashlyticsCore.Builder()
      .disabled(!context.prefs.enableCrashReporting)
      .build())
  }

  companion object {
    fun show(context: Context) {
      if (!context.prefs.hasAcceptedGdpr) {
        GdprBottomSheet(context).show()
      }
    }
  }
}