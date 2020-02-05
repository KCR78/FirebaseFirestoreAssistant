package com.sach.mark42.firestoreassistant

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.HashMap

class FirestoreApi {

    suspend fun getCollectionFromFirestoreCache(collectionPath: String): FirestoreResult<QuerySnapshot> {
        val firestoreResult = FirestoreHelper().get(collectionPath)

        if (firestoreResult.value == null || !firestoreResult.isSuccess()) {
            return FirestoreResult.failed(FirestoreResult.Error.FIRESTORE_REQUEST_FAILED.toString())
        }

        return firestoreResult
    }

    fun getCollectionFromFirestore(collectionPath: String): LiveData<FirestoreResult<QuerySnapshot>> {
        return FirestoreHelper().fetchCollection(collectionPath)
    }

    suspend fun getDocumentFromFirestoreCache(collectionPath: String, documentPath: String) :
            FirestoreResult<DocumentSnapshot> {
        return FirestoreHelper().getDocument(collectionPath, documentPath)
    }

    fun getDocumentFromFirestore(collectionPath: String, documentPath: String) :
            LiveData<FirestoreResult<DocumentSnapshot>> {
        return FirestoreHelper().fetchDocument(collectionPath, documentPath)
    }

    suspend fun pushToFirestore(collectionPath: String, t: Any): String? {
        val key = FirestoreHelper().push(collectionPath, t)
        return key.value
    }

    suspend fun postDocumentToFirestore(
        collectionPath: String,
        documentPath: String,
        updates: Any
    ): FirestoreResult<Unit> {
        return FirestoreHelper().post(collectionPath, documentPath, updates)
    }

    suspend fun updateChildToFirestore(
        collectionPath: String,
        documentPath: String,
        field: String,
        value: Any
    ): FirestoreResult<Unit> {
        return FirestoreHelper().updateField(collectionPath, documentPath, field, value)
    }

    suspend fun updateChildrenToFirestore(
        collectionPath: String,
        documentPath: String,
        updates: HashMap<String, Any?>
    ): FirestoreResult<Unit> {
        return FirestoreHelper().updateFields(collectionPath, documentPath, updates)
    }

    suspend fun deleteFromFirestore(collectionPath: String, documentPath: String): FirestoreResult<Unit> {
        return FirestoreHelper().delete(collectionPath, documentPath)
    }

    suspend fun getQueryFromFirestoreCache(query: Query): FirestoreResult<QuerySnapshot> {
        val databaseResult = FirestoreHelper().get(query)

        if (databaseResult.value == null || !databaseResult.isSuccess()) {
            return FirestoreResult.failed(FirestoreResult.Error.FIRESTORE_REQUEST_FAILED.toString())
        }

        return databaseResult
    }

    fun getQueryFromFirestore(query: Query): LiveData<FirestoreResult<QuerySnapshot>> {
        return FirestoreHelper().fetchCollection(query = query)
    }
}