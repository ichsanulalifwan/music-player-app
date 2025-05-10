package com.example.musicplayerapp.core.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import androidx.core.graphics.drawable.toDrawable
import com.example.musicplayerapp.core.ui.R

class ProgressDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        setContentView(R.layout.progress_dialog)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}