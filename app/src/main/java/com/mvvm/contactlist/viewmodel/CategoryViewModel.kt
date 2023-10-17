package com.mvvm.contactlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvvm.contactlist.R
import com.mvvm.contactlist.adapter.CategoryListAdapter
import com.mvvm.contactlist.db.entities.CategoryEntity
import com.mvvm.contactlist.repo.CategoryRepository
import com.mvvm.contactlist.utilities.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CategoryViewModel private constructor(private val repository: CategoryRepository) :
    ViewModel() {

    private val mDisposable = CompositeDisposable()
    val category = MutableLiveData<String>()
    val errorSuccMessage = SingleLiveEvent<Int>()
    val adapter: CategoryListAdapter = CategoryListAdapter(CategoryListAdapter.DiffCallback(), this)

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
                        adapter.submitList(models.toMutableList())
                    }
                ) { _: Throwable? -> errorSuccMessage.setValue(R.string.lbl_something_wrong) })
    }

    /**
     * when user press the save button then
     * this method called and save category in the db
     */
    fun onSaveClick() {
        val categoryValue = category.value
        if (!categoryValue.isNullOrEmpty()) {
            val entity = CategoryEntity()
            entity.categoryName = categoryValue.trim { it <= ' ' }
            mDisposable.add(
                repository.insert(entity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            category.value = ""
                            errorSuccMessage.setValue(R.string.lbl_category_added)
                        }
                    ) { _: Throwable? -> errorSuccMessage.setValue(R.string.lbl_something_wrong) })
        } else {
            errorSuccMessage.setValue(R.string.error_msg_enter_category)
        }
    }

    /**
     * when user press delete button then this method call from the adapter
     *
     * @param entity and give CategoryEntity
     */
    fun onDeleteClick(entity: CategoryEntity) {
        mDisposable.add(
            repository.delete(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { errorSuccMessage.setValue(R.string.lbl_category_deleted) }
                ) { _: Throwable? -> errorSuccMessage.setValue(R.string.lbl_something_wrong) })
    }

    /**
     * when user press edit button then this method call from the adapter
     *
     * @param entity and give CategoryEntity
     */
    fun onUpdateClick(entity: CategoryEntity) {
        val categoryValue = category.value
        if (!categoryValue.isNullOrEmpty()) {
            entity.categoryName = categoryValue.trim { it <= ' ' }
            mDisposable.add(
                repository.update(entity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { errorSuccMessage.setValue(R.string.lbl_category_updated) }
                    ) { _: Throwable? -> errorSuccMessage.setValue(R.string.lbl_something_wrong) })
        } else {
            errorSuccMessage.setValue(R.string.error_msg_enter_category)
        }
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }

    class AddCategoryViewModelFactory(private val repository: CategoryRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(repository) as T
        }
    }
}