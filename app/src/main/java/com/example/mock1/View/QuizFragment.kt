package com.example.mock1.View

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
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mock1.Login
import com.example.mock1.Model.QuestionModel
import com.example.mock1.R
import com.example.mock1.SecondActivity
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
    private lateinit var questionTextView: TextView
    private lateinit var option1Button: Button
    private lateinit var option2Button: Button
    private lateinit var option3Button: Button
    private lateinit var option4Button: Button
    private lateinit var correctOption: Button
    private lateinit var finishBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var time : TextView
    private lateinit var correct : TextView
    private lateinit var wrong : TextView
    private lateinit var timer: CountDownTimer
    private var isTimeUp = false

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
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        questionTextView = view.findViewById(R.id.question)
        option1Button = view.findViewById(R.id.op1)
        option2Button = view.findViewById(R.id.op2)
        option3Button = view.findViewById(R.id.op3)
        option4Button = view.findViewById(R.id.op4)
        finishBtn = view.findViewById(R.id.finishQ)
        nextBtn = view.findViewById(R.id.nextQ)
        time = view.findViewById(R.id.time)
        correct = view.findViewById(R.id.correct)
        wrong = view.findViewById(R.id.wrong)


        timer = object : CountDownTimer(25000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                time.text = "${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                isTimeUp = true
                handleTimeUp()
            }
        }

        option1Button.setOnClickListener {
            handleOptionClick(option1Button)
        }
        option2Button.setOnClickListener {
            handleOptionClick(option2Button)
        }
        option3Button.setOnClickListener {
            handleOptionClick(option3Button)
        }
        option4Button.setOnClickListener {
            handleOptionClick(option4Button)
        }

        questionViewModel = ViewModelProvider(this)[QuestionViewModel::class.java]

        // Đăng ký theo dõi LiveData questions
        questionViewModel.questions.observe(viewLifecycleOwner, Observer { questionModels ->
            // Kiểm tra nếu danh sách câu hỏi không rỗng
            if (!questionModels.isNullOrEmpty()) {
                sizeT = questionModels.size
            }
        })

        // Khởi tạo câu hỏi ban đầu
        loadNextQuestion()
        nextBtn.setOnClickListener { nextClick() }

        finishBtn.setOnClickListener {
            finish()
        }

        return view
    }

    private fun loadNextQuestion() {
        questionViewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        questionViewModel.questions.observe(viewLifecycleOwner, Observer { questionModels ->
            if (!questionModels.isNullOrEmpty()) {
                val question = questionModels[index] // Lấy câu hỏi đầu tiên hoặc tùy chỉnh logic ở đây
                // Hiển thị câu hỏi lên giao diện
                questionTextView.text = question.q
                option1Button.text = question.a
                option2Button.text = question.b
                option3Button.text = question.c
                option4Button.text = question.d
                correctOption = when (question.answer) {
                    "a" -> option1Button
                    "b" -> option2Button
                    "c" -> option3Button
                    "d" -> option4Button
                    else -> option1Button // Đặt mặc định là Option 1
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
        if (!isTimeUp) {
            // Đặt màu nền cho tất cả các nút lựa chọn
            resetOptionButtons()
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
        option1Button.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        option2Button.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        option3Button.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        option4Button.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
    }

    private fun stateQuiz() {
        correct.text = "$correctAnswerCount"
        wrong.text = "$wrongAnswerCount"
    }

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

        // Tạm dừng đồng hồ đếm thời gian
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