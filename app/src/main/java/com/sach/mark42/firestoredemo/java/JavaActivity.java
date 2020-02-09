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

        final UsersViewModel usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        //usersViewModel.getUsersFromDatabase(this);
        //usersViewModel.getUsersFromCache(this);
        //usersViewModel.queryUsersFromDatabase(this);
        //usersViewModel.queryUsersFromCache(this);

        //userViewModel.getUserFromDatabase(this);
        //userViewModel.getUserFromCache(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pushUser(userViewModel);
                //updateChild(userViewModel);
                //updateUser(userViewModel);
                //deleteUserField(userViewModel);
            }
        });
    }

    private void deleteUserField(UserViewModel userViewModel) {
        userViewModel.deleteFromFirestore(this, "users", "");
    }

    private void updateUserFields(UserViewModel userViewModel) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(FIRESTORE_KEY.USER.firstName, "asdf");
        updates.put(FIRESTORE_KEY.USER.email, 16);
        userViewModel.updateUserFieldsToFirestore(this, "users",
                "", updates);
    }

    private void updateChild(UserViewModel userViewModel) {
        userViewModel.updateUserFieldToFirestore(this, "users",
                "", FIRESTORE_KEY.USER.firstName,"Sachin");
    }

    private void updateUser(UserViewModel userViewModel) {
        User user = new User();
        user.setFirstName("Sachi6");
        user.setLastName("Sahu");
        user.setEmail("android@com6");
        userViewModel.updateUserToFirestore(this,"users",
                "", user);
    }

    private void pushUser(UserViewModel userViewModel) {
        User user = new User();
        user.setFirstName("Sach");
        user.setLastName("Sahu");
        user.setEmail("android@gmail.com5");
        userViewModel.pushUserToFirestore(this,"users",  user);
    }
}
