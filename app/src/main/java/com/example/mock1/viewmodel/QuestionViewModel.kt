package com.example.mock1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mock1.Model.QuestionModel
import com.example.mock1.data.repository.QuestionRepository


class QuestionViewModel : ViewModel() {
    private val repository = QuestionRepository(this)

    // Biến LiveData để giữ danh sách câu hỏi
    val questions: LiveData<List<QuestionModel>> = MutableLiveData()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        repository.getQuestions()
    }

    fun onUserListReady(userList: ArrayList<QuestionModel>) {
        (questions as MutableLiveData).value = userList

    }

    fun getQuestionList(): LiveData<List<QuestionModel>> = questions

}