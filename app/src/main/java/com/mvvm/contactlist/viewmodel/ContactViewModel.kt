package com.mvvm.contactlist.viewmodel

import android.util.Log
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvvm.contactlist.R
import com.mvvm.contactlist.db.entities.ContactEntity
import com.mvvm.contactlist.repo.ContactRepository
import com.mvvm.contactlist.utilities.Const
import com.mvvm.contactlist.utilities.SingleLiveEvent
import com.mvvm.contactlist.utilities.Utils.isValidEmail
import io.reactivex.MaybeObserver
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File

class ContactViewModel private constructor(private val repository: ContactRepository) :
    ViewModel() {

    private val mDisposable = CompositeDisposable()
    val errorSuccMessage = SingleLiveEvent<Int>()
    val progressDialog = SingleLiveEvent<Boolean>()
    val selectImage = SingleLiveEvent<Void?>()
    val toContactList = SingleLiveEvent<Void?>()
    val addContact = SingleLiveEvent<ContactEntity>()
    val profilePic = MutableLiveData<File>()
    val placeHolderPic = MutableLiveData<Int>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val mobileNum = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val btnText = MutableLiveData<Int?>()
    val categoryDefaultValue = MutableLiveData<Int>()
    private var selectedCategoryId = 0
    private var contactId = 0

    init {
        btnText.value = R.string.lbl_save
    }

    /**
     * @param contactId Contact ID for the update procedure
     */
    fun setValuesForUpdate(contactId: Int) {

        if (contactId == -1) {
            return
        }

        this.contactId = contactId
        mDisposable.add(
            repository.getContactFromContactId(contactId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { model: ContactEntity ->
                        btnText.value = R.string.lbl_update
                        firstName.value = model.firstName
                        lastName.value = model.lastName
                        mobileNum.value = model.mobileNum
                        email.value = model.email
                        model.profilePic?.apply {
                            profilePic.value = File(this)
                        }
                        categoryDefaultValue.setValue(model.categoryId)
                    }
                ) { _: Throwable? -> errorSuccMessage.setValue(R.string.lbl_something_wrong) })
    }

    fun addDisposable(disposable: Disposable) {
        mDisposable.add(disposable)
    }

    val allCategoryName: Single<List<String>>
        get() = repository.allCategoryName

    fun setProfilePic(placeHolder: Int) {
        placeHolderPic.value = placeHolder
    }

    /**
     * @param parent get Adapter view then using this view you get the selected values from the Spinner
     */
    fun onSelectItem(parent: AdapterView<*>) {
        //pos                                 get selected item position
        //view.getText()                      get label of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getAdapter().getCount()      get item count
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        //and other...
        val selectedCatg = parent.selectedItem.toString()
        repository.findIdByCatgName(selectedCatg)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MaybeObserver<Int> {
                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                }

                override fun onSuccess(integer: Int) {
                    selectedCategoryId = integer
                }

                override fun onError(e: Throwable) {
                    Log.e(Const.TAG, Log.getStackTraceString(e))
                }

                override fun onComplete() {}
            })
    }

    /**
     * when user click on photo this method call and bind with image picker
     */
    fun onProfilePicSelect() {
        selectImage.value = null
    }

    /**
     * @param file compressed image received in the File format
     */
    fun onImageReceived(file: File) {
        profilePic.value = file
    }

    /**
     * When user press save or update button this method called
     */
    fun onSaveClick() {
        val entity = ContactEntity()
        val file = profilePic.value
        if (file != null) {
            val path = file.absolutePath
            entity.profilePic = path
        } else {
            errorSuccMessage.value = R.string.err_msg_select_profile_pic
            return
        }
        val strFirstName = firstName.value
        if (!strFirstName.isNullOrEmpty()) {
            entity.firstName = strFirstName.trim { it <= ' ' }
        } else {
            errorSuccMessage.value = R.string.err_msg_first_name
            return
        }
        val strLastName = lastName.value
        if (!strLastName.isNullOrEmpty()) {
            entity.lastName = strLastName.trim { it <= ' ' }
        } else {
            errorSuccMessage.value = R.string.err_msg_last_name
            return
        }
        val strMobileNum = mobileNum.value
        if (!strMobileNum.isNullOrEmpty()) {
            entity.mobileNum = strMobileNum.trim { it <= ' ' }
        } else {
            errorSuccMessage.value = R.string.err_msg_mobile_num
            return
        }
        val strEmail = email.value
        if (!strEmail.isNullOrEmpty()) {
            if (isValidEmail(strEmail)) {
                entity.email = strEmail.trim { it <= ' ' }
            } else {
                errorSuccMessage.value = R.string.err_msg_email_valid
                return
            }
        } else {
            errorSuccMessage.value = R.string.err_msg_email
            return
        }
        if (selectedCategoryId != 0) {
            entity.categoryId = selectedCategoryId
        } else {
            errorSuccMessage.value = R.string.err_msg_select_ctg
            return
        }
        progressDialog.value = true
        if (btnText.value != null && btnText.value == R.string.lbl_save) {
            mDisposable.add(
                repository.insertContact(entity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            addContact.value = entity
                            placeHolderPic.value = R.drawable.ic_place_holder
                            firstName.value = ""
                            lastName.value = ""
                            mobileNum.value = ""
                            email.value = ""
                            progressDialog.value = false
                            toContactList.setValue(null)
                        }
                    ) { _: Throwable? ->
                        progressDialog.value = false
                        errorSuccMessage.setValue(R.string.lbl_something_wrong)
                    })
        } else {
            entity.contactId = contactId
            mDisposable.add(
                repository.updateContact(entity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            placeHolderPic.value = R.drawable.ic_place_holder
                            firstName.value = ""
                            lastName.value = ""
                            mobileNum.value = ""
                            email.value = ""
                            progressDialog.value = false
                            toContactList.setValue(null)
                        }
                    ) { _: Throwable? ->
                        progressDialog.value = false
                        errorSuccMessage.setValue(R.string.lbl_something_wrong)
                    })
        }
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }

    class ContactViewModelFactory(private val repository: ContactRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ContactViewModel(repository) as T
        }
    }
}