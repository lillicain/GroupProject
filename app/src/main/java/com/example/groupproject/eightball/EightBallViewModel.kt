package com.example.groupproject.eightball

import android.os.CountDownTimer
import android.widget.Button
import android.widget.GridLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupproject.evilgarden.SharedViewModel

class EightBallViewModel(
//    private val sharedViewModel: SharedViewModel
) : ViewModel() {


    var greengusString = MutableLiveData<String>("")
    private val _eightBall = MutableLiveData<EightBall>()
    var _buttonsVisible = MutableLiveData<Boolean>()
    val buttonsVisible: LiveData<Boolean>
        get() = _buttonsVisible
    val eightBall: LiveData<EightBall>
        get() = _eightBall

    private var canShake = true
    private val shakeCooldownMillis = 2000L // Set the cooldown time in milliseconds
    private val shakeTimerMillis = 5000L // Set the timer duration in milliseconds
    private var shakeTimer: CountDownTimer? = null
    private var cooldownTimer: CountDownTimer? = null

    private var _selectedAnswer = MutableLiveData<Answer>()
    val selectedAnswer: LiveData<Answer>
        get() = _selectedAnswer

    init {
        _eightBall.value = EightBall(Answer.entries.random())
    }
    fun createButtons(gridLayout: GridLayout) {
        val answers = Answer.values()

        for (answer in answers) {
            val button = Button(gridLayout.context)
            val layoutParams = GridLayout.LayoutParams()
            layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT
            button.layoutParams = layoutParams

            button.text = answer.value
            button.setOnClickListener { addAnswer(answer) }

            gridLayout.addView(button)
        }
    }
    fun showAnswer() {
        if (canShake) {
            _eightBall.value?.setAnswer()
            _selectedAnswer.value = _eightBall.value?.selectedAnswer
            canShake = false
            startCooldownTimer(shakeCooldownMillis)
            startShakeTimer()
            println(" RINKLEE ${eightBall.value?.selectedAnswer?.value}")
            updateXP()
        } else {
            println("Shake is on cooldown")
        }
    }
    fun addAnswer(answer: Answer) {
        _eightBall.value?.addAnswer(answer)
        updateXP2()
    }

    private fun startShakeTimer() {
        shakeTimer?.cancel()
        shakeTimer = object : CountDownTimer(shakeTimerMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Timer is ticking (optional)
            }

            override fun onFinish() {
                // Timer finished, allow shaking again
                canShake = true
            }
        }.start()
    }

    private fun startCooldownTimer(cooldownMillis: Long) {
        cooldownTimer?.cancel()
        cooldownTimer = object : CountDownTimer(cooldownMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Cooldown timer is ticking (optional)
            }

            override fun onFinish() {
                // Cooldown timer finished, allow shaking again
                canShake = true
            }
        }.start()
    }

    fun changeButtonVisibility() {
        if (_buttonsVisible.value == true) {
            _buttonsVisible.value = false
        } else {
            _buttonsVisible.value = true
        }
    }

    fun changeGreengus() {
        if (greengusString.value == "GREENGUS") {
            greengusString.value = "Rumple"
        } else if (greengusString.value == "Rumple") {
            greengusString.value = "GREENGUS"
        }
    }

    override fun onCleared() {
        super.onCleared()
        shakeTimer?.cancel()
        cooldownTimer?.cancel()
    }
    private fun updateXP() {
//        sharedViewModel.updateUserXP(xpToAdd = 5)
    }
    private fun updateXP2() {
//        sharedViewModel.updateUserXP(xpToAdd = 3)
    }

}