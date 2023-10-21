package com.mvvm.contactlist.utilities

import android.graphics.BitmapFactory
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mvvm.contactlist.viewmodel.ContactViewModel
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

object CustomViewBindings {

    @JvmStatic
    @BindingAdapter("setAdapter")
    fun bindRecyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("set_profile_pic")
    fun bindSetProfilePic(imageView: CircleImageView, file: File?) {
        file?.apply {
            Glide.with(imageView.context).load(this).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView)
        }
    }


    @JvmStatic
    @BindingAdapter("load_profile_pic")
    fun bindLoadProfilePic(imageView: CircleImageView, file: String?) {
        if (file != null) {
            Glide.with(imageView.context).load(File(file))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView)
        }
    }


    @JvmStatic
    @BindingAdapter("set_place_holder_pic")
    fun bindSetPlaceHolderPic(imageView: CircleImageView, integer: Int) {
        if (integer != 0) {
            val bitmap = BitmapFactory.decodeResource(imageView.context.resources, integer)
            imageView.setImageBitmap(bitmap)
        }
    }


    @JvmStatic
    @BindingAdapter("set_text")
    fun bindSetText(btn: AppCompatButton, integer: Int) {
        if (integer != 0) {
            btn.text = btn.context.getString(integer)
        }
    }


    @JvmStatic
    @BindingAdapter("onItemSelected")
    fun bindOnItemSelected(spinner: AppCompatSpinner, viewModel: ContactViewModel) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                viewModel.onSelectItem(parent)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    @JvmStatic
    @BindingAdapter("set_default_value")
    fun bindSetDefaultValueSelected(spinner: AppCompatSpinner, index: Int?) {
        if (index != null) {
            spinner.setSelection(index, true)
        }
    }
}