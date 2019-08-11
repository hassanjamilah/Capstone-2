package com.andalus.a_zmedicines.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;


import com.andalus.a_zmedicines.MyClasses.Composition;
import com.andalus.a_zmedicines.MyClasses.Drug;

@Database(entities = {Drug.class, Composition.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DrugsDatabase extends RoomDatabase {
    private static final Object lock = new Object();
    private static final String DATABASE_NAME = "drugs.db";
    private static DrugsDatabase mInstance;

    public static DrugsDatabase getDatabaseInstance(Context context) {
        Log.i("hassan", "Will get databaase instance");
        if (mInstance == null) {
            synchronized (lock) {

                mInstance = Room.databaseBuilder(context, DrugsDatabase.class, DATABASE_NAME).build();
                Log.i("hassan", "The database created successfully");
            }
        }
        return mInstance;
    }

    public abstract DrugDao drugsDao();


}
