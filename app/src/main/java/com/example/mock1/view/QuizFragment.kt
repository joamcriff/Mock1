package com.example.mock1.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mock1.R
import com.example.mock1.SecondActivity
import com.example.mock1.View.ResultFragment
import com.example.mock1.databinding.FragmentQuizBinding
import com.example.mock1.viewmodel.QuestionViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var questionViewModel: QuestionViewModel
    private lateinit var timer: CountDownTimer
    private lateinit var correctOption: Button
    private var isTimeUp = false

    private lateinit var binding: FragmentQuizBinding

    private var correctAnswerCount = 0
    private var wrongAnswerCount = 0
    var index = 0
    var sizeT = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionViewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        val view = binding.root



        timer = object : CountDownTimer(25000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.time.text = "${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                isTimeUp = true
                handleTimeUp()
            }
        }

        binding.op1.setOnClickListener {
            handleOptionClick(binding.op1)
        }
        binding.op2.setOnClickListener {
            handleOptionClick(binding.op2)
        }
        binding.op3.setOnClickListener {
            handleOptionClick(binding.op3)
        }
        binding.op3.setOnClickListener {
            handleOptionClick(binding.op3)
        }

        questionViewModel = ViewModelProvider(this)[QuestionViewModel::class.java]

        // Đăng ký theo dõi LiveData questions
        questionViewModel.getQuestionList().observe(viewLifecycleOwner, Observer { questionModels ->
            // Kiểm tra nếu danh sách câu hỏi không rỗng
            if (!questionModels.isNullOrEmpty()) {
                sizeT = questionModels.size
            }
        })

        // Khởi tạo câu hỏi ban đầu
        loadNextQuestion()
        binding.nextQ.setOnClickListener { nextClick() }

        binding.finishQ.setOnClickListener {
            finish()
        }

        return view
    }

    private fun loadNextQuestion() {
        questionViewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        questionViewModel.getQuestionList().observe(viewLifecycleOwner, Observer { questionModels ->
            if (!questionModels.isNullOrEmpty()) {
                val question = questionModels[index]
                // Hiển thị câu hỏi lên giao diện
                binding.question.text = question.q
                binding.op1.text = question.a
                binding.op2.text = question.b
                binding.op3.text = question.c
                binding.op4.text = question.d
                correctOption = when (question.answer) {
                    "a" -> binding.op1
                    "b" -> binding.op2
                    "c" -> binding.op3
                    "d" -> binding.op4
                    else -> binding.op1 // Đặt mặc định là Option 1
                }
            }
            timer.start()
            stateQuiz()
        })
    }

    private fun nextClick() {
        if (index < sizeT - 1) {
            index++
            loadNextQuestion()
            resetOptionButtons()
        } else {
            showDialog()
        }
    }

    private fun handleTimeUp() {
        if (isTimeUp) {
            // Đặt màu nền cho tất cả các nút lựa chọn
//            resetOptionButtons()
            correctOption.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    private fun finish() {
        val intent = Intent(requireContext(), Login::class.java)
        startActivity(intent)
    }

    private fun showDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Quiz Game")
        alertDialog.setMessage("Congratulation!!!\nYou have answered all the questions. Do you want to see the result?")

        alertDialog.setNegativeButton("PLAY AGAIN") {
                dialog, which ->
            val intent = Intent(requireContext(), SecondActivity::class.java)
            startActivity(intent)
        }

        alertDialog.setPositiveButton("SEE RESULT") {
                dialog, which ->
            passResult()
        }

        val dialog = alertDialog.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.red))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.red))
        }

        alertDialog.show()
    }

    private fun passResult() {
        val fragment = ResultFragment()
        val bundle = Bundle()
        bundle.putInt("Correct Answer", correctAnswerCount)
        bundle.putInt("Wrong Answer", wrongAnswerCount)
        fragment.arguments = bundle
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.quiz, fragment)
        fragmentTransaction?.commit()
    }

    private fun resetOptionButtons() {
        binding.op1.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.op2.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.op3.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding.op4.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
    }

    private fun stateQuiz() {
        binding.correct.text = "$correctAnswerCount"
        binding.wrong.text = "$wrongAnswerCount"
    }

    @SuppressLint("SuspiciousIndentation")
    private fun handleOptionClick(selectedOptionButton: Button) {
        val isCorrect = selectedOptionButton == correctOption

        // Đặt màu nền cho lựa chọn đã chọn
        selectedOptionButton.backgroundTintList =
            if (isCorrect) ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            ) else ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red))

            timer.cancel()

            if (isCorrect) {
                correctAnswerCount++
            } else {
                wrongAnswerCount++
                correctOption.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green))
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}