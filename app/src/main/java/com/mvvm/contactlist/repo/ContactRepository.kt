package com.mvvm.contactlist.repo

import com.mvvm.contactlist.db.dao.CategoryDao
import com.mvvm.contactlist.db.dao.ContactDao
import com.mvvm.contactlist.db.entities.CategoryEntity
import com.mvvm.contactlist.db.entities.ContactEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class ContactRepository private constructor(
    private val categoryDao: CategoryDao,
    private val contactDao: ContactDao
) {

    val category: Flowable<List<CategoryEntity>>
        get() = categoryDao.category
    val allContact: Flowable<List<ContactEntity>>
        get() = contactDao.allContact
    val allCategoryName: Single<List<String>>
        get() = categoryDao.allCategoryName

    fun getContactFromCategory(categoryId: Int): Single<List<ContactEntity>> {
        return contactDao.getContactFromCategory(categoryId)
    }

    fun findIdByCatgName(category: String): Maybe<Int> {
        return categoryDao.findIdByCatgName(category)
    }

    fun insertContact(entity: ContactEntity): Completable {
        return contactDao.insert(entity)
    }

    fun deleteContact(entity: ContactEntity): Completable {
        return contactDao.delete(entity)
    }

    fun updateContact(entity: ContactEntity): Completable {
        return contactDao.update(entity)
    }

    fun getContactFromContactId(id: Int): Maybe<ContactEntity> {
        return contactDao.getContactFromContactId(id)
    }

    companion object {
        private lateinit var repository: ContactRepository

        fun getInstance(categoryDao: CategoryDao, contactDao: ContactDao): ContactRepository {
            if (!::repository.isInitialized) {
                repository = ContactRepository(categoryDao, contactDao)
            }
            return repository
        }
    }
}