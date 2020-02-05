package com.sach.mark42.firestoredemo

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.sach.mark42.firestoreassistant.FirestoreRepo

class UsersRepo: FirestoreRepo<List<User>>() {
    override fun convertDocumentSnapshot(documentSnapshot: DocumentSnapshot?): List<User>? {
        return null
    }

    override fun convertQuerySnapshot(querySnapshot: QuerySnapshot?): List<User>? {
        val documents = querySnapshot?.documents?.mapNotNull {
            try {
                it.toObject(User::class.java)
            } catch (e: Exception) {
                null
            }
        }
        return documents
    }

    companion object {
        private var instance : UsersRepo? = null

        fun getInstance(): UsersRepo {
            if (instance == null)
                instance = UsersRepo()

            return instance!!
        }
        fun collectionPath() = "users"
    }
}