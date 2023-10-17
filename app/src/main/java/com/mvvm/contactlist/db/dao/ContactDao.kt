package com.mvvm.contactlist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mvvm.contactlist.db.entities.ContactEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface ContactDao {

    @get:Query("SELECT * FROM Tbl_Contact")
    val allContact: Flowable<List<ContactEntity>>

    @Query("SELECT * FROM Tbl_Contact WHERE category_Id=:categoryId")
    fun getContactFromCategory(categoryId: Int): Single<List<ContactEntity>>

    @Query("SELECT * FROM Tbl_Contact WHERE contactId=:id")
    fun getContactFromContactId(id: Int): Maybe<ContactEntity>

    @Insert
    fun insert(entity: ContactEntity): Completable

    @Update
    fun update(entity: ContactEntity): Completable

    @Delete
    fun delete(entity: ContactEntity): Completable
}