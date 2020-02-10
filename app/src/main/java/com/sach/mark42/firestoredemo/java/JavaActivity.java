package com.sach.mark42.firestoredemo.java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sach.mark42.firestoredemo.R;

import java.util.HashMap;

public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        Button button = findViewById(R.id.button);

        final UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUsersFromDatabase(this);
        userViewModel.getUsersFromCache(this);
        userViewModel.queryUsersFromDatabase(this);
        userViewModel.queryUsersFromCache(this);

        //userViewModel.getUserFromDatabase(this);
        userViewModel.getUserFromCache(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pushUser(userViewModel);
                //updateUser(userViewModel);
                //updateUserFields(userViewModel);
                //updateChild(userViewModel);
                //deleteUserField(userViewModel);
            }
        });
    }

    private void deleteUserField(UserViewModel userViewModel) {
        userViewModel.deleteFromFirestore(this, "users",
                "vSXgBUPW5VvXe2gbqJes");
    }

    private void updateUserFields(UserViewModel userViewModel) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(FIRESTORE_KEY.USER.firstName, "asdf");
        updates.put(FIRESTORE_KEY.USER.email, 16);
        userViewModel.updateUserFieldsToFirestore(this, "users",
                "JTwG2mtMytEUe6ONyGll", updates);
    }

    private void updateChild(UserViewModel userViewModel) {
        userViewModel.updateUserFieldToFirestore(this, "users",
                "JTwG2mtMytEUe6ONyGll", FIRESTORE_KEY.USER.firstName,"Sach5");
    }

    private void updateUser(UserViewModel userViewModel) {
        User user = new User();
        user.setFirstName("Sachi6");
        user.setLastName("Sahu");
        user.setEmail("android@com6");
        userViewModel.updateUserToFirestore(this,"users",
                "JTwG2mtMytEUe6ONyGll", user);
    }

    private void pushUser(UserViewModel userViewModel) {
        User user = new User();
        user.setFirstName("Sach");
        user.setLastName("Sahu");
        user.setEmail("android@gmail.com5");
        userViewModel.pushUserToFirestore(this,"users",  user);
    }
}
