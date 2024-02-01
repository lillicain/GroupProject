package com.example.groupproject.eightball

enum class Answer(val value: String) {
    CERTAIN("It is certain"),
    DECIDEDLY_SO("It is decidedly so"),
    WITHOUT_A_DOUBT("Without a doubt"),
    YES_DEFINITELY("Yes definitely"),
    YOU_MAY_RELY_ON_IT("You may rely on it"),

    AS_I_SEE_IT_YES("As I see it, yes"),
    MOST_LIKELY("Most likely"),
    OUTLOOK_GOOD("Outlook good"),
    YES("Yes"),
    SIGNS_POINT_TO_YES("Signs point to yes"),

    REPLY_HAZY("Reply hazy, try again"),
    ASK_AGAIN_LATER("Ask again later"),
    BETTER_NOT_TELL_YOU_NOW("Better not tell you now"),
    CANNOT_PREDICT_NOW("Cannot predict now"),
    CONCENTRATE_AND_ASK_AGAIN("Concentrate and ask again"),

    DONT_COUNT_ON_IT("Don't count on it"),
    MY_REPLY_IS_NO("My reply is no"),
    MY_SOURCES_SAY_NO("My sources say no"),
    OUTLOOK_NOT_SO_GOOD("Outlook not so good"),
    VERY_DOUBTFUL("Very doubtful")
}
data class EightBall (
    var selectedAnswer: Answer,
    private var queuedAnswers: MutableList<Answer> = mutableListOf()
) {
    init {
        setAnswer()
    }
    fun setAnswer() {
        if (queuedAnswers.isNotEmpty()) {
            // Set selectedAnswer to the first item in the queue
            selectedAnswer = queuedAnswers.first()

            // Remove the selectedAnswer from the queue
            queuedAnswers.remove(selectedAnswer)
        } else {
            selectedAnswer = Answer.entries.random()
        }
    }
    fun addAnswer(answer: Answer) {
        queuedAnswers.add(answer)
    }
    fun resetQueue() {
        queuedAnswers = mutableListOf()
    }
//    fun resetAnswer() {
//        selectedAnswer = Answer.entries.random()
//    }
}