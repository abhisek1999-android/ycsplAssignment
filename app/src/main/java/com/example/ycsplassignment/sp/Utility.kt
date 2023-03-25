package com.example.ycsplassignment.sp

import android.content.Context


const val EMAIL = "email"
const val PASSWORD = "password"

fun writeStringPref(context: Context?, stringId: String?, LIST_KEY: String?) {
    val pref = context?.getSharedPreferences("com.example.ycsplassignment.sp", Context.MODE_PRIVATE)
    val editor = pref?.edit()
    editor?.putString(LIST_KEY, stringId)
    editor?.apply()
}

fun readStringPref(context: Context?, LIST_KEY: String?): String? {
    val pref = context?.getSharedPreferences("com.example.ycsplassignment.sp", Context.MODE_PRIVATE)
    return pref?.getString(LIST_KEY, "")
}