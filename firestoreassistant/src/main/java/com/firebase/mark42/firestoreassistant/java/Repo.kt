package com.firebase.mark42.firestoreassistant.java

import androidx.lifecycle.LiveData
import com.firebase.mark42.firestoreassistant.FirestoreResult
import com.google.firebase.firestore.Query
import java.util.HashMap
import java.util.concurrent.CompletableFuture

internal interface Repo<T> {
    fun getCollectionFromFirestoreCache(path: String): CompletableFuture<T?>
    fun getCollectionFromFirestore(path: String): LiveData<T?>
    fun getDocumentFromFirestoreCache(path: String, documentPath: String) : CompletableFuture<T?>
    fun getDocumentFromFirestore(path: String, documentPath: String) : LiveData<T?>
    fun pushToFirestore(path: String, t: Any): CompletableFuture<String?>
    fun updateChildToFirestore(path: String, documentPath: String, field: String, value: Any)
            : CompletableFuture<FirestoreResult<Unit>>
    fun updateChildrenToFirestore(path: String, documentPath: String, updates: HashMap<String, Any?>)
            : CompletableFuture<FirestoreResult<Unit>>
    fun deleteFromFirestore(path: String, documentPath: String): CompletableFuture<FirestoreResult<Unit>>
    fun getQueryFromFirestoreCache(query: Query): CompletableFuture<T?>
    fun getQueryFromFirestore(query: Query): LiveData<T?>
}