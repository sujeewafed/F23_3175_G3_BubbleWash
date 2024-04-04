package com.example.bubblewash.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bubblewash.model.Booking;

import java.util.List;

@Dao
public interface BookingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertBookingsFromList(List<Booking> bookingList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneBooking(Booking booking);

    @Query("SELECT * from bookings")
    List<Booking> GetAllBookings();

    @Query("SELECT * from bookings WHERE id=:userId AND status='DELIVER'")
    List<Booking> GetAllPastBookingsForUser(String userId);
}
