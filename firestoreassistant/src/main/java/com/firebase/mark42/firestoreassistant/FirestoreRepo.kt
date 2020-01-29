package com.firebase.mark42.firestoreassistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.arch.core.util.Function
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.HashMap

abstract class FirestoreRepo<T> : Repo<T>{

    private val api = FirestoreApi()

    override suspend fun getFromFirestoreCache(path: String): T? {
        val result = api.getFromFirestoreCache(path)
        return convertQuerySnapshot(result.value)
    }

    override fun getFromFirestore(path: String): LiveData<T?> {
        val result = api.getFromFirestore(path)
        val function = Function<FirestoreResult<QuerySnapshot>, T> {
            if (it.isSuccess()) {
                convertQuerySnapshot(it.value)
            } else {
                null
            }
        }
        return Transformations.map(result, function)
    }

    override suspend fun getDocumentFromFirestoreCache(
        path: String,
        documentPath: String
    ): T? {
        val result = api.getDocumentFromFirestoreCache(path, documentPath)
        return convertDocumentSnapshot(result.value)
    }

    override fun getDocumentFromFirestore(
        path: String,
        documentPath: String
    ): LiveData<T?> {
        val result = api.getDocumentFromFirestore(path, documentPath)
        val function = Function<FirestoreResult<DocumentSnapshot>, T> {
            if (it.isSuccess()) {
                convertDocumentSnapshot(it.value)
            } else {
                null
            }
        }
        return Transformations.map(result, function)
    }

    override suspend fun pushToFirestore(path: String, t: Any): String? {
        return api.pushToFirestore(path, t)
    }

    override suspend fun updateChildToFirestore(
        path: String,
        documentPath: String,
        field: String,
        value: Any
    ): FirestoreResult<Unit> {
        return api.updateChildToFirestore(path, documentPath, field, value)
    }

    override suspend fun updateChildrenToFirestore(
        path: String,
        documentPath: String,
        updates: HashMap<String, Any?>
    ): FirestoreResult<Unit> {
        return api.updateChildrenToFirestore(path, documentPath, updates)
    }

    override suspend fun deleteFromFirestore(
        path: String,
        documentPath: String
    ): FirestoreResult<Unit> {
        return api.deleteFromFirestore(path, documentPath)
    }

    override suspend fun getQueryFromFirestoreCache(query: Query): T? {
        val result = api.getQueryFromFirestoreCache(query)
        return convertQuerySnapshot(result.value)
    }

    override fun getQueryFromFirestore(query: Query): LiveData<T?> {
        val result = api.getQueryFromFirestore(query)
        val function = Function<FirestoreResult<QuerySnapshot>, T> {
            if (it.isSuccess()) {
                convertQuerySnapshot(it.value)
            } else {
                null
            }
        }
        return Transformations.map(result, function)
    }

    abstract fun convertDocumentSnapshot(value: DocumentSnapshot?): T?

    abstract fun convertQuerySnapshot(value: QuerySnapshot?): T?

}