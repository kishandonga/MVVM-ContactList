package com.mvvm.contactlist.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mvvm.contactlist.db.dao.CategoryDao;
import com.mvvm.contactlist.db.dao.ContactDao;
import com.mvvm.contactlist.db.entities.CategoryEntity;
import com.mvvm.contactlist.db.entities.ContactEntity;


@Database(entities = {CategoryEntity.class, ContactEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "contact_list.db";
    private static volatile AppDatabase database;

    public static AppDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized (AppDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                }
                            })
                            .build();
                }
            }
        }
        return database;
    }

    public abstract CategoryDao categoryDao();

    public abstract ContactDao contactDao();
}
