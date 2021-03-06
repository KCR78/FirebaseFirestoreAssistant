package com.sach.mark42.firestoreassistant

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.Query
import java.util.HashMap

internal interface Repo<T> {
    suspend fun getCollectionFromFirestoreCache(collectionPath: String): List<T>?
    fun getCollectionFromFirestore(collectionPath: String): LiveData<List<T>?>
    suspend fun getDocumentFromFirestoreCache(collectionPath: String, documentPath: String) : T?
    fun getDocumentFromFirestore(collectionPath: String, documentPath: String) : LiveData<T?>
    suspend fun pushToFirestore(collectionPath: String, t: Any): String?
    suspend fun updateDocumentToFirestore(collectionPath: String, documentPath: String, value: Any)
            : FirestoreResult<Unit>
    suspend fun updateChildToFirestore(collectionPath: String, documentPath: String,
                                       field: String, value: Any): FirestoreResult<Unit>
    suspend fun updateChildrenToFirestore(collectionPath: String, documentPath: String,
                                          updates: HashMap<String, Any?>): FirestoreResult<Unit>
    suspend fun deleteDocumentFromFirestore(collectionPath: String, documentPath: String): FirestoreResult<Unit>
    suspend fun getQueryFromFirestoreCache(query: Query): List<T>?
    fun getQueryFromFirestore(query: Query): LiveData<List<T>?>
}