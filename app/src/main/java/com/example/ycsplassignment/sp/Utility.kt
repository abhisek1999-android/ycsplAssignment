package com.example.ycsplassignment.sp

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.example.ycsplassignment.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


const val EMAIL = "email"
const val PASSWORD = "password"

fun writeStringPref(context: Context?, stringId: String?, LIST_KEY: String?) {
    val pref = context?.getSharedPreferences("com.example.ycsplassignment.sp", Context.MODE_PRIVATE)
    val editor = pref?.edit()
    editor?.putString(LIST_KEY, stringId)
    editor?.apply()
}

fun readStringPref(context: Context?, LIST_KEY: String?): String {
    val pref = context?.getSharedPreferences("com.example.ycsplassignment.sp", Context.MODE_PRIVATE)
    return pref?.getString(LIST_KEY, "")?:""
}

fun <B : ViewBinding> showUniversalDialog(
    context: Context,
    binding: B,
    title: String? = null,
    conformText: String? = null,
    conformAction: ((DialogInterface, B) -> Unit)? = null,
    apply: B.(AlertDialog) -> Unit = {}
) {
    MaterialAlertDialogBuilder(
        context, R.style.ThemeOverlay_App_MaterialAlertDialog
    ).setView(binding.root).also { dialog ->
        title?.let {
            dialog.setTitle(it)
        }
        conformText?.let {
            dialog.setPositiveButton(it) { dialog, _ ->
                conformAction?.invoke(dialog, binding)
            }
            dialog.setNegativeButton("Cancel", null)
        }
    }.create().also {
        apply.invoke(binding, it)
    }.show()
}