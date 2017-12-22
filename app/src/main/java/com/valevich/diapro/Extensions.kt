package com.valevich.diapro

import android.content.res.Resources
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_INDEFINITE
import android.support.design.widget.Snackbar.make
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import com.valevich.diapro.base.view.BaseActivity
import com.valevich.diapro.base.view.BaseFragment


fun <T> unsafeLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun AppCompatActivity.showSnackBar(
        message: String,
        length: Int = LENGTH_INDEFINITE,
        rootView: View,
        action: Snackbar.() -> Unit = { dismiss() },
        actionText: String = ""
): Snackbar =
        make(rootView, message, length).apply {
            setAction(actionText, { action.invoke(this) })
            show()
        }

fun Fragment.showSnackBar(
        message: String,
        length: Int = LENGTH_INDEFINITE,
        rootView: View,
        action: Snackbar.() -> Unit = { dismiss() },
        actionText: String = ""
): Snackbar =
        make(rootView, message, length).apply {
            setAction(actionText, { action.invoke(this) })
            show()
        }

fun BaseActivity.appComponent() = (application as DiaApplication).getAppComponent()
fun BaseFragment.appComponent() = (activity.application as DiaApplication).getAppComponent()

/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun dpToPixels(dp: Float): Float {
    return dp * (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

/**
 * This method converts device specific pixels to density independent pixels.
 *
 * @param px A value in px (pixels) unit. Which we need to convert into db
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent dp equivalent to px value
 */
fun pixelsToDp(px: Float): Float {
    return px / (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}
