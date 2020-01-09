package com.firebase.mark42.firestoreassistant

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*
import java.util.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirestoreHelper {

    suspend fun get(query: Query): FirestoreResult<QuerySnapshot> = suspendCoroutine { continuation ->
        val source = Source.CACHE
        query.get(source).addOnSuccessListener {querySnapshot ->
            continuation.resume(FirestoreResult.success(querySnapshot))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    suspend fun get(path: String): FirestoreResult<QuerySnapshot> {
        val dataRef = FirebaseFirestore.getInstance().collection(path)
        return get(dataRef)
    }

    suspend fun getDocument(path: String, document: String): FirestoreResult<DocumentSnapshot> = suspendCoroutine { continuation ->
        val dataRef = FirebaseFirestore.getInstance().collection(path).document(document)
        dataRef.get().addOnSuccessListener {documentSnapshot ->
            continuation.resume(FirestoreResult.success(documentSnapshot))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    suspend fun updateField(path: String, document: String, field: String, value: Any): FirestoreResult<Unit> = suspendCoroutine { continuation ->
        val dataRef = FirebaseFirestore.getInstance().collection(path).document(document)
        dataRef.update(field, value).addOnSuccessListener {
            continuation.resume(FirestoreResult.success(Unit))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    suspend fun delete(path: String, document: String): FirestoreResult<Unit> = suspendCoroutine { continuation ->
        val dataRef = FirebaseFirestore.getInstance().collection(path).document(document)
        dataRef.delete().addOnSuccessListener {
            continuation.resume(FirestoreResult.success(Unit))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    suspend fun push(path: String, value: Any): FirestoreResult<Unit> = suspendCoroutine { continuation ->
        val dataRef = FirebaseFirestore.getInstance().collection(path)
        dataRef.add(value).addOnSuccessListener {
            continuation.resume(FirestoreResult.success(Unit))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    suspend fun post(path: String, documentId: String, value: Any): FirestoreResult<Unit> = suspendCoroutine { continuation ->
        val dataRef = FirebaseFirestore.getInstance().collection(path).document(documentId)
        dataRef.set(value).addOnSuccessListener {
            continuation.resume(FirestoreResult.success(Unit))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    suspend fun updateFields(path: String, document: String, updates: HashMap<String, Any?>): FirestoreResult<Unit> = suspendCoroutine { continuation ->
        val dataRef = FirebaseFirestore.getInstance().collection(path).document(document)
        dataRef.update(updates).addOnSuccessListener {
            continuation.resume(FirestoreResult.success(Unit))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    fun stream(query: Query): LiveData<FirestoreResult<QuerySnapshot>> {
        return FirestoreQueryLiveData(query)

    }
}

class FirestoreQueryLiveData(private val query: Query) : LiveData<FirestoreResult<QuerySnapshot>>() {

    private val listener = FirestoreEventListener()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration = query.addSnapshotListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        listenerRegistration?.remove()
        super.onInactive()
    }

    inner class FirestoreEventListener : EventListener<QuerySnapshot> {
        override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
            value = FirestoreResult.success(p0)
        }
    }
}