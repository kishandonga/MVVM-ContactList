package com.mvvm.contactlist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.mvvm.contactlist.db.dao.CategoryDao
import com.mvvm.contactlist.db.dao.ContactDao
import com.mvvm.contactlist.db.entities.CategoryEntity
import com.mvvm.contactlist.db.entities.ContactEntity

@Database(
    entities = [CategoryEntity::class, ContactEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun contactDao(): ContactDao

    companion object {
        private const val DB_NAME = "contact_list.db"

        @Volatile
        private lateinit var database: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            synchronized(AppDatabase::class.java) {
                if (!::database.isInitialized) {
                    database = databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    ).build()
                }
            }
            return database
        }
    }
}