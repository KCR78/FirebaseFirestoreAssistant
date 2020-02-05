package com.sach.mark42.firestoredemo.java;

import com.google.firebase.firestore.PropertyName;

public class User {

    private String firstName;
    private String lastName;
    private String email;

    //Required an empty constructor for firabase
    public User() {}

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @PropertyName(FIRESTORE_KEY.USER.firstName)
    public String getFirstName() {
        return firstName;
    }

    @PropertyName(FIRESTORE_KEY.USER.firstName)
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @PropertyName(FIRESTORE_KEY.USER.lastName)
    public String getLastName() {
        return lastName;
    }

    @PropertyName(FIRESTORE_KEY.USER.lastName)
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @PropertyName(FIRESTORE_KEY.USER.email)
    public String getEmail() {
        return email;
    }

    @PropertyName(FIRESTORE_KEY.USER.email)
    public void setEmail(String email) {
        this.email = email;
    }
}
