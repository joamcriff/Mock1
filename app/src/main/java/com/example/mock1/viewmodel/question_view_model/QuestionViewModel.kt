package com.example.mock1.viewmodel.question_view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mock1.Model.QuestionModel
import com.example.mock1.data.repository.question_repository.QuestionRepository
import com.example.mock1.data.repository.question_repository.QuestionRepositoryService


class QuestionViewModel : ViewModel(), QuestionViewModelService {
    private val repository : QuestionRepositoryService = QuestionRepository(this)

    // Biến LiveData để giữ danh sách câu hỏi
    private val questions: LiveData<List<QuestionModel>> = MutableLiveData()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        repository.getQuestions()
    }

    override fun onUserListReady(question: ArrayList<QuestionModel>) {
        (questions as MutableLiveData).value = question
    }

    override fun getQuestionList(): LiveData<List<QuestionModel>> = questions

}