package com.poker.deuceswild.cardgame.ui

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.poker.deuceswild.R
import timber.log.Timber

object TipDialog {

    fun showDialog(context: Context, title: String, body: String) {

        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.tip_dialog_layout)

        val dismissButton = dialog.findViewById(R.id.dismiss) as Button
        dismissButton.setOnClickListener {
            dialog.dismiss()
        }

        val titleText = dialog.findViewById<TextView>(R.id.title)
        val bodyText = dialog.findViewById<TextView>(R.id.body)

        titleText.text = title
        bodyText.text = body

        Timber.d("showing tip dialog")
        dialog.show()
    }

}