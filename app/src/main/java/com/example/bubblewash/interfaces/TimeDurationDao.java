package com.example.bubblewash.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bubblewash.model.TimeDuration;
import com.example.bubblewash.model.User;

import java.util.List;

@Dao
public interface TimeDurationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertTimesFromList(List<TimeDuration> timesList);

    @Query("SELECT * from timeDurations")
    List<TimeDuration> getAllTimes();

    @Query("SELECT timeDurations.starttime FROM timeDurations WHERE timeDurations.starttime NOT IN " +
            "(SELECT bookings.washTime FROM bookings WHERE Bookings.Date=:bookingDate)")
    List<TimeDuration> getWasherTimeDurationList(String bookingDate);

    @Query("SELECT timeDurations.starttime FROM timeDurations WHERE timeDurations.starttime NOT IN " +
            "(SELECT bookings.dryTime FROM bookings WHERE Bookings.Date=:bookingDate)")
    List<TimeDuration> getDryerTimeDurationList(String bookingDate);

}
