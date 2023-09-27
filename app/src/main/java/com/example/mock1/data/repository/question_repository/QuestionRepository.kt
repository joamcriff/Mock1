package com.example.mock1.data.repository.question_repository


import com.example.mock1.Model.QuestionModel
import com.example.mock1.viewmodel.question_view_model.QuestionViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class QuestionRepository(private val questionViewModel: QuestionViewModel): QuestionRepositoryService {
    var questionList: ArrayList<QuestionModel> = arrayListOf()
     override fun getQuestions() {
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
                    questionViewModel.onUserListReady(questionList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        )
    }
}