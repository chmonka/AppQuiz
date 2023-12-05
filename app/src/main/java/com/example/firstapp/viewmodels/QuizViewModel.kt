package com.example.firstapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.firstapp.R
import com.example.firstapp.models.Question

private const val TAG = "QuizViewModel"
class QuizViewModel : ViewModel() {
    public var currentIndex = 0
    public val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToBack() {
        if(currentIndex==0){
            currentIndex = questionBank.size - 1
        }
        currentIndex = (currentIndex - 1) % questionBank.size
    }

    init {
        Log.d(TAG, "ViewModel instance created")
    }
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}
