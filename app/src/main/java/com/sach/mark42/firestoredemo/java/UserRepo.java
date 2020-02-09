package com.sach.mark42.firestoredemo.java;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sach.mark42.firestoreassistant.java.FirestoreRepo;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UserRepo extends FirestoreRepo<User> {

    private static UserRepo INSTANCE;

    @Nullable
    @Override
    public User convertDocumentSnapshot(@Nullable DocumentSnapshot documentSnapshot) {
        return documentSnapshot.toObject(User.class);
    }

    @Nullable
    @Override
    public List<User> convertQuerySnapshot(@Nullable QuerySnapshot value) {
        ArrayList<User> users = new ArrayList<>();
        for (DocumentSnapshot snapshot: value.getDocuments()) {
            try {
                User user = snapshot.toObject(User.class);
                users.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public static UserRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepo();
        }
        return INSTANCE;
    }
}
