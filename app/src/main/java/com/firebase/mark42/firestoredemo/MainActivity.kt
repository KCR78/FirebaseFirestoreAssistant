package com.firebase.mark42.firestoredemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.getUserFromFirestore(this)

        button.setOnClickListener {
            //pushUser(userViewModel)
        }
    }

    private fun pushUser(viewModel: UserViewModel) {
        val user = User()
        user.firstName = "Sachidananda"
        user.lastName = "Sahu"
        user.email = "android@gmail.com"
        viewModel.pushUserToFirestore(user)
    }
}
