package com.example.mock1.data.repository

import com.example.mock1.Model.UsersModel
import com.example.mock1.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    val currentUser: FirebaseUser? = auth.currentUser

    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<UsersModel> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user != null) {
                Resource.Success(UsersModel().apply {
                    userId = user.uid
                    name = user.displayName
                })
            } else {
                Resource.Error(Exception("User is null"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}