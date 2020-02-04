package com.sach.mark42.firestoreassistant

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

    suspend fun getDocument(path: String, documentPath: String): FirestoreResult<DocumentSnapshot> = suspendCoroutine { continuation ->
        val source = Source.CACHE
        val dataRef = FirebaseFirestore.getInstance().collection(path).document(documentPath)
        dataRef.get(source).addOnSuccessListener { documentSnapshot ->
            continuation.resume(FirestoreResult.success(documentSnapshot))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    suspend fun updateField(path: String, documentPath: String, field: String, value: Any): FirestoreResult<Unit> = suspendCoroutine { continuation ->
        val dataRef = FirebaseFirestore.getInstance().collection(path).document(documentPath)
        dataRef.update(field, value).addOnSuccessListener {
            continuation.resume(FirestoreResult.success(Unit))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    suspend fun delete(path: String, documentPath: String): FirestoreResult<Unit> = suspendCoroutine { continuation ->
        val dataRef = FirebaseFirestore.getInstance().collection(path).document(documentPath)
        dataRef.delete().addOnSuccessListener {
            continuation.resume(FirestoreResult.success(Unit))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    suspend fun push(path: String, value: Any): FirestoreResult<String> = suspendCoroutine { continuation ->
        val dataRef = FirebaseFirestore.getInstance().collection(path)
        dataRef.add(value).addOnSuccessListener {
            continuation.resume(FirestoreResult.success(dataRef.id))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    suspend fun post(path: String, documentPath: String, value: Any): FirestoreResult<Unit> = suspendCoroutine { continuation ->
        val dataRef = FirebaseFirestore.getInstance().collection(path).document(documentPath)
        dataRef.set(value).addOnSuccessListener {
            continuation.resume(FirestoreResult.success(Unit))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    suspend fun updateFields(path: String, documentPath: String, updates: HashMap<String, Any?>): FirestoreResult<Unit> = suspendCoroutine { continuation ->
        val dataRef = FirebaseFirestore.getInstance().collection(path).document(documentPath)
        dataRef.update(updates).addOnSuccessListener {
            continuation.resume(FirestoreResult.success(Unit))
        }.addOnFailureListener {
            continuation.resume(FirestoreResult.failed(it.message ?: return@addOnFailureListener))
        }
    }

    fun fetchCollection(path: String? = null, query: Query? = null): LiveData<FirestoreResult<QuerySnapshot>> {
        return FirestoreCollectionLiveData(path, query)
    }

    fun fetchDocument(path: String, documentPath: String): LiveData<FirestoreResult<DocumentSnapshot>> {
        val ref = FirebaseFirestore.getInstance().collection(path).document(documentPath)
        return FirestoreDocumentLiveData(ref)
    }
}

class FirestoreDocumentLiveData(private val ref: DocumentReference) : LiveData<FirestoreResult<DocumentSnapshot>>() {

    private val listener = FirestoreEventListener()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration = ref.addSnapshotListener(listener)
        super.onActive()
    }

    override fun onInactive() {
        listenerRegistration?.remove()
        super.onInactive()
    }

    inner class FirestoreEventListener : EventListener<DocumentSnapshot> {
        override fun onEvent(p0: DocumentSnapshot?, p1: FirebaseFirestoreException?) {
            value = FirestoreResult.success(p0)
        }
    }
}

class FirestoreCollectionLiveData(private val path: String? = null,
                                  private val query: Query? = null) : LiveData<FirestoreResult<QuerySnapshot>>() {

    private val ref: Query
        get() {
            return if (path != null) {
                FirebaseFirestore.getInstance().collection(path)
            } else {
                query!!
            }
        }
    private val listener = FirestoreEventListener()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration = ref.addSnapshotListener(listener)
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