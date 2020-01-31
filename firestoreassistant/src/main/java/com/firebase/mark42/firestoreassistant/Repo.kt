package com.firebase.mark42.firestoreassistant

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import java.util.HashMap

internal interface Repo<T> {
    suspend fun getCollectionFromFirestoreCache(path: String): T?
    fun getCollectionFromFirestore(path: String): LiveData<T?>
    suspend fun getDocumentFromFirestoreCache(path: String, documentPath: String) : T?
    fun getDocumentFromFirestore(path: String, documentPath: String) : LiveData<T?>
    suspend fun pushToFirestore(path: String, t: Any): String?
    suspend fun updateChildToFirestore(path: String, documentPath: String,
                                       field: String, value: Any): FirestoreResult<Unit>
    suspend fun updateChildrenToFirestore(path: String, documentPath: String,
                                          updates: HashMap<String, Any?>): FirestoreResult<Unit>
    suspend fun deleteFromFirestore(path: String, documentPath: String): FirestoreResult<Unit>
    suspend fun getQueryFromFirestoreCache(query: Query): T?
    fun getQueryFromFirestore(query: Query): LiveData<T?>
}