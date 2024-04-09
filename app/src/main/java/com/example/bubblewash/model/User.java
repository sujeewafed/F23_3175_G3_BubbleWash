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

    @NonNull
    @ColumnInfo(name = "username")
    private String userName;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "firstName")
    private String firstName;

    @ColumnInfo(name = "lastName")
    private String lastName;

    @ColumnInfo(name = "mobileNumber")
    private String mobileNumber;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "isAdmin")
    private boolean isAdmin;

    @ColumnInfo(name = "imagePath")
    private String imagePath;

    public User() {
    }

    public User(@NonNull String id, @NonNull String userName, @NonNull String password, String firstName, String lastName, String mobileNumber, String address, String imagePath) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.imagePath = imagePath;
    }

    public User(@NonNull String id, @NonNull String userName, @NonNull String password){
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public User(@NonNull String id, @NonNull String userName, @NonNull String password, boolean isAdmin) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
