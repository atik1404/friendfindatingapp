package com.friendfinapp.dating.helper

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.google.gson.Gson

fun showDebugLog(msg: String) =
Log.d("bu", "showDebugLog: $msg")

fun showErrorLog(msg: String) =
    Log.e("BUR", "showErrorLog: $msg")

fun showResponseLog(response: Any) =
    Log.e("BUR", "showResponseLog: "+ Gson().toJson(response))


fun showEventLog(msg: String) =
    Log.e("EVENT", "Event logged: $msg")

fun Context.showToast(msg: String) {
    val toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 100)
    /*val view: View = toast.view!!

    //To change the Background of Toast
    view.background
        .setColorFilter(ContextCompat.getColor(AppController.instance.applicationContext, R.color.colorAccent), PorterDuff.Mode.SRC_IN)
    val text: TextView = view.findViewById(R.id.message)
    text.setTextColor(Color.WHITE)
    text.textSize = 18f*/
    toast.show()
}

fun ImageView.setTint(@ColorRes colorRes: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
}

fun View.toggleVisibility() {
    visibility = if(visibility == View.VISIBLE)
        View.INVISIBLE
    else
        View.VISIBLE
}

fun Context.changeActivity(target: Class<*>, bundle: Bundle) =
    startActivity(Intent(this, target).putExtras(bundle))

fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)

fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
