package com.example.bubblewash.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bubblewash.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertUsersFromList(List<User> usersList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneUser(User user);

    @Query("SELECT * FROM users WHERE username= :userName AND passoword= :password")
    User getUser(String userName, String password);

}
