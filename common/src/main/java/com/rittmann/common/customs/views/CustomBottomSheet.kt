package com.rittmann.common.customs.views

import android.app.Dialog
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog

fun createBottomSheet(view: View?): Dialog {
    if (view == null) throw NullPointerException("N�o � poss�vel criar um Bottom Sheet Dialog com uma view null, passe uma view v�lida para continuar.")
    if (view.context == null) throw NullPointerException("N�o � poss�vel criar um Bottom Sheet Dialog com um context null, passe uma view com context v�lido para continuar.")
    val dialog: Dialog = BottomSheetDialog(view.context)
    dialog.setContentView(view)
    dialog.setCancelable(true)
    dialog.show()
    return dialog
}