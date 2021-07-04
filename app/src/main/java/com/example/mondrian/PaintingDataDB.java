package com.example.mondrian;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PaintingData.class}, version = 1, exportSchema = false)
public abstract class PaintingDataDB extends RoomDatabase {
    public abstract PaintingDataDao paintingDataDao();
    private static PaintingDataDB INSTANCE;
    static PaintingDataDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PaintingDataDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PaintingDataDB.class, "painting_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
