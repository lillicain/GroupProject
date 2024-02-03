package com.example.groupproject.eightball

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EightBallViewModel: ViewModel() {
//     val eightBall = EightBall()
    val greengusString = "GREENGUS"
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
        _eightBall.value = EightBall(Answer.YES_DEFINITELY)
    }
    fun resetAnswer() {
//        eightBall.value?.resetAnswer()
    }
}