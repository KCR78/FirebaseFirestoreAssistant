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

    override fun getCollectionFromFirestoreCache(collectionPath: String): CompletableFuture<T?> =
        GlobalScope.future {
            val result = api.getCollectionFromFirestoreCache(collectionPath)
            convertQuerySnapshot(result.value)
        }

    override fun getCollectionFromFirestore(collectionPath: String): LiveData<T?> {
        val result = api.getCollectionFromFirestore(collectionPath)
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
        collectionPath: String,
        documentPath: String
    ): CompletableFuture<T?> =
        GlobalScope.future {
            val result = api.getDocumentFromFirestoreCache(collectionPath, documentPath)
            convertDocumentSnapshot(result.value)
        }

    override fun getDocumentFromFirestore(collectionPath: String, documentPath: String): LiveData<T?> {
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

    override fun pushToFirestore(collectionPath: String, t: Any): CompletableFuture<String?> =
        GlobalScope.future {
            api.pushToFirestore(collectionPath, t)
        }

    override fun updateChildToFirestore(
        collectionPath: String,
        documentPath: String,
        field: String,
        value: Any
    ): CompletableFuture<FirestoreResult<Unit>> =
        GlobalScope.future {
            api.updateChildToFirestore(collectionPath, documentPath, field, value)
        }

    override fun updateDocumentToFirestore(
        collectionPath: String,
        documentPath: String,
        value: Any
    ): CompletableFuture<FirestoreResult<Unit>> =
        GlobalScope.future {
            api.postDocumentToFirestore(collectionPath, documentPath, value)
        }

    override fun updateChildrenToFirestore(
        collectionPath: String,
        documentPath: String,
        updates: HashMap<String, Any?>
    ): CompletableFuture<FirestoreResult<Unit>> =
        GlobalScope.future {
            api.updateChildrenToFirestore(collectionPath, documentPath, updates)
        }

    override fun deleteFromFirestore(
        collectionPath: String,
        documentPath: String
    ): CompletableFuture<FirestoreResult<Unit>> =
        GlobalScope.future {
            api.deleteFromFirestore(collectionPath, documentPath)
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

    abstract fun convertDocumentSnapshot(documentSnapshot: DocumentSnapshot?): T?

    abstract fun convertQuerySnapshot(querySnapshot: QuerySnapshot?): T?
}