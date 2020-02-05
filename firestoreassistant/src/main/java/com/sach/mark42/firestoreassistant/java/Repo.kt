package com.sach.mark42.firestoreassistant.java

import androidx.lifecycle.LiveData
import com.sach.mark42.firestoreassistant.FirestoreResult
import com.google.firebase.firestore.Query
import java.util.HashMap
import java.util.concurrent.CompletableFuture

internal interface Repo<T> {
    fun getCollectionFromFirestoreCache(collectionPath: String): CompletableFuture<T?>
    fun getCollectionFromFirestore(collectionPath: String): LiveData<T?>
    fun getDocumentFromFirestoreCache(collectionPath: String, documentPath: String) : CompletableFuture<T?>
    fun getDocumentFromFirestore(collectionPath: String, documentPath: String) : LiveData<T?>
    fun pushToFirestore(collectionPath: String, t: Any): CompletableFuture<String?>
    fun updateDocumentToFirestore(collectionPath: String, documentPath: String, value: Any)
            : CompletableFuture<FirestoreResult<Unit>>
    fun updateChildToFirestore(collectionPath: String, documentPath: String, field: String, value: Any)
            : CompletableFuture<FirestoreResult<Unit>>
    fun updateChildrenToFirestore(collectionPath: String, documentPath: String, updates: HashMap<String, Any?>)
            : CompletableFuture<FirestoreResult<Unit>>
    fun deleteFromFirestore(collectionPath: String, documentPath: String): CompletableFuture<FirestoreResult<Unit>>
    fun getQueryFromFirestoreCache(query: Query): CompletableFuture<T?>
    fun getQueryFromFirestore(query: Query): LiveData<T?>
}