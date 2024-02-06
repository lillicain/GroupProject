package com.example.groupproject.ringtone

data class RingtoneProperty(
    val name: String,
    val year: Int,
    val url: String,
)

class allRingtones {
    var allMyRingtones: Array<RingtoneProperty> = arrayOf(
        RingtoneProperty("GowBell", 1902, "https://www.youtube.com/shorts/FFXL9LJwdQg"),
        RingtoneProperty("Crosley Candlestick", 1920, "https://www.youtube.com/shorts/HkgjI0x_JCo"),
        RingtoneProperty("Bakelite Ericsson", 1930, "https://www.youtube.com/shorts/sk_0TqvxdLE")
        )
}
