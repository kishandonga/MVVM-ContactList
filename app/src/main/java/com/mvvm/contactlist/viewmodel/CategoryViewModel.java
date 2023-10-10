package com.mvvm.contactlist.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mvvm.contactlist.R;
import com.mvvm.contactlist.adapter.CategoryListAdapter;
import com.mvvm.contactlist.db.entities.CategoryEntity;
import com.mvvm.contactlist.repo.CategoryRepository;
import com.mvvm.contactlist.utilities.SingleLiveEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class CategoryViewModel extends ViewModel {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private final CategoryRepository repository;
    private final MutableLiveData<String> edCategory = new MutableLiveData<>();
    private final SingleLiveEvent<Integer> errorSuccMessage = new SingleLiveEvent<>();
    private final CategoryListAdapter categoryListAdapter;

    /**
     * @param repository CategoryRepository category dao provider
     */
    private CategoryViewModel(CategoryRepository repository) {
        this.repository = repository;
        categoryListAdapter = new CategoryListAdapter(new CategoryListAdapter.DiffCallback(), this);
    }

    /**
     * set data in the adapter after the binding done
     */
    public void setCategoryInAdapter() {
        mDisposable.add(repository.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                            categoryListAdapter.submitList(models);
                            categoryListAdapter.notifyDataSetChanged();
                        },
                        throwable -> errorSuccMessage.setValue(R.string.lbl_something_wrong)));
    }

    public CategoryListAdapter getAdapter() {
        return categoryListAdapter;
    }

    public SingleLiveEvent<Integer> getErrorSuccMessage() {
        return errorSuccMessage;
    }

    public MutableLiveData<String> getCategory() {
        return edCategory;
    }

    /**
     * when user press the save button then
     * this method called and save category in the db
     */
    public void onSaveClick() {
        String category = edCategory.getValue();
        if (category != null && !category.isEmpty()) {
            CategoryEntity entity = new CategoryEntity();
            entity.setCategoryName(category.trim());

            mDisposable.add(repository.insert(entity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                edCategory.setValue("");
                                errorSuccMessage.setValue(R.string.lbl_category_added);
                            },
                            throwable -> errorSuccMessage.setValue(R.string.lbl_something_wrong)));


        } else {
            errorSuccMessage.setValue(R.string.error_msg_enter_category);
        }
    }

    /**
     * when user press delete button then this method call from the adapter
     *
     * @param entity and give CategoryEntity
     */
    public void onDeleteClick(CategoryEntity entity) {
        mDisposable.add(repository.delete(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> errorSuccMessage.setValue(R.string.lbl_category_deleted),
                        throwable -> errorSuccMessage.setValue(R.string.lbl_something_wrong)));
    }

    /**
     * when user press edit button then this method call from the adapter
     *
     * @param entity and give CategoryEntity
     */
    public void onUpdateClick(CategoryEntity entity) {

        String category = edCategory.getValue();
        if (category != null && !category.isEmpty()) {
            entity.setCategoryName(category.trim());

            mDisposable.add(repository.update(entity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> errorSuccMessage.setValue(R.string.lbl_category_updated),
                            throwable -> errorSuccMessage.setValue(R.string.lbl_something_wrong)));
        } else {
            errorSuccMessage.setValue(R.string.error_msg_enter_category);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mDisposable.clear();
    }

    public static class AddCategoryViewModelFactory implements ViewModelProvider.Factory {

        private final CategoryRepository repository;

        public AddCategoryViewModelFactory(CategoryRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CategoryViewModel(repository);
        }
    }
}