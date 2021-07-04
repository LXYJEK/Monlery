package com.example.mondrian;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PaintingDataDao {
    @Insert
    void insert(PaintingData paintingData);

    @Query("DELETE FROM painting_table")
    void deleteAll();

    @Query("SELECT * from painting_table ORDER BY id")
    List<PaintingData> getAllPaintings();
}
