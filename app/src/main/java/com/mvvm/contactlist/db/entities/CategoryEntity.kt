package com.mvvm.contactlist.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Tbl_Category", indices = [Index(value = ["category_name"], unique = true)])
class CategoryEntity : Comparable<CategoryEntity> {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_Id")
    var categoryId = 0

    @ColumnInfo(name = "category_name")
    var categoryName = ""
    override fun compareTo(other: CategoryEntity): Int {
        return if (categoryId == other.categoryId && other.categoryName == categoryName) 1 else 0
    }
}