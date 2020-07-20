package com.mvvm.contactlist.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mvvm.contactlist.viewmodel.ContactViewModel;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomViewBindings {

    @BindingAdapter("setAdapter")
    public static void bindRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("set_profile_pic")
    public static void bindSetProfilePic(CircleImageView imageView, File file) {
        if (file != null) {
            Glide.with(imageView.getContext()).load(file).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
        }
    }

    @BindingAdapter("load_profile_pic")
    public static void bindLoadProfilePic(CircleImageView imageView, String file) {
        if (file != null) {
            Glide.with(imageView.getContext()).load(new File(file)).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
        }
    }

    @BindingAdapter("set_place_holder_pic")
    public static void bindSetPlaceHolderPic(CircleImageView imageView, Integer integer) {
        if (integer != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(imageView.getContext().getResources(), integer);
            imageView.setImageBitmap(bitmap);
        }
    }

    @BindingAdapter("set_text")
    public static void bindSetText(AppCompatButton btn, Integer integer) {
        if (integer != 0) {
            btn.setText(btn.getContext().getString(integer));
        }
    }

    @BindingAdapter("onItemSelected")
    public static void bindOnItemSelected(AppCompatSpinner spinner, ContactViewModel viewModel) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.onSelectItem(parent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @BindingAdapter("set_default_value")
    public static void bindSetDefaultValueSelected(AppCompatSpinner spinner, Integer index) {
        if (index != null) {
            spinner.setSelection(index, true);
        }
    }
}
