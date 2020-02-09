package com.sach.mark42.firestoredemo.java;

import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UsersViewModel extends ViewModel {
    private UsersRepo usersRepo = UsersRepo.getInstance();

    public void getUsersFromDatabase(AppCompatActivity activity) {
        usersRepo.getCollectionFromFirestore("users").observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter
                }
            }
        });
    }

    public void queryUsersFromDatabase(AppCompatActivity activity) {
        Query query = FirebaseFirestore.getInstance().collection("users").
                whereEqualTo("fName", "sachi").
                whereEqualTo("email", "android@email");
        usersRepo.getQueryFromFirestore(query).observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter
                }
            }
        });
    }

    public void getUsersFromCache(AppCompatActivity activity) {
        getUsersFromCache().observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter
                }
            }
        });
    }

    public void queryUsersFromCache(AppCompatActivity activity) {
        Query query = FirebaseFirestore.getInstance().collection("users").
                orderBy("fname").
                startAt("sach").
                endAt("sach" + "\uf8ff");
        queryUsersFromCache(query).observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter
                }
            }
        });
    }

    private static LiveData<List<User>> getUsersFromCache() {
        final UsersRepo usersRepo = UsersRepo.getInstance();
        final MutableLiveData<List<User>> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<List<User>>>() {

            @Override
            protected LiveData<List<User>> doInBackground(Void... voids) {
                try {
                    List<User> result = usersRepo.
                            getCollectionFromFirestoreCache("users").get();
                    liveData.postValue(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return liveData;
            }
        }.execute();

        return liveData;
    }

    private static LiveData<List<User>> queryUsersFromCache(final Query query) {
        final UsersRepo usersRepo = UsersRepo.getInstance();
        final MutableLiveData<List<User>> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<List<User>>>() {

            @Override
            protected LiveData<List<User>> doInBackground(Void... voids) {
                try {
                    List<User> result = usersRepo.getQueryFromFirestoreCache(query).get();
                    liveData.postValue(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return liveData;
            }
        }.execute();

        return liveData;
    }

}
