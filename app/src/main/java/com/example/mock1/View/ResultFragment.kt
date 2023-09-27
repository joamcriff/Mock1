package com.example.mock1.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mock1.Model.ScoreRankModel
import com.example.mock1.R
import com.example.mock1.databinding.FragmentResultBinding
import com.example.mock1.viewmodel.rank_score_view_model.ScoreViewModel
import com.example.mock1.viewmodel.rank_score_view_model.ScoreViewModelService
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var binding: FragmentResultBinding
    private lateinit var resultScore: ScoreViewModelService
    private lateinit var dataBase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        resultScore = ViewModelProvider(requireActivity())[ScoreViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.playAgain.setOnClickListener {
            playClick()
        }

        binding.exit.setOnClickListener {
            exitClick()
            putResult()
        }

        binding.rank.setOnClickListener {
            rankClick()
            putResult()
        }

        receiveDataFromIntent()

        return view
    }

    private fun rankClick() {
        val fragment = RankFragment()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.startFragment, fragment)
        fragmentTransaction?.commit()
    }

    private fun playClick() {
        val fragment = StartFragment()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.startFragment, fragment)
        fragmentTransaction?.commit()
    }

    private fun exitClick() {
        val fragment = LoginFragment()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.startFragment, fragment)
        fragmentTransaction?.commit()
    }

    private fun receiveDataFromIntent() {
        resultScore.getResult().observe(viewLifecycleOwner, Observer {
            binding.correct1.text = "${it.correctAns}"
            binding.wrong1.text = "${it.wrongAns}"
        })
    }


    private fun putResult() {
        dataBase = FirebaseDatabase.getInstance().getReference("Score")
        val nameID = dataBase.push().key!!
        val userResult = resultScore.getResult().value

        dataBase.child(nameID).setValue(ScoreRankModel(nameID, userResult))
            .addOnCompleteListener {
                Toast.makeText(activity, "Add successful", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Error ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}