package com.sach.mark42.firestoredemo.java;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sach.mark42.firestoreassistant.java.FirestoreRepo;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UsersRepo extends FirestoreRepo<List<User>> {

    private static UsersRepo INSTANCE;

    @Nullable
    @Override
    public List<User> convertDocumentSnapshot(@Nullable DocumentSnapshot value) {
        return null;
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

    public static UsersRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepo();
        }
        return INSTANCE;
    }
}
