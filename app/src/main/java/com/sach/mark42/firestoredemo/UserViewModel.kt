package com.sach.mark42.firestoredemo

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            val user = userRepo.getDocumentFromFirestoreCache(UserRepo.collectionPath(), "")
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
                UserRepo.collectionPath(),"", user)
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
                "", FIRESTORE_KEY.USER.firstName, "Sachin")
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
                "", updates)
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
                "")
            if (result.isSuccess()) {
                //Display success message
            } else {
                //Display error message
                result.errorMessage
            }
        }
    }
}