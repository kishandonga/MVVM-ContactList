package com.mvvm.contactlist.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mvvm.contactlist.R;
import com.mvvm.contactlist.databinding.FragmentAddContactBinding;
import com.mvvm.contactlist.utilities.Const;
import com.mvvm.contactlist.utilities.Contact;
import com.mvvm.contactlist.utilities.InjectorUtils;
import com.mvvm.contactlist.utilities.Utils;
import com.mvvm.contactlist.viewmodel.ContactViewModel;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import id.zelory.compressor.Compressor;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddContactFragment extends Fragment {

    private FragmentAddContactBinding binding;
    private ContactViewModel contactViewModel;
    private SweetAlertDialog dialog;
    private int contact_id = -1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            contact_id = bundle.getInt(Const.CONTACT_ID, -1);
        }

        ContactViewModel.ContactViewModelFactory factory = InjectorUtils.provideContactViewModelFactory(requireContext());
        contactViewModel = new ViewModelProvider(this, factory).get(ContactViewModel.class);
        contactViewModel.setProfilePic(R.drawable.ic_place_holder);

        if (contact_id != -1) {
            contactViewModel.setValuesForUpdate(contact_id);
        }

        binding = FragmentAddContactBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(contactViewModel);

        dialog = Utils.getProgressDialog(requireContext());


        contactViewModel.getErrorSuccMessage().observe(getViewLifecycleOwner(), integer ->
                Snackbar.make(binding.getRoot(), getString(integer), Snackbar.LENGTH_SHORT).show());

        contactViewModel.getSelectImage().observe(getViewLifecycleOwner(), aVoid ->
                ImagePicker.with(this)             //  Initialize ImagePicker with activity or fragment context
                        .setToolbarColor("#212121")         //  Toolbar color
                        .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                        .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                        .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                        .setProgressBarColor("#4CAF50")     //  ProgressBar color
                        .setBackgroundColor("#212121")      //  Background color
                        .setCameraOnly(false)               //  Camera mode
                        .setMultipleMode(false)             //  Select multiple images or single image
                        .setFolderMode(true)                //  Folder mode
                        .setShowCamera(true)                //  Show camera button
                        .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                        .setImageTitle("Gallery")           //  Image title (works with FolderMode = false)
                        .setDoneTitle("Done")               //  Done button title
                        .setSavePath("ImagePicker")         //  Image capture folder name
                        .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                        .setRequestCode(Const.IMAGE_PIC_REQ_CODE)                //  Set request code, default Config.RC_PICK_IMAGES
                        .setKeepScreenOn(true)              //  Keep screen on when selecting images
                        .start());

        contactViewModel.getAllCategoryName()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        contactViewModel.addDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<String> list) {
                        List<String> aList = new ArrayList<>();
                        aList.add(requireActivity().getString(R.string.lbl_select_category));
                        aList.addAll(list);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, aList);
                        binding.spCategory.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(Const.TAG, Log.getStackTraceString(e));
                    }
                });

        contactViewModel.getToContactList().observe(getViewLifecycleOwner(), aVoid ->
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.nav_contact_list));

        contactViewModel.getProgressDialog().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                dialog.show();
            } else {
                dialog.dismiss();
            }
        });

        contactViewModel.getAddContact().observe(getViewLifecycleOwner(), entity ->

                Dexter.withActivity(requireActivity())
                        .withPermissions(
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_CONTACTS
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Contact.addContact(requireContext(), entity.getFirstName() + " " + entity.getLastName(), entity.getMobileNum(), entity.getEmail());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check());


        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (images != null && images.size() > 0) {
                Image image = images.get(0);

                contactViewModel.addDisposable(new Compressor(requireContext())
                        .compressToFileAsFlowable(new File(image.getPath()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bitmap -> contactViewModel.onImageReceived(bitmap), throwable -> Log.e(Const.TAG, Log.getStackTraceString(throwable))));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}