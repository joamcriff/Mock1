package com.example.mock1.viewmodel.question_view_model

import androidx.lifecycle.LiveData
import com.example.mock1.Model.QuestionModel


interface QuestionViewModelService {
    fun onUserListReady(question: ArrayList<QuestionModel>)

    fun getQuestionList(): LiveData<List<QuestionModel>>
}