package com.mvvm.contactlist.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Tbl_Category", indices = {@Index(value = {"category_name"}, unique = true)})
public class CategoryEntity implements Comparable<CategoryEntity> {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_Id")
    private int categoryId;

    @ColumnInfo(name = "category_name")
    private String categoryName = "";

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public int compareTo(CategoryEntity o) {
        if (categoryId == o.getCategoryId() && o.getCategoryName().equals(categoryName))
            return 1;
        return 0;
    }
}
