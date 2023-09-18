package com.example.mock1.View

import android.annotation.SuppressLint
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
import com.example.mock1.Model.QuestionModel
import com.example.mock1.R
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
    private lateinit var timer: CountDownTimer

    private lateinit var questions: List<QuestionModel>
    private var selectedOption: Button? = null
    private var correctAnswerCount = 0
    private var wrongAnswerCount = 0
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

        // Khởi tạo ViewModel
        questionViewModel = ViewModelProvider(this)[QuestionViewModel::class.java]

        // Ánh xạ các thành phần giao diện
        questionTextView = view.findViewById(R.id.question)
        option1Button = view.findViewById(R.id.op1)
        option2Button = view.findViewById(R.id.op2)
        option3Button = view.findViewById(R.id.op3)
        option4Button = view.findViewById(R.id.op4)
        finishBtn = view.findViewById(R.id.finishQ)
        nextBtn = view.findViewById(R.id.nextQ)


        // Gắn sự kiện click cho các nút lựa chọn
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

        nextBtn.setOnClickListener {
            nextClick()
        }

        // Khởi tạo câu hỏi ban đầu
        loadNextQuestion()

        return view
    }

    private fun nextClick() {

    }

    private fun loadNextQuestion() {
        println("run test")
        questionViewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        questionViewModel.questions.observe(viewLifecycleOwner, Observer { questionModels ->
            if (!questionModels.isNullOrEmpty()) {
                val question = questionModels[0] // Lấy câu hỏi đầu tiên hoặc tùy chỉnh logic ở đây
                // Hiển thị câu hỏi lên giao diện
                questionTextView.text = question.q
                option1Button.text = question.a
                option2Button.text = question.b
                option3Button.text = question.c
                option4Button.text = question.d
                correctOption = when (question.answer) {
                    "Option1" -> option1Button
                    "Option2" -> option2Button
                    "Option3" -> option3Button
                    "Option4" -> option4Button
                    else -> option1Button // Đặt mặc định là Option 1
                }

            }
        })
    }

    private fun handleOptionClick(selectedOptionButton: Button) {
        // Kiểm tra xem người dùng đã chọn đúng lựa chọn hay không
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
            // Nếu đúng, tăng số câu trả lời đúng
            correctAnswerCount++
        } else {
            // Nếu sai, tăng số câu trả lời sai và đặt màu nền xanh cho đáp án đúng
            wrongAnswerCount++
            correctOption.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green))
        }

        // Chuyển sang câu hỏi mới
        loadNextQuestion()
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