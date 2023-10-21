package com.mvvm.contactlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvvm.contactlist.R
import com.mvvm.contactlist.adapter.ContactListAdapter
import com.mvvm.contactlist.adapter.SearchCategoryAdapter
import com.mvvm.contactlist.db.entities.CategoryEntity
import com.mvvm.contactlist.db.entities.ContactEntity
import com.mvvm.contactlist.repo.ContactRepository
import com.mvvm.contactlist.utilities.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ContactListViewModel private constructor(private val repository: ContactRepository) :
    ViewModel() {

    private val mDisposable = CompositeDisposable()
    private val errorSuccMessage = SingleLiveEvent<Int>()
    val searchText = MutableLiveData<String>()
    val toUpdateContact = SingleLiveEvent<Int>()
    val adapter: ContactListAdapter = ContactListAdapter(ContactListAdapter.DiffCallback(), this)
    val searchCategoryAdapter: SearchCategoryAdapter =
        SearchCategoryAdapter(SearchCategoryAdapter.DiffCallback(), this)
    var contactEntities: List<ContactEntity> = ArrayList()

    /**
     * set data in the adapter after the binding done
     */
    fun setCategoryInAdapter() {
        mDisposable.add(
            repository.category
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { models: List<CategoryEntity> ->
                        searchCategoryAdapter.submitList(models.toMutableList())
                    }
                ) { _: Throwable? -> errorSuccMessage.setValue(R.string.lbl_something_wrong) })
    }

    /**
     * set data in the adapter after the binding done
     */
    fun setContactInAdapter() {
        mDisposable.add(
            repository.allContact
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { models: List<ContactEntity> ->
                        contactEntities = models
                        adapter.submitList(models.toMutableList())
                    }
                ) { _: Throwable? -> errorSuccMessage.setValue(R.string.lbl_something_wrong) })
    }

    fun addDisposable(disposable: Disposable) {
        mDisposable.add(disposable)
    }

    /**
     * when user press edit button then this method call from the adapter
     *
     * @param entity and give ContactEntity
     */
    fun onUpdateClick(entity: ContactEntity) {
        toUpdateContact.value = entity.contactId
    }

    /**
     * when user press delete button then this method call from the adapter
     *
     * @param entity and give ContactEntity
     */
    fun onDeleteClick(entity: ContactEntity) {
        mDisposable.add(
            repository.deleteContact(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { errorSuccMessage.setValue(R.string.lbl_contact_deleted) }
                ) { _: Throwable? -> errorSuccMessage.setValue(R.string.lbl_something_wrong) })
    }

    /**
     * when user select category from list then this method call from the adapter and do searching accordingly
     *
     * @param entity and give CategoryEntity
     */
    fun onCategorySelected(entity: CategoryEntity) {
        mDisposable.add(
            repository.getContactFromCategory(entity.categoryId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { models: List<ContactEntity> ->
                        contactEntities = models
                        adapter.submitList(models.toMutableList())
                    }
                ) { _: Throwable? -> errorSuccMessage.setValue(R.string.lbl_something_wrong) })
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }

    class ContactListViewModelFactory(private val repository: ContactRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ContactListViewModel(repository) as T
        }
    }
}