package com.nihal.chatapplication.utils

import android.app.Activity
import android.app.AlertDialog
import com.nihal.chatapplication.R

class LoadingDialog(private var activity: Activity) {

    private  var alertDialog: AlertDialog? = null

    fun showLoadingDialog() {
        val builder = AlertDialog.Builder(activity);
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        alertDialog = builder.create()
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    fun dismissLoadingDialog(){
        alertDialog?.dismiss()
    }

}