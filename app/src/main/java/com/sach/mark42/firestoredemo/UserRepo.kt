package com.sach.mark42.firestoredemo

import com.sach.mark42.firestoreassistant.FirestoreRepo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class UserRepo: FirestoreRepo<User>(){
    override fun convertDocumentSnapshot(documentSnapshot: DocumentSnapshot?): User? {
        return documentSnapshot?.toObject(User::class.java)
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
        private var instance : UserRepo? = null

        fun getInstance(): UserRepo {
            if (instance == null)
                instance = UserRepo()

            return instance!!
        }
        fun collectionPath() = "users"
    }
}