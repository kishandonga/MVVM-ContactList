package com.mvvm.contactlist.utilities;

import android.content.Context;

import com.mvvm.contactlist.db.AppDatabase;
import com.mvvm.contactlist.db.dao.CategoryDao;
import com.mvvm.contactlist.db.dao.ContactDao;
import com.mvvm.contactlist.repo.CategoryRepository;
import com.mvvm.contactlist.repo.ContactRepository;
import com.mvvm.contactlist.viewmodel.CategoryViewModel;
import com.mvvm.contactlist.viewmodel.ContactListViewModel;
import com.mvvm.contactlist.viewmodel.ContactViewModel;

public class InjectorUtils {

    public static CategoryViewModel.AddCategoryViewModelFactory provideAddCategoryViewModelFactory(Context context) {
        CategoryDao dao = AppDatabase.getDatabase(context.getApplicationContext()).categoryDao();
        CategoryRepository repository = CategoryRepository.getInstance(dao);
        return new CategoryViewModel.AddCategoryViewModelFactory(repository);
    }

    public static ContactViewModel.ContactViewModelFactory provideContactViewModelFactory(Context context) {
        CategoryDao categoryDao = AppDatabase.getDatabase(context.getApplicationContext()).categoryDao();
        ContactDao contactDao = AppDatabase.getDatabase(context.getApplicationContext()).contactDao();
        ContactRepository repository = ContactRepository.getInstance(categoryDao, contactDao);
        return new ContactViewModel.ContactViewModelFactory(repository);
    }

    public static ContactListViewModel.ContactListViewModelFactory provideContactListViewModelFactory(Context context) {
        CategoryDao categoryDao = AppDatabase.getDatabase(context.getApplicationContext()).categoryDao();
        ContactDao contactDao = AppDatabase.getDatabase(context.getApplicationContext()).contactDao();
        ContactRepository repository = ContactRepository.getInstance(categoryDao, contactDao);
        return new ContactListViewModel.ContactListViewModelFactory(repository);
    }
}
