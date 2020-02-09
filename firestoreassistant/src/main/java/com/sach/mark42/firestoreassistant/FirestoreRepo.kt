package com.sach.mark42.firestoreassistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.arch.core.util.Function
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.HashMap

abstract class FirestoreRepo<T> : Repo<T>{

    private val api = FirestoreApi()

    override suspend fun getCollectionFromFirestoreCache(collectionPath: String): List<T>? {
        val result = api.getCollectionFromFirestoreCache(collectionPath)
        return convertQuerySnapshot(result.value)
    }

    override fun getCollectionFromFirestore(collectionPath: String): LiveData<List<T>?> {
        val result = api.getCollectionFromFirestore(collectionPath)
        val function = Function<FirestoreResult<QuerySnapshot>, List<T>> {
            if (it.isSuccess()) {
                convertQuerySnapshot(it.value)
            } else {
                null
            }
        }
        return Transformations.map(result, function)
    }

    override suspend fun getDocumentFromFirestoreCache(
        collectionPath: String,
        documentPath: String
    ): T? {
        val result = api.getDocumentFromFirestoreCache(collectionPath, documentPath)
        return convertDocumentSnapshot(result.value)
    }

    override fun getDocumentFromFirestore(
        collectionPath: String,
        documentPath: String
    ): LiveData<T?> {
        val result = api.getDocumentFromFirestore(collectionPath, documentPath)
        val function = Function<FirestoreResult<DocumentSnapshot>, T> {
            if (it.isSuccess()) {
                convertDocumentSnapshot(it.value)
            } else {
                null
            }
        }
        return Transformations.map(result, function)
    }

    override suspend fun pushToFirestore(collectionPath: String, t: Any): String? {
        return api.pushToFirestore(collectionPath, t)
    }

    override suspend fun updateDocumentToFirestore(
        collectionPath: String,
        documentPath: String,
        value: Any
    ): FirestoreResult<Unit> {
        return api.postDocumentToFirestore(collectionPath, documentPath, value)
    }

    override suspend fun updateChildToFirestore(
        collectionPath: String,
        documentPath: String,
        field: String,
        value: Any
    ): FirestoreResult<Unit> {
        return api.updateChildToFirestore(collectionPath, documentPath, field, value)
    }

    override suspend fun updateChildrenToFirestore(
        collectionPath: String,
        documentPath: String,
        updates: HashMap<String, Any?>
    ): FirestoreResult<Unit> {
        return api.updateChildrenToFirestore(collectionPath, documentPath, updates)
    }

    override suspend fun deleteDocumentFromFirestore(
        collectionPath: String,
        documentPath: String
    ): FirestoreResult<Unit> {
        return api.deleteDocumentFromFirestore(collectionPath, documentPath)
    }

    override suspend fun getQueryFromFirestoreCache(query: Query): List<T>? {
        val result = api.getQueryFromFirestoreCache(query)
        return convertQuerySnapshot(result.value)
    }

    override fun getQueryFromFirestore(query: Query): LiveData<List<T>?> {
        val result = api.getQueryFromFirestore(query)
        val function = Function<FirestoreResult<QuerySnapshot>, List<T>> {
            if (it.isSuccess()) {
                convertQuerySnapshot(it.value)
            } else {
                null
            }
        }
        return Transformations.map(result, function)
    }

    abstract fun convertDocumentSnapshot(documentSnapshot: DocumentSnapshot?): T?

    abstract fun convertQuerySnapshot(querySnapshot: QuerySnapshot?): List<T>?

}