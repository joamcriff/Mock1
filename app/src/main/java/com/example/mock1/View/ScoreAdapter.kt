package com.example.mock1.View

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mock1.Model.ScoreRankModel
import com.example.mock1.R

class ScoreAdapter(val activity: Activity, val scoreList: List<ScoreRankModel>): ArrayAdapter<ScoreRankModel>(activity, R.layout.list_item_score) {
    override fun getCount(): Int {
        return scoreList.size
    }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val rowView = activity.layoutInflater.inflate(R.layout.list_item_score, parent, false)

            val rank = rowView.findViewById<TextView>(R.id.rankT)
            val name = rowView.findViewById<TextView>(R.id.emailT)
            val correctAns = rowView.findViewById<TextView>(R.id.correct_ans)
            val wrongAns = rowView.findViewById<TextView>(R.id.wrong_ans)

            rank.text = (position + 1).toString()
            name.text = scoreList[position].userResult?.name
            correctAns.text = "Correct: ${scoreList[position].userResult?.correctAns}"
            wrongAns.text = "Wrong: ${scoreList[position].userResult?.wrongAns}"

            return rowView
        }
}

