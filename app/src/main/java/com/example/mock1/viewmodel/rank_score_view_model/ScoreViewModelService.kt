package com.example.mock1.viewmodel.rank_score_view_model

import androidx.lifecycle.LiveData
import com.example.mock1.Model.ScoreRankModel
import com.example.mock1.Model.UsersModel

interface ScoreViewModelService {
    fun onScoreListReady(scores: ArrayList<ScoreRankModel>)

    fun getScoreList(): LiveData<ArrayList<ScoreRankModel>>

    fun setName(name: String)

    fun setScore(correctAns: Int, wrongAns: Int)

    fun getResult(): LiveData<UsersModel>
}