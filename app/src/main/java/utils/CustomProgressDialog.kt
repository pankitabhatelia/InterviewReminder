package utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.interviewreminderapp.R


class CustomProgressDialog(context: Context) {

    private var dialog: CustomDialog
    private var cpTitle: TextView
    private var cpCardView: CardView
    private var progressBar: ProgressBar

    fun start() {
        cpTitle.setText(R.string.loading)
        dialog.show()
    }

    fun stop() {
        dialog.dismiss()
    }

    init {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.loading_item, null)
        cpTitle = view.findViewById(R.id.cpTitle)
        cpCardView = view.findViewById(R.id.cpCardView)
        progressBar = view.findViewById(R.id.progressBar)
        cpTitle.setTextColor(Color.GRAY)
        cpTitle.setText(R.string.loading)
        dialog = CustomDialog(context)
        dialog.setContentView(view)
    }


    class CustomDialog(context: Context) : Dialog(context, R.style.CustomDialogTheme) {
        init {
            window?.decorView?.rootView?.setBackgroundResource(R.color.trans)
        }
    }
}