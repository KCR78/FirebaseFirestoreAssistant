package com.sach.mark42.firestoredemo

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class UsersViewModel: ViewModel() {

    private val usersRepo by lazy {
        UsersRepo.getInstance()
    }

    fun getUsersFromFirestore(activity: AppCompatActivity) {
        usersRepo.getCollectionFromFirestore(UsersRepo.collectionPath()).observe(activity, Observer { users ->
            users?.forEach {
                // update your adapter
            }
        })
    }

    fun getUsersFromCache() {
        viewModelScope.launch {
            val users = usersRepo.getCollectionFromFirestoreCache(UsersRepo.collectionPath())
            users?.forEach {
                // update your adapter
            }
        }
    }

    fun queryUsersFromFirestore(activity: AppCompatActivity) {
        val query = FirebaseFirestore.getInstance().collection("users").
            whereEqualTo("fName", "sachi").
            whereEqualTo("email", "android@email")
        usersRepo.getQueryFromFirestore(query).observe(activity, Observer { users ->
            users?.forEach {
                // update your adapter
            }
        })
    }

    fun queryUsersFromCache() {
        val query = FirebaseFirestore.getInstance().collection("users").
            orderBy("fname").
            startAt("sach").
            endAt("sach" + "\uf8ff")
        viewModelScope.launch {
            val users = usersRepo.getQueryFromFirestoreCache(query)
            users?.forEach {
                // update your adapter
            }
        }
    }
}