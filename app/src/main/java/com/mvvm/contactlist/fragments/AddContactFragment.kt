package com.mvvm.contactlist.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mvvm.contactlist.R
import com.mvvm.contactlist.databinding.FragmentAddContactBinding
import com.mvvm.contactlist.db.entities.ContactEntity
import com.mvvm.contactlist.utilities.Const
import com.mvvm.contactlist.utilities.Contact.addContact
import com.mvvm.contactlist.utilities.InjectorUtils.provideContactViewModelFactory
import com.mvvm.contactlist.utilities.Utils
import com.mvvm.contactlist.utilities.Utils.getProgressDialog
import com.mvvm.contactlist.viewmodel.ContactViewModel
import com.nguyenhoanglam.imagepicker.model.CustomColor
import com.nguyenhoanglam.imagepicker.model.CustomMessage
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import id.zelory.compressor.Compressor
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File

class AddContactFragment : Fragment() {

    private lateinit var binding: FragmentAddContactBinding
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var dialog: SweetAlertDialog

    private val launcher = registerImagePicker { images ->
        if (images.isNotEmpty()) {
            val image = images[0]
            contactViewModel.addDisposable(Compressor(requireContext())
                .compressToFileAsFlowable(
                    File(
                        Utils.getRealPathFromUri(
                            requireContext(),
                            image.uri
                        )
                    )
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bitmap: File ->
                    contactViewModel.onImageReceived(bitmap)
                }) { throwable: Throwable? ->
                    Log.e(Const.TAG, Log.getStackTraceString(throwable))
                })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val factory = provideContactViewModelFactory(requireContext())
        contactViewModel = ViewModelProvider(this, factory)[ContactViewModel::class.java]
        contactViewModel.setProfilePic(R.drawable.ic_place_holder)

        arguments?.apply {
            val contactId = getInt(Const.CONTACT_ID, -1)
            contactViewModel.setValuesForUpdate(contactId)
        }

        binding = FragmentAddContactBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = contactViewModel
        dialog = getProgressDialog(requireContext())

        contactViewModel.errorSuccMessage.observe(viewLifecycleOwner) { integer: Int ->
            Snackbar.make(binding.root, getString(integer), Snackbar.LENGTH_SHORT).show()
        }

        contactViewModel.selectImage.observe(viewLifecycleOwner) { _: Void? ->

            val config = ImagePickerConfig(
                isMultiSelectMode = false,
                isShowCamera = false,
                isFolderMode = true,
                customColor = CustomColor(
                    background = "#000000",
                    statusBar = "#000000",
                    toolbar = "#212121",
                    toolbarTitle = "#FFFFFF",
                    toolbarIcon = "#FFFFFF",
                ),
                customMessage = CustomMessage(
                    noImage = "No image found.",
                    noPhotoAccessPermission = "Please allow permission to access photos and media.",
                ),
            )

            launcher.launch(config)
        }

        contactViewModel.allCategoryName
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<String>> {

                override fun onSubscribe(d: Disposable) {
                    contactViewModel.addDisposable(d)
                }

                override fun onSuccess(list: List<String>) {
                    val aList: MutableList<String> = ArrayList()
                    aList.add(requireActivity().getString(R.string.lbl_select_category))
                    aList.addAll(list)
                    val adapter =
                        ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, aList)
                    binding.spCategory.adapter = adapter
                }

                override fun onError(e: Throwable) {
                    Log.e(Const.TAG, Log.getStackTraceString(e))
                }
            })

        contactViewModel.toContactList.observe(viewLifecycleOwner) { _: Void? ->
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.nav_contact_list)
        }

        contactViewModel.progressDialog.observe(viewLifecycleOwner) { aBoolean: Boolean ->
            if (aBoolean) {
                dialog.show()
            } else {
                dialog.dismiss()
            }
        }

        contactViewModel.addContact.observe(viewLifecycleOwner) { entity: ContactEntity ->
            Dexter.withActivity(requireActivity())
                .withPermissions(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            addContact(
                                requireContext(),
                                entity.firstName + " " + entity.lastName,
                                entity.mobileNum,
                                entity.email
                            )
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }

        return binding.root
    }
}