package com.example.mock1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mock1.Model.QuestionModel
import com.example.mock1.repository.QuestionRepository


class QuestionViewModel : ViewModel(), QuestionRepository.UserCallback {
    private val repository = QuestionRepository()

    // Biến LiveData để giữ danh sách câu hỏi
    val questions: LiveData<List<QuestionModel>> = MutableLiveData()

    init {
        // Gọi hàm để tải danh sách câu hỏi từ repository
        loadQuestions()
    }

    // Hàm này được gọi từ constructor để tải danh sách câu hỏi
    private fun loadQuestions() {
        repository.getQuestions(this)
    }
    override fun onUserListReady(userList: ArrayList<QuestionModel>) {
        (questions as MutableLiveData).value = userList

    }

    override fun onDataError(error: String) {

    }

}