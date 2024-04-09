package com.example.bubblewash.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.bubblewash.model.CardDetails;

@Dao
public interface CardDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCardDetails(CardDetails card);
}
