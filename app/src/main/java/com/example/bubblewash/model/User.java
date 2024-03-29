package com.example.bubblewash.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;
    @ColumnInfo(name = "username")
    private String userName;

    @ColumnInfo(name = "passoword")
    private String passoword;

    public User() {
    }

    public User(@NonNull String id, String userName, String passoword) {
        this.id = id;
        this.userName = userName;
        this.passoword = passoword;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassoword() {
        return passoword;
    }

    public void setPassoword(String passowrd) {
        this.passoword = passowrd;
    }
}
