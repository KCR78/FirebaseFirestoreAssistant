package com.sach.mark42.firestoredemo.java;

import android.os.AsyncTask;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.sach.mark42.firestoreassistant.FirestoreResult;
import com.sach.mark42.firestoredemo.R;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class UserViewModel extends ViewModel {
    private UserRepo userRepo = UserRepo.getInstance();

    public void getUserFromDatabase(AppCompatActivity activity) {
        final TextView fName = activity.findViewById(R.id.firstName);
        final TextView lName = activity.findViewById(R.id.lastName);
        final TextView email = activity.findViewById(R.id.email);

        userRepo.getDocumentFromFirestore("users", "").observe(activity, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                fName.setText(user.getFirstName());
                lName.setText(user.getLastName());
                email.setText(user.getEmail());
            }
        });
    }

    public void getUserFromCache(final AppCompatActivity activity) {
        getUserFromCache("users", "").observe(activity, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                TextView fName = activity.findViewById(R.id.firstName);
                TextView lName = activity.findViewById(R.id.lastName);
                TextView email = activity.findViewById(R.id.email);

                fName.setText(user.getFirstName());
                lName.setText(user.getLastName());
                email.setText(user.getEmail());
            }
        });
    }

    public void pushUserToFirestore(AppCompatActivity activity, String collectionPath, User user) {
        pushUserToFirestore(collectionPath, user).observe(activity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //it return the firebase push id key
                String key = s;
                // show confirmation message to user
            }
        });
    }

    public void updateUserFieldToFirestore(AppCompatActivity activity,
                                          String collectionPath,
                                          String documentPath,
                                          String userField,
                                          String value) {
        updateUserFieldToFirestore(collectionPath, documentPath, userField, value).observe(activity, new Observer<FirestoreResult>() {
            @Override
            public void onChanged(FirestoreResult firestoreResult) {
                if (firestoreResult.isSuccess()) {
                    //Display success message
                } else {
                    //Display error message
                    firestoreResult.getErrorMessage();
                }
            }
        });
    }

    public void updateUserFieldsToFirestore(AppCompatActivity activity,
                                            String collectionPath,
                                            String documentPath,
                                            HashMap<String, Object> updates) {
        updateUserFieldsToFirestore(collectionPath, documentPath, updates).observe(activity, new Observer<FirestoreResult>() {
            @Override
            public void onChanged(FirestoreResult firestoreResult) {
                if (firestoreResult.isSuccess()) {
                    //Display success message
                } else {
                    //Display error message
                    firestoreResult.getErrorMessage();
                }
            }
        });
    }

    public void updateUserToFirestore(AppCompatActivity activity,
                                            String collectionPath,
                                            String documentPath,
                                            User user) {
        updateUserToFirestore(collectionPath, documentPath, user).observe(activity, new Observer<FirestoreResult>() {
            @Override
            public void onChanged(FirestoreResult firestoreResult) {
                if (firestoreResult.isSuccess()) {
                    //Display success message
                } else {
                    //Display error message
                    firestoreResult.getErrorMessage();
                }
            }
        });
    }

    public void deleteFromFirestore(AppCompatActivity activity, String collectionPath, String documentPath) {
        deleteFromFirestore(collectionPath, documentPath).observe(activity, new Observer<FirestoreResult>() {
            @Override
            public void onChanged(FirestoreResult firestoreResult) {
                if (firestoreResult.isSuccess()) {
                    //Display success message
                } else {
                    //Display error message
                    firestoreResult.getErrorMessage();
                }
            }
        });
    }

    private static LiveData<User> getUserFromCache(final String collectionPath, final String documentPath) {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<User> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<User>>() {

            @Override
            protected LiveData<User> doInBackground(Void... voids) {
                try {
                    User result = userRepo.getDocumentFromFirestoreCache(collectionPath, documentPath).get();
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

    private static LiveData<String> pushUserToFirestore(final String collectionPath, final User user) {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<String>>() {
            @Override
            protected LiveData<String> doInBackground(Void... voids) {
                try {
                    String key = userRepo.pushToFirestore(collectionPath, user).get();
                    liveData.postValue(key);
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

    private static LiveData<FirestoreResult> updateUserFieldToFirestore
            (final String collectionPath,
             final String documentPath,
             final String field,
             final String value) {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<FirestoreResult> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<FirestoreResult>>() {

            @Override
            protected LiveData<FirestoreResult> doInBackground(Void... voids) {
                try {
                    FirestoreResult result = userRepo.
                            updateChildToFirestore(collectionPath, documentPath, field, value).get();
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

    private static LiveData<FirestoreResult> updateUserFieldsToFirestore
            (final String collectionPath,
             final String documentPath,
             final HashMap<String, Object> updates) {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<FirestoreResult> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<FirestoreResult>>() {

            @Override
            protected LiveData<FirestoreResult> doInBackground(Void... voids) {
                try {
                    FirestoreResult result = userRepo.
                            updateChildrenToFirestore(collectionPath, documentPath, updates).get();
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

    private static LiveData<FirestoreResult> updateUserToFirestore
            (final String collectionPath,
             final String documentPath,
             final User user) {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<FirestoreResult> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<FirestoreResult>>() {

            @Override
            protected LiveData<FirestoreResult> doInBackground(Void... voids) {
                try {
                    FirestoreResult result = userRepo.
                            updateDocumentToFirestore(collectionPath, documentPath, user).get();
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

    private static LiveData<FirestoreResult> deleteFromFirestore(final String collectionPath,
                                                                final String documentPath) {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<FirestoreResult> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<FirestoreResult>>() {

            @Override
            protected LiveData<FirestoreResult> doInBackground(Void... voids) {
                try {
                    FirestoreResult result = userRepo.
                            deleteFromFirestore(collectionPath, documentPath).get();
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
