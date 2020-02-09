package com.sach.mark42.firestoredemo.java;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class App extends Application {

    private static Boolean firebaseInstanceInitialized = false;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!FirebaseApp.getApps(this).isEmpty() && !firebaseInstanceInitialized) {
            firebaseInstanceInitialized = true;
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            db.setFirestoreSettings(settings);
        }
    }
}
