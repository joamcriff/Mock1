package com.example.mock1.viewmodel.rank_score_view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mock1.Model.QuestionModel
import com.example.mock1.Model.ScoreRankModel
import com.example.mock1.Model.UsersModel
import com.example.mock1.data.repository.score_repository.ScoreRepository
import com.example.mock1.data.repository.score_repository.ScoreService

class ScoreViewModel : ViewModel(), ScoreViewModelService {

    private val scoreList: LiveData<ArrayList<ScoreRankModel>> = MutableLiveData()
    private val scoreRepository: ScoreService = ScoreRepository(this)

    private val name = MutableLiveData<String>()
    private val userResult = MutableLiveData<UsersModel>()

    init {
        loadScore()
    }

    private fun loadScore() {
        scoreRepository.getScores()
    }

    override fun onScoreListReady(scores: ArrayList<ScoreRankModel>) {
        (scoreList as MutableLiveData).value = scores
    }

    override fun getScoreList(): LiveData<ArrayList<ScoreRankModel>> = scoreList

    override fun setName(name: String) {
        this.name.value = name
    }

    override fun setScore(correctAns: Int, wrongAns: Int) {
        val result = UsersModel(name.value, correctAns, wrongAns)

        this.userResult.value = result
    }

    override fun getResult(): LiveData<UsersModel> = userResult

}