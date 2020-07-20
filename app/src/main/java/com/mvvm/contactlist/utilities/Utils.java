package com.mvvm.contactlist.utilities;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Patterns;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Utils {

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static SweetAlertDialog getProgressDialog(Context context) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(false);
        return pDialog;
    }
}
