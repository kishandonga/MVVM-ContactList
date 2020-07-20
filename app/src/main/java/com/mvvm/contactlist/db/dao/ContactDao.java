package com.mvvm.contactlist.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mvvm.contactlist.db.entities.ContactEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM Tbl_Contact")
    Flowable<List<ContactEntity>> getAllContact();

    @Query("SELECT * FROM Tbl_Contact WHERE category_Id=:categoryId")
    Single<List<ContactEntity>> getContactFromCategory(int categoryId);

    @Query("SELECT * FROM Tbl_Contact WHERE contactId=:id")
    Maybe<ContactEntity> getContactFromContactId(int id);

    @Insert
    Completable insert(ContactEntity entity);

    @Update
    Completable update(ContactEntity entity);

    @Delete
    Completable delete(ContactEntity entity);
}
