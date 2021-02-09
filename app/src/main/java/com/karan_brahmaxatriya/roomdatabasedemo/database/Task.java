package com.karan_brahmaxatriya.roomdatabasedemo.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "fullname")
    private String fullname;

    @ColumnInfo(name = "mobile")
    private String mobile;

    @ColumnInfo(name = "email")
    private String email;


    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "dob")
    private String dob;

    @ColumnInfo(name = "spin_value")
    private String spin_value;

    @ColumnInfo(name = "position")
    private int position;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;
    public boolean swiped = false;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSpin_value() {
        return spin_value;
    }

    public void setSpin_value(String spin_value) {
        this.spin_value = spin_value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}