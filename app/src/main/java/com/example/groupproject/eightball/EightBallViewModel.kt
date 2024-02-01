package com.example.groupproject.eightball

import androidx.lifecycle.ViewModel

class EightBallViewModel: ViewModel() {
     val eightBall = EightBall()

    fun addAnswer(answer: Answer) {
        eightBall.addAnswer(answer)
    }
    fun showAnswer() {
        eightBall.showAnswer()
    }
    fun resetAnswer() {
        eightBall.resetAnswer()
    }
}