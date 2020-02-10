package com.sach.mark42.firestoredemo

import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.HashMap

class UserViewModel: ViewModel() {
    private val userRepo by lazy {
        UserRepo.getInstance()
    }

    fun getUserFromFirestore(activity: AppCompatActivity) {
        val fName = activity.findViewById<TextView>(R.id.firstName)
        val lName = activity.findViewById<TextView>(R.id.lastName)
        val email = activity.findViewById<TextView>(R.id.email)
        userRepo.getDocumentFromFirestore(UserRepo.collectionPath(), "qUiuSLFQXTqYbEf6mxBV").observe(activity, Observer { user ->
            fName.text = user?.firstName
            lName.text = user?.lastName
            email.text = user?.email
        })
    }

    fun getUserFromCache(activity: AppCompatActivity) {
        viewModelScope.launch {
            val fName = activity.findViewById<TextView>(R.id.firstName)
            val lName = activity.findViewById<TextView>(R.id.lastName)
            val email = activity.findViewById<TextView>(R.id.email)
            val user = userRepo.getDocumentFromFirestoreCache(UserRepo.collectionPath(), "qUiuSLFQXTqYbEf6mxBV")
            fName.text = user?.firstName
            lName.text = user?.lastName
            email.text = user?.email
        }
    }

    fun pushUserToFirestore(user: User) {
        viewModelScope.launch {
            //it'll return the firestore push id key
            val key = userRepo.pushToFirestore(UserRepo.collectionPath(), user)
            user.userPushId = key
            // show confirmation message to user
        }
    }

    fun updateUserToFirestore(user: User) {
        viewModelScope.launch {
            val result = userRepo.updateDocumentToFirestore(
                UserRepo.collectionPath(),"mjPWSp0CDWlGetcgsotL", user)
            if (result.isSuccess()) {
                //Display success message
            } else {
                //Display error message
                result.errorMessage
            }
        }
    }

    fun updateUserFieldToFirestore() {
        viewModelScope.launch {
            val result = userRepo.updateChildToFirestore(UserRepo.collectionPath(),
                "mjPWSp0CDWlGetcgsotL", FIRESTORE_KEY.USER.lastName, "Sahu")
            if (result.isSuccess()) {
                //Display success message
            } else {
                //Display error message
                result.errorMessage
            }
        }
    }

    fun updateUserFieldsToFirestore(updates: HashMap<String, Any?>) {
        viewModelScope.launch {
            //if path is null then it'll create a new node
            val result = userRepo.updateChildrenToFirestore(UserRepo.collectionPath(),
                "mjPWSp0CDWlGetcgsotL", updates)
            if (result.isSuccess()) {
                //Display success message
            } else {
                //Display error message
                result.errorMessage
            }
        }
    }

    fun deleteDocumentFromFirestore() {
        viewModelScope.launch {
            val result = userRepo.deleteDocumentFromFirestore(UserRepo.collectionPath(),
                "F7koA7eRAsUjWtAzQrL5")
            if (result.isSuccess()) {
                //Display success message
            } else {
                //Display error message
                result.errorMessage
            }
        }
    }

    fun getUsersFromFirestore(activity: AppCompatActivity) {
        userRepo.getCollectionFromFirestore(UserRepo.collectionPath()).observe(activity, Observer { users ->
            users?.forEach {
                // update your adapter in activity
            }
        })
    }

    fun getUsersFromCache() {
        viewModelScope.launch {
            val users = userRepo.getCollectionFromFirestoreCache(UserRepo.collectionPath())
            users?.forEach {
                // update your adapter in activity
            }
        }
    }

    fun queryUsersFromFirestore(activity: AppCompatActivity) {
        val query = FirebaseFirestore.getInstance().collection("users").
            orderBy(FIRESTORE_KEY.USER.firstName).
            whereEqualTo(FIRESTORE_KEY.USER.lastName, "Sahu").
            limitToLast(2)
        userRepo.getQueryFromFirestore(query).observe(activity, Observer { users ->
            users?.forEach {
                // update your adapter in activity
            }
        })
    }

    fun queryUsersFromCache() {
        val query = FirebaseFirestore.getInstance().collection("users").
            whereEqualTo(FIRESTORE_KEY.USER.firstName, "Sachi").
            whereEqualTo(FIRESTORE_KEY.USER.lastName, "Sahu")
        viewModelScope.launch {
            val users = userRepo.getQueryFromFirestoreCache(query)
            users?.forEach {
                // update your adapter in activity
            }
        }
    }
}