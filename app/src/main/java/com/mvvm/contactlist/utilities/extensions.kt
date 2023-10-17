package com.mvvm.contactlist.utilities

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.onBackButtonPressed(callback: (() -> Unit)? = null) {
    val onBackPressed: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            callback?.let { callback() } ?: run { finish() }
        }
    }
    this.onBackPressedDispatcher.addCallback(this, onBackPressed)
}