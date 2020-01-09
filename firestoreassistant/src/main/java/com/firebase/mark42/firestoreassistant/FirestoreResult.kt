package com.firebase.mark42.firestoreassistant

class FirestoreResult<D>(
    val error: Error?,
    val value: D? = null,
    val errorMessage: String? = null
) {
    fun isSuccess(): Boolean = this.error == null

    companion object {
        fun <D> success(value: D?): FirestoreResult<D> = FirestoreResult(null, value)

        fun <D> failed(errorMessage: String) = FirestoreResult<D>(Error.DATABASE_REQUEST_FAILED, errorMessage = errorMessage)
    }

    enum class Error {
        DATABASE_REQUEST_FAILED,
        PERMISSION_DENIED,
        NOT_LOADED,
        NOT_EXIST
    }
}