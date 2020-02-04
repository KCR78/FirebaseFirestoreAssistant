package com.sach.mark42.firestoreassistant.java

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.arch.core.util.Function
import com.sach.mark42.firestoreassistant.FirestoreApi
import com.sach.mark42.firestoreassistant.FirestoreResult
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.HashMap
import java.util.concurrent.CompletableFuture

abstract class FirestoreRepo<T> : Repo<T> {

    private val api = FirestoreApi()

    override fun getCollectionFromFirestoreCache(path: String): CompletableFuture<T?> =
        GlobalScope.future {
            val result = api.getCollectionFromFirestoreCache(path)
            convertQuerySnapshot(result.value)
        }

    override fun getCollectionFromFirestore(path: String): LiveData<T?> {
        val result = api.getCollectionFromFirestore(path)
        val function = Function<FirestoreResult<QuerySnapshot>, T> {
            if (it.isSuccess()) {
                convertQuerySnapshot(it.value)
            } else {
                null
            }
        }
        return Transformations.map(result, function)
    }

    override fun getDocumentFromFirestoreCache(
        path: String,
        documentPath: String
    ): CompletableFuture<T?> =
        GlobalScope.future {
            val result = api.getDocumentFromFirestoreCache(path, documentPath)
            convertDocumentSnapshot(result.value)
        }

    override fun getDocumentFromFirestore(path: String, documentPath: String): LiveData<T?> {
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

    override fun pushToFirestore(path: String, t: Any): CompletableFuture<String?> =
        GlobalScope.future {
            api.pushToFirestore(path, t)
        }

    override fun updateChildToFirestore(
        path: String,
        documentPath: String,
        field: String,
        value: Any
    ): CompletableFuture<FirestoreResult<Unit>> =
        GlobalScope.future {
            api.updateChildToFirestore(path, documentPath, field, value)
        }

    override fun updateChildrenToFirestore(
        path: String,
        documentPath: String,
        updates: HashMap<String, Any?>
    ): CompletableFuture<FirestoreResult<Unit>> =
        GlobalScope.future {
            api.updateChildrenToFirestore(path, documentPath, updates)
        }

    override fun deleteFromFirestore(
        path: String,
        documentPath: String
    ): CompletableFuture<FirestoreResult<Unit>> =
        GlobalScope.future {
            api.deleteFromFirestore(path, documentPath)
        }

    override fun getQueryFromFirestoreCache(query: Query): CompletableFuture<T?> =
        GlobalScope.future {
            val result = api.getQueryFromFirestoreCache(query)
            convertQuerySnapshot(result.value)
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