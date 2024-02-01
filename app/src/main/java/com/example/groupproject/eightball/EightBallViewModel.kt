package com.example.groupproject.eightball

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EightBallViewModel: ViewModel() {
//     val eightBall = EightBall()

    private val _eightBall = MutableLiveData<EightBall>()
    val eightBall: LiveData<EightBall>
        get() = _eightBall

    private var _selectedAnswer = MutableLiveData<Answer>()
    val selectedAnswer: LiveData<Answer>
        get() = _selectedAnswer
    private var _queuedAnswer = MutableLiveData<MutableList<Answer>>()
    val queuedAnswer: LiveData<MutableList<Answer>>
        get() = _queuedAnswer
    init {
        _selectedAnswer.value = Answer.SIGNS_POINT_TO_YES
        _queuedAnswer.value = mutableListOf()
    }
    fun addAnswer(answer: Answer) {
        eightBall.value?.addAnswer(answer)
    }
    fun showAnswer() {
        println(eightBall.value?.selectedAnswer?.value.toString())
        eightBall.value?.setAnswer()
    }
    fun resetAnswer() {
//        eightBall.value?.resetAnswer()
    }
}