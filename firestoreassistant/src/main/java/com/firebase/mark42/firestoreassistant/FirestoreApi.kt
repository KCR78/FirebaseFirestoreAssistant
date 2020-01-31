package com.firebase.mark42.firestoreassistant

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.HashMap

class FirestoreApi {

    suspend fun getCollectionFromFirestoreCache(path: String): FirestoreResult<QuerySnapshot> {
        val firestoreResult = FirestoreHelper().get(path)

        if (firestoreResult.value == null || !firestoreResult.isSuccess()) {
            return FirestoreResult.failed(FirestoreResult.Error.FIRESTORE_REQUEST_FAILED.toString())
        }

        return firestoreResult
    }

    fun getCollectionFromFirestore(path: String): LiveData<FirestoreResult<QuerySnapshot>> {
        return FirestoreHelper().fetchCollection(path)
    }

    suspend fun getDocumentFromFirestoreCache(path: String, documentPath: String) :
            FirestoreResult<DocumentSnapshot> {
        return FirestoreHelper().getDocument(path, documentPath)
    }

    fun getDocumentFromFirestore(path: String, documentPath: String) :
            LiveData<FirestoreResult<DocumentSnapshot>> {
        return FirestoreHelper().fetchDocument(path, documentPath)
    }

    suspend fun pushToFirestore(path: String, t: Any): String? {
        val key = FirestoreHelper().push(path, t)
        return key.value
    }

    suspend fun updateChildToFirestore(
        path: String,
        documentPath: String,
        field: String,
        value: Any
    ): FirestoreResult<Unit> {
        return FirestoreHelper().updateField(path, documentPath, field, value)
    }

    suspend fun updateChildrenToFirestore(
        path: String,
        documentPath: String,
        updates: HashMap<String, Any?>
    ): FirestoreResult<Unit> {
        return FirestoreHelper().updateFields(path, documentPath, updates)
    }

    suspend fun deleteFromFirestore(path: String, documentPath: String): FirestoreResult<Unit> {
        return FirestoreHelper().delete(path, documentPath)
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