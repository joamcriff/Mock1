package com.example.mock1.data.repository.score_repository

import com.example.mock1.Model.ScoreRankModel
import com.example.mock1.viewmodel.rank_score_view_model.ScoreViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ScoreRepository(private val scoreViewModel: ScoreViewModel) : ScoreService {
    var scoreList: ArrayList<ScoreRankModel> = arrayListOf()
    override fun getScores() {
        val database = Firebase.database
        val myRef = database.getReference("questions")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                scoreList.clear()
                if (dataSnapshot.exists()) {
                    for (scoreSnapshot in dataSnapshot.children) {
                        val score = scoreSnapshot.getValue(ScoreRankModel::class.java)
                        scoreList.add(score!!)
                    }
                    scoreViewModel.onScoreListReady(scoreList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        )
    }
}