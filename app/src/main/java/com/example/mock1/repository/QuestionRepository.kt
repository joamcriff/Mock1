package com.example.mock1.repository

import androidx.lifecycle.MutableLiveData
import com.example.mock1.Model.QuestionModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

class QuestionRepository {
    var questionList: ArrayList<QuestionModel> = arrayListOf()
    fun getQuestions(callback: UserCallback) {
        val database = Firebase.database
        val myRef = database.getReference("questions")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                questionList.clear()
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(QuestionModel::class.java)
                        questionList.add(user!!)
                    }
                    callback.onUserListReady(questionList)

                } else {
                    callback.onDataError("No users found.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onDataError("Failed to read data: ${databaseError.toException()}")
            }
        })
    }
    interface UserCallback {
        fun onUserListReady(userList: ArrayList<QuestionModel>)
        fun onDataError(error: String)
    }
}