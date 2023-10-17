package com.mvvm.contactlist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mvvm.contactlist.db.entities.CategoryEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CategoryDao {

    @get:Query("SELECT * FROM Tbl_Category")
    val category: Flowable<List<CategoryEntity>>

    @get:Query("SELECT category_name FROM Tbl_Category")
    val allCategoryName: Single<List<String>>

    @Query("SELECT category_Id FROM Tbl_Category WHERE category_name=:category")
    fun findIdByCatgName(category: String): Maybe<Int>

    @Insert
    fun insert(entity: CategoryEntity): Completable

    @Update
    fun update(entity: CategoryEntity): Completable

    @Delete
    fun delete(entity: CategoryEntity): Completable
}