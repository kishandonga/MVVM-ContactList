package com.mvvm.contactlist.viewmodel;

import android.util.Log;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mvvm.contactlist.R;
import com.mvvm.contactlist.db.entities.ContactEntity;
import com.mvvm.contactlist.repo.ContactRepository;
import com.mvvm.contactlist.utilities.Const;
import com.mvvm.contactlist.utilities.SingleLiveEvent;
import com.mvvm.contactlist.utilities.Utils;

import java.io.File;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactViewModel extends ViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private SingleLiveEvent<Integer> errorSuccMessage = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> progressDialog = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> selectImage = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> toContactList = new SingleLiveEvent<>();
    private SingleLiveEvent<ContactEntity> addContact = new SingleLiveEvent<>();

    private MutableLiveData<File> profilePic = new MutableLiveData<>();
    private MutableLiveData<Integer> placeHolderPic = new MutableLiveData<>();
    private MutableLiveData<String> firstName = new MutableLiveData<>();
    private MutableLiveData<String> lastName = new MutableLiveData<>();
    private MutableLiveData<String> mobileNum = new MutableLiveData<>();
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<Integer> btnText = new MutableLiveData<>();
    private MutableLiveData<Integer> categoryDefaultValue = new MutableLiveData<>();
    private ContactRepository repository;
    private int selectedCategoryId = 0;
    private int contactId = 0;

    /**
     * @param repository ContactRepository category dao and contact dao provider
     */
    private ContactViewModel(ContactRepository repository) {
        this.repository = repository;
        btnText.setValue(R.string.lbl_save);
    }

    /**
     * @param contactId Contact ID for the update procedure
     */
    public void setValuesForUpdate(int contactId) {

        this.contactId = contactId;

        mDisposable.add(repository.getContactFromContactId(contactId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                            btnText.setValue(R.string.lbl_update);
                            profilePic.setValue(new File(model.getProfilePic()));
                            firstName.setValue(model.getFirstName());
                            lastName.setValue(model.getLastName());
                            mobileNum.setValue(model.getMobileNum());
                            email.setValue(model.getEmail());
                            categoryDefaultValue.setValue(model.getCategoryId());
                        },
                        throwable -> errorSuccMessage.setValue(R.string.lbl_something_wrong)));
    }

    public MutableLiveData<Integer> getCategoryDefaultValue() {
        return categoryDefaultValue;
    }

    public MutableLiveData<Integer> getBtnText() {
        return btnText;
    }

    public MutableLiveData<Integer> getPlaceHolderPic() {
        return placeHolderPic;
    }

    public SingleLiveEvent<ContactEntity> getAddContact() {
        return addContact;
    }

    public SingleLiveEvent<Boolean> getProgressDialog() {
        return progressDialog;
    }

    public SingleLiveEvent<Void> getToContactList() {
        return toContactList;
    }

    public void addDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }

    public Single<List<String>> getAllCategoryName() {
        return repository.getAllCategoryName();
    }

    public MutableLiveData<File> getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Integer placeHolder) {
        placeHolderPic.setValue(placeHolder);
    }

    public SingleLiveEvent<Void> getSelectImage() {
        return selectImage;
    }

    public SingleLiveEvent<Integer> getErrorSuccMessage() {
        return errorSuccMessage;
    }

    public MutableLiveData<String> getFirstName() {
        return firstName;
    }

    public MutableLiveData<String> getLastName() {
        return lastName;
    }

    public MutableLiveData<String> getMobileNum() {
        return mobileNum;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    /**
     * @param parent get Adapter view then using this view you get the selected values from the Spinner
     */
    public void onSelectItem(AdapterView<?> parent) {
        //pos                                 get selected item position
        //view.getText()                      get label of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getAdapter().getCount()      get item count
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        //and other...

        String selectedCatg = parent.getSelectedItem().toString();
        repository.findIdByCatgName(selectedCatg)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        selectedCategoryId = integer;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(Const.TAG, Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * when user click on photo this method call and bind with image picker
     */
    public void onProfilePicSelect() {
        selectImage.setValue(null);
    }

    /**
     * @param file compressed image received in the File format
     */
    public void onImageReceived(File file) {
        profilePic.setValue(file);
    }

    /**
     * When user press save or update button this method called
     */
    public void onSaveClick() {

        ContactEntity entity = new ContactEntity();

        File file = profilePic.getValue();
        if (file != null) {
            String path = file.getAbsolutePath();
            entity.setProfilePic(path);
        } else {
            errorSuccMessage.setValue(R.string.err_msg_select_profile_pic);
            return;
        }

        String strFirstName = firstName.getValue();
        if (strFirstName != null && !strFirstName.isEmpty()) {
            entity.setFirstName(strFirstName.trim());
        } else {
            errorSuccMessage.setValue(R.string.err_msg_first_name);
            return;
        }

        String strLastName = lastName.getValue();
        if (strLastName != null && !strLastName.isEmpty()) {
            entity.setLastName(strLastName.trim());
        } else {
            errorSuccMessage.setValue(R.string.err_msg_last_name);
            return;
        }

        String strMobileNum = mobileNum.getValue();
        if (strMobileNum != null && !strMobileNum.isEmpty()) {
            entity.setMobileNum(strMobileNum.trim());
        } else {
            errorSuccMessage.setValue(R.string.err_msg_mobile_num);
            return;
        }

        String strEmail = email.getValue();
        if (strEmail != null && !strEmail.isEmpty()) {
            if (Utils.isValidEmail(strEmail)) {
                entity.setEmail(strEmail.trim());
            } else {
                errorSuccMessage.setValue(R.string.err_msg_email_valid);
                return;
            }
        } else {
            errorSuccMessage.setValue(R.string.err_msg_email);
            return;
        }

        if (selectedCategoryId != 0) {
            entity.setCategoryId(selectedCategoryId);
        } else {
            errorSuccMessage.setValue(R.string.err_msg_select_ctg);
            return;
        }

        progressDialog.setValue(true);

        if (btnText.getValue() != null && btnText.getValue() == R.string.lbl_save) {
            mDisposable.add(repository.insertContact(entity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {

                                addContact.setValue(entity);

                                placeHolderPic.setValue(R.drawable.ic_place_holder);
                                firstName.setValue("");
                                lastName.setValue("");
                                mobileNum.setValue("");
                                email.setValue("");

                                progressDialog.setValue(false);
                                toContactList.setValue(null);
                            },
                            throwable -> {
                                progressDialog.setValue(false);
                                errorSuccMessage.setValue(R.string.lbl_something_wrong);
                            }));
        } else {

            entity.setContactId(contactId);

            mDisposable.add(repository.updateContact(entity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {

                                placeHolderPic.setValue(R.drawable.ic_place_holder);
                                firstName.setValue("");
                                lastName.setValue("");
                                mobileNum.setValue("");
                                email.setValue("");

                                progressDialog.setValue(false);
                                toContactList.setValue(null);
                            },
                            throwable -> {
                                progressDialog.setValue(false);
                                errorSuccMessage.setValue(R.string.lbl_something_wrong);
                            }));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

    public static class ContactViewModelFactory extends ViewModelProvider.NewInstanceFactory {

        private ContactRepository repository;

        public ContactViewModelFactory(ContactRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ContactViewModel(repository);
        }
    }
}