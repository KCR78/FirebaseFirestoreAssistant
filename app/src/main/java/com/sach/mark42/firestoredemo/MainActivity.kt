package com.sach.mark42.firestoredemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.getUsersFromCache()
        userViewModel.getUsersFromFirestore(this)
        userViewModel.queryUsersFromCache()
        userViewModel.queryUsersFromFirestore(this)

        userViewModel.getUserFromFirestore(this)
        //userViewModel.getUserFromCache(this)

        button.setOnClickListener {
            //pushUser(userViewModel)
            //updateUserFields(userViewModel)
            //updateChild(userViewModel)
            //updateUser(userViewModel)
            //deleteUserField(userViewModel)
        }
    }

    private fun deleteUserField(viewModel: UserViewModel) {
        viewModel.deleteDocumentFromFirestore()
    }

    private fun updateUserFields(viewModel: UserViewModel) {
        val updates = HashMap<String, Any?>()
        updates[FIRESTORE_KEY.USER.firstName] = "abcd"
        updates[FIRESTORE_KEY.USER.lastName] = "mn"
        viewModel.updateUserFieldsToFirestore(updates)
    }

    private fun updateChild(viewModel: UserViewModel) {
        viewModel.updateUserFieldToFirestore()
    }

    private fun updateUser(viewModel: UserViewModel) {
        val user = User()
        user.firstName = "Sachi5"
        user.lastName = "Sahu"
        user.email = "android@com"
        viewModel.updateUserToFirestore(user)
    }

    private fun pushUser(viewModel: UserViewModel) {
        val user = User()
        user.firstName = "Sachidananda2"
        user.lastName = "Sahu"
        user.email = "android@gmail.com2"
        viewModel.pushUserToFirestore(user)
    }
}
