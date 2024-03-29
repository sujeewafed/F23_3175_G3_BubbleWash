package com.example.bubblewash.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.bubblewash.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertUsersFromList(List<User> usersList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneUser(User user);
}
