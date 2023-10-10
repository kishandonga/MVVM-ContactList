package com.mvvm.contactlist.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mvvm.contactlist.R;
import com.mvvm.contactlist.adapter.ContactListAdapter;
import com.mvvm.contactlist.adapter.SearchCategoryAdapter;
import com.mvvm.contactlist.db.entities.CategoryEntity;
import com.mvvm.contactlist.db.entities.ContactEntity;
import com.mvvm.contactlist.repo.ContactRepository;
import com.mvvm.contactlist.utilities.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactListViewModel extends ViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private final SingleLiveEvent<Integer> errorSuccMessage = new SingleLiveEvent<>();
    private final MutableLiveData<String> searchText = new MutableLiveData<>();
    private final SingleLiveEvent<Integer> toUpdateContact = new SingleLiveEvent<>();
    private final ContactListAdapter contactListAdapter;
    private final SearchCategoryAdapter searchCategoryAdapter;
    private final ContactRepository repository;
    private List<ContactEntity> contactEntities = new ArrayList<>();

    /**
     * @param repository ContactRepository category dao provider
     */
    private ContactListViewModel(ContactRepository repository) {
        this.repository = repository;
        contactListAdapter = new ContactListAdapter(new ContactListAdapter.DiffCallback(), this);
        searchCategoryAdapter = new SearchCategoryAdapter(new SearchCategoryAdapter.DiffCallback(), this);
    }

    /**
     * set data in the adapter after the binding done
     */
    public void setCategoryInAdapter() {
        mDisposable.add(repository.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                            searchCategoryAdapter.submitList(models);
                            searchCategoryAdapter.notifyDataSetChanged();
                        },
                        throwable -> errorSuccMessage.setValue(R.string.lbl_something_wrong)));
    }

    /**
     * set data in the adapter after the binding done
     */
    public void setContactInAdapter() {
        mDisposable.add(repository.getAllContact()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                            contactEntities = models;
                            contactListAdapter.submitList(models);
                            contactListAdapter.notifyDataSetChanged();
                        },
                        throwable -> errorSuccMessage.setValue(R.string.lbl_something_wrong)));
    }

    public List<ContactEntity> getContactEntities() {
        return contactEntities;
    }

    public void addDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }

    public SingleLiveEvent<Integer> getToUpdateContact() {
        return toUpdateContact;
    }

    public SearchCategoryAdapter getSearchCategoryAdapter() {
        return searchCategoryAdapter;
    }

    public MutableLiveData<String> getSearchText() {
        return searchText;
    }

    public ContactListAdapter getAdapter() {
        return contactListAdapter;
    }

    /**
     * when user press edit button then this method call from the adapter
     *
     * @param entity and give ContactEntity
     */
    public void onUpdateClick(ContactEntity entity) {
        toUpdateContact.setValue(entity.getContactId());
    }

    /**
     * when user press delete button then this method call from the adapter
     *
     * @param entity and give ContactEntity
     */
    public void onDeleteClick(ContactEntity entity) {
        mDisposable.add(repository.deleteContact(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> errorSuccMessage.setValue(R.string.lbl_contact_deleted),
                        throwable -> errorSuccMessage.setValue(R.string.lbl_something_wrong)));
    }

    /**
     * when user select category from list then this method call from the adapter and do searching accordingly
     *
     * @param entity and give CategoryEntity
     */
    public void onCategorySelected(CategoryEntity entity) {

        mDisposable.add(repository.getContactFromCategory(entity.getCategoryId())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                            contactEntities = models;
                            contactListAdapter.submitList(models);
                            contactListAdapter.notifyDataSetChanged();
                        },
                        throwable -> errorSuccMessage.setValue(R.string.lbl_something_wrong)));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

    public static class ContactListViewModelFactory implements ViewModelProvider.Factory {

        private final ContactRepository repository;

        public ContactListViewModelFactory(ContactRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ContactListViewModel(repository);
        }
    }
}