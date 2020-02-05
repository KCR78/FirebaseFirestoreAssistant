package com.sach.mark42.firestoredemo.java;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sach.mark42.firestoreassistant.java.FirestoreRepo;

import org.jetbrains.annotations.Nullable;

public class UserRepo extends FirestoreRepo<User> {

    private static UserRepo INSTANCE;

    @Nullable
    @Override
    public User convertDocumentSnapshot(@Nullable DocumentSnapshot documentSnapshot) {
        return documentSnapshot.toObject(User.class);
    }

    @Nullable
    @Override
    public User convertQuerySnapshot(@Nullable QuerySnapshot querySnapshot) {
        return null;
    }

    public static UserRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepo();
        }
        return INSTANCE;
    }
}
