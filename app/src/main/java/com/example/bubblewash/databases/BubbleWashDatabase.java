package com.example.bubblewash.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bubblewash.interfaces.BookingDao;
import com.example.bubblewash.interfaces.UserDao;
import com.example.bubblewash.model.Booking;
import com.example.bubblewash.model.User;

@Database(entities = {User.class, Booking.class}, version = 1, exportSchema = false)
public abstract class BubbleWashDatabase extends RoomDatabase {
    public abstract UserDao userDAO();

    public abstract BookingDao bookingDao();
}
