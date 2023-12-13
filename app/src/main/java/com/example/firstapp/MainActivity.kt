package com.example.firstapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.firstapp.viewmodels.QuizViewModel

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

open class MainActivity : AppCompatActivity() {


    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var sumbitButton: Button
    private lateinit var checkAnwser: Button
    private lateinit var questionTextView: TextView
    private lateinit var returnButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }
    var countSuccessQuestions = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        sumbitButton = findViewById(R.id.sucess_button)
        checkAnwser = findViewById(R.id.check_answer)
        returnButton = findViewById(R.id.return_button)
        questionTextView = findViewById(R.id.question_text_view)

        sumbitButton.isEnabled = false
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            sumbitButton.isEnabled = true
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            sumbitButton.isEnabled = true
        }

        sumbitButton.setOnClickListener {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            sumbitButton.isEnabled = false
            if (quizViewModel.currentIndex == quizViewModel.questionBank.size - 1) {
                trueButton.isEnabled = false
                falseButton.isEnabled = false
                falseButton.isEnabled = false
                returnButton.isEnabled = true
            } else {
                quizViewModel.moveToNext()
            }
            updateQuestion()
        }

        returnButton.isEnabled = false
        returnButton.setOnClickListener {
            countSuccessQuestions = 0
            quizViewModel.currentIndex = 0
            updateQuestion()
            Toast.makeText(this, "Квиз запущен заново", Toast.LENGTH_SHORT).show()
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            falseButton.isEnabled = true
            returnButton.isEnabled = false
        }

        checkAnwser.setOnClickListener {
            showResultDialog()
        }
        updateQuestion()
    }

    fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = if (userAnswer == correctAnswer) {
            countSuccessQuestions++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    fun showResultDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Результаты")
            .setMessage("Количество правильных ответов: $countSuccessQuestions")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }
}
