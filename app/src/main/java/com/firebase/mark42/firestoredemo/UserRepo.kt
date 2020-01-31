package com.firebase.mark42.firestoredemo

import android.content.Context
import com.firebase.mark42.firestoreassistant.FirestoreRepo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class UserRepo: FirestoreRepo<User>(){
    override fun convertDocumentSnapshot(value: DocumentSnapshot?): User? {
        return value?.toObject(User::class.java)
    }

    override fun convertQuerySnapshot(value: QuerySnapshot?): User? {
        return null
    }

    companion object {
        private var instance : UserRepo? = null

        fun getInstance(): UserRepo {
            if (instance == null)
                instance = UserRepo()

            return instance!!
        }
        fun collectionPath() = "users"
    }

}