package com.maciek.myapplication;

import java.io.Serializable;

/**
 * Created by Maciek on 2016-06-08.
 */
public class User implements Serializable{

    private static User mInstance = null;
    private String email;
    private String firstName;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public static User getInstance(){
        if(mInstance == null)
        {
            mInstance = new User();
        }
        return mInstance;
    }
}
