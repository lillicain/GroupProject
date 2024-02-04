package com.example.groupproject.eightball

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EightBallViewModel: ViewModel() {
//     val eightBall = EightBall()
var greengusString = MutableLiveData<String>("GREENGUS")
    private val _eightBall = MutableLiveData<EightBall>()

    val eightBall: LiveData<EightBall>
        get() = _eightBall
//
    private var _selectedAnswer = MutableLiveData<Answer>()
    val selectedAnswer: LiveData<Answer>
        get() = _selectedAnswer
//    private var _queuedAnswer = MutableLiveData<MutableList<Answer>>()
//    val queuedAnswer: LiveData<MutableList<Answer>>
//        get() = _queuedAnswer
    init {
        _eightBall.value = EightBall(Answer.entries.random())

    }
    fun showAnswer() {
        if (_eightBall.value != null) {
            _eightBall.value?.setAnswer()
            _selectedAnswer.value = _eightBall.value?.selectedAnswer
        } else {
            println(" RINKLEE ${eightBall.value?.selectedAnswer?.value}")
        }



    }
    fun addAnswer(answer: Answer) {
        _eightBall.value?.addAnswer(answer)
    }
    fun changeGreengus() {
        if (greengusString.value == "GREENGUS") {
            greengusString.value = "Rumple"
        } else if (greengusString.value == "Rumple") {
            greengusString.value = "GREENGUS"
        }
    }
    fun resetAnswer() {
//        eightBall.value?.resetAnswer()
    }
}