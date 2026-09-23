package com.btjnonbrokerage.Base

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver

class HandleKeyBoard {

    private var onKeyboardVisibilityChanged: ((Boolean) -> Unit)? = null
    private var rootView: View? = null
    private var initScreenHeight = 0


    fun startKeyboardListener(view: View, onKeyboardVisibilityChanged: (Boolean) -> Unit) {
        this.rootView = view
        this.rootView?.viewTreeObserver?.addOnGlobalLayoutListener(keyboardLayoutListener)
        this.onKeyboardVisibilityChanged = onKeyboardVisibilityChanged
    }

    fun stopKeyboardListener() {
        this.rootView?.viewTreeObserver?.removeOnGlobalLayoutListener(keyboardLayoutListener)
        this.rootView = null
        this.onKeyboardVisibilityChanged = null
    }

    private val keyboardLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val r = Rect()

        rootView?.getWindowVisibleDisplayFrame(r)
        val screenHeight = rootView?.height ?: 0

        if (initScreenHeight == 0){
            initScreenHeight = screenHeight
        }

        if (initScreenHeight * 0.8 > screenHeight ) {
            onKeyboardVisibilityChanged?.invoke(true)
        } else {
            onKeyboardVisibilityChanged?.invoke(false)
        }
    }
}