package com.mvvm.contactlist.repo

import com.mvvm.contactlist.db.dao.CategoryDao
import com.mvvm.contactlist.db.entities.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Flowable

class CategoryRepository private constructor(private val categoryDao: CategoryDao) {
    val category: Flowable<List<CategoryEntity>>
        get() = categoryDao.category

    fun insert(entity: CategoryEntity): Completable {
        return categoryDao.insert(entity)
    }

    fun update(entity: CategoryEntity): Completable {
        return categoryDao.update(entity)
    }

    fun delete(entity: CategoryEntity): Completable {
        return categoryDao.delete(entity)
    }

    companion object {
        private lateinit var repository: CategoryRepository

        fun getInstance(categoryDao: CategoryDao): CategoryRepository {
            if (!::repository.isInitialized) {
                repository = CategoryRepository(categoryDao)
            }
            return repository
        }
    }
}