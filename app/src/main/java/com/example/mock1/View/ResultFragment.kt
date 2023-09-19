package com.example.mock1.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.mock1.Login
import com.example.mock1.R
import com.example.mock1.SecondActivity

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

    private lateinit var correct1 : TextView
    private lateinit var wrong1 : TextView
    private lateinit var playA : Button
    private lateinit var exit : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        correct1 = view.findViewById(R.id.correct1)
        wrong1 = view.findViewById(R.id.wrong1)
        playA = view.findViewById(R.id.playAgain)
        exit = view.findViewById(R.id.exit)

        playA.setOnClickListener {
            playClick()
        }

        exit.setOnClickListener {
            exitClick()
        }

        receiveDataFromIntent()

        return view
    }

    private fun playClick() {
        val intent = Intent(requireContext(), SecondActivity::class.java)
        startActivity(intent)
    }

    private fun exitClick() {
        val intent = Intent(requireContext(), Login::class.java)
        startActivity(intent)
    }

    private fun receiveDataFromIntent() {
        val arguments = arguments
        if (arguments != null) {
            val correctAnswer = arguments.getInt("Correct Answer", 0)
            val wrongAnswer = arguments.getInt("Wrong Answer", 0)

            // Gán giá trị vào TextViews
            correct1.text = "$correctAnswer"
            wrong1.text = "$wrongAnswer"
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