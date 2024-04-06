package com.example.bubblewash.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bubblewash.model.Booking;
import com.example.bubblewash.model.TimeDuration;
import com.example.bubblewash.model.MonthCostTuple;

import java.util.List;

@Dao
public interface BookingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertBookingsFromList(List<Booking> bookingList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneBooking(Booking booking);

    @Query("SELECT * from bookings")
    List<Booking> getAllBookings();

    @Query("SELECT * from bookings WHERE userId=:userId AND status='DELIVER'")
    List<Booking> getAllPastBookingsForUser(String userId);

    @Query("SELECT bookingMonth, SUM(totalCost) AS cost from bookings WHERE userId=:userId GROUP BY bookingMonth")
    List<MonthCostTuple> getMonthlyUsage(String userId);

    @Query("SELECT * from bookings WHERE userId=:userId AND status IN ('CONFIRM', 'PICK', 'WASH', 'DRY')")
    List<Booking> getCurrentUserBooking(String userId);
}
