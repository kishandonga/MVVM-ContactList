package com.mvvm.contactlist.repo;

import com.mvvm.contactlist.db.dao.CategoryDao;
import com.mvvm.contactlist.db.entities.CategoryEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class CategoryRepository {

    private static CategoryRepository repository;
    private final CategoryDao categoryDao;

    private CategoryRepository(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public static CategoryRepository getInstance(CategoryDao categoryDao) {
        if (repository == null) {
            repository = new CategoryRepository(categoryDao);
        }

        return repository;
    }

    public Flowable<List<CategoryEntity>> getCategory() {
        return categoryDao.getCategory();
    }

    public Completable insert(CategoryEntity entity) {
        return categoryDao.insert(entity);
    }

    public Completable update(CategoryEntity entity) {
        return categoryDao.update(entity);
    }

    public Completable delete(CategoryEntity entity) {
        return categoryDao.delete(entity);
    }
}
