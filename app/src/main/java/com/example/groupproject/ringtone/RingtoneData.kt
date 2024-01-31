package com.example.groupproject.ringtone

import java.time.Year
import java.util.UUID

data class RingtoneData(
    val name: String,
    val year: Int,
    val url: String,
)

class allRingtones {
    val gowBell = RingtoneData("GowBell", 1902,
        "https://www.youtube.com/shorts/FFXL9LJwdQg")

    val crosleyCandlestick = RingtoneData("Crosley Candlestick", 1920,
        "https://www.youtube.com/shorts/HkgjI0x_JCo")

    val bakeliteEricsson = RingtoneData("Bakelite Ericsson", 1930, "https://www.youtube.com/shorts/sk_0TqvxdLE")
}
