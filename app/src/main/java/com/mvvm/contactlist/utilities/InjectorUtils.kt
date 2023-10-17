package com.mvvm.contactlist.utilities

import android.content.Context
import com.mvvm.contactlist.db.AppDatabase.Companion.getDatabase
import com.mvvm.contactlist.repo.CategoryRepository
import com.mvvm.contactlist.repo.ContactRepository
import com.mvvm.contactlist.viewmodel.CategoryViewModel.AddCategoryViewModelFactory
import com.mvvm.contactlist.viewmodel.ContactListViewModel.ContactListViewModelFactory
import com.mvvm.contactlist.viewmodel.ContactViewModel.ContactViewModelFactory

object InjectorUtils {
    fun provideAddCategoryViewModelFactory(context: Context): AddCategoryViewModelFactory {
        val dao = getDatabase(context.applicationContext).categoryDao()
        val repository = CategoryRepository.getInstance(dao)
        return AddCategoryViewModelFactory(repository)
    }

    fun provideContactViewModelFactory(context: Context): ContactViewModelFactory {
        val categoryDao = getDatabase(context.applicationContext).categoryDao()
        val contactDao = getDatabase(context.applicationContext).contactDao()
        val repository = ContactRepository.getInstance(categoryDao, contactDao)
        return ContactViewModelFactory(repository)
    }

    fun provideContactListViewModelFactory(context: Context): ContactListViewModelFactory {
        val categoryDao = getDatabase(context.applicationContext).categoryDao()
        val contactDao = getDatabase(context.applicationContext).contactDao()
        val repository = ContactRepository.getInstance(categoryDao, contactDao)
        return ContactListViewModelFactory(repository)
    }
}