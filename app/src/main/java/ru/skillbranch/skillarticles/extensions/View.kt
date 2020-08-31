package ru.skillbranch.skillarticles.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

fun View.setMarginOptionally(
    left: Int = marginLeft,
    top: Int = marginTop,
    right: Int = marginRight,
    bottom: Int = marginBottom
) {
    val lp = this.layoutParams as ViewGroup.MarginLayoutParams
    lp.setMargins(left, top, right, bottom)

    this.layoutParams = lp
}
