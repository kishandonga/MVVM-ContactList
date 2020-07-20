package com.mvvm.contactlist.repo;

import com.mvvm.contactlist.db.dao.CategoryDao;
import com.mvvm.contactlist.db.dao.ContactDao;
import com.mvvm.contactlist.db.entities.CategoryEntity;
import com.mvvm.contactlist.db.entities.ContactEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class ContactRepository {

    private static ContactRepository repository;
    private CategoryDao categoryDao;
    private ContactDao contactDao;

    private ContactRepository(CategoryDao categoryDao, ContactDao contactDao) {
        this.categoryDao = categoryDao;
        this.contactDao = contactDao;
    }

    public static ContactRepository getInstance(CategoryDao categoryDao, ContactDao contactDao) {
        if (repository == null) {
            repository = new ContactRepository(categoryDao, contactDao);
        }

        return repository;
    }

    public Single<List<ContactEntity>> getContactFromCategory(int categoryId) {
        return contactDao.getContactFromCategory(categoryId);
    }

    public Flowable<List<CategoryEntity>> getCategory() {
        return categoryDao.getCategory();
    }

    public Flowable<List<ContactEntity>> getAllContact() {
        return contactDao.getAllContact();
    }

    public Single<List<String>> getAllCategoryName() {
        return categoryDao.getAllCategoryName();
    }

    public Maybe<Integer> findIdByCatgName(String category) {
        return categoryDao.findIdByCatgName(category);
    }

    public Completable insertContact(ContactEntity entity) {
        return contactDao.insert(entity);
    }

    public Completable deleteContact(ContactEntity entity) {
        return contactDao.delete(entity);
    }

    public Completable updateContact(ContactEntity entity) {
        return contactDao.update(entity);
    }

    public Maybe<ContactEntity> getContactFromContactId(int id) {
        return contactDao.getContactFromContactId(id);
    }
}
