package com.firebase.mark42.firestoredemo

import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

@Keep
data class User(
    @Exclude @set:Exclude @get:Exclude
    var userPushId: String? = null,

    @get:PropertyName(FIRESTORE_KEY.USER.firstName)
    @set:PropertyName(FIRESTORE_KEY.USER.firstName)
    var firstName: String = "",

    @get:PropertyName(FIRESTORE_KEY.USER.lastName)
    @set:PropertyName(FIRESTORE_KEY.USER.lastName)
    var lastName: String = "",

    @get:PropertyName(FIRESTORE_KEY.USER.email)
    @set:PropertyName(FIRESTORE_KEY.USER.email)
    var email: String = ""
)