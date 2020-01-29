package com.firebase.mark42.firestoreassistant

class FirestoreResult<F>(
    val error: Error?,
    val value: F? = null,
    val errorMessage: String? = null
) {
    fun isSuccess(): Boolean = this.error == null

    companion object {
        fun <F> success(value: F?): FirestoreResult<F> = FirestoreResult(null, value)

        fun <F> failed(errorMessage: String) = FirestoreResult<F>(Error.FIRESTORE_REQUEST_FAILED, errorMessage = errorMessage)
    }

    enum class Error {
        FIRESTORE_REQUEST_FAILED,
        PERMISSION_DENIED,
        NOT_LOADED,
        NOT_EXIST
    }
}