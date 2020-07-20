package com.mvvm.contactlist.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mvvm.contactlist.db.entities.CategoryEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM Tbl_Category")
    Flowable<List<CategoryEntity>> getCategory();

    @Query("SELECT category_name FROM Tbl_Category")
    Single<List<String>> getAllCategoryName();

    @Query("SELECT category_Id FROM Tbl_Category WHERE category_name=:category")
    Maybe<Integer> findIdByCatgName(String category);

    @Insert
    Completable insert(CategoryEntity entity);

    @Update
    Completable update(CategoryEntity entity);

    @Delete
    Completable delete(CategoryEntity entity);
}
