package com.example.groupproject.ringtone

import androidx.lifecycle.ViewModel
import java.util.UUID

data class RingtoneProperty(
    val name: String,
    val year: Int,
    val url: String,
    val id: Int
)

class allRingtones: ViewModel() {
    var allMyRingtones: Array<RingtoneProperty> = arrayOf(
        RingtoneProperty("GowBell", 1902, "https://www.youtube.com/shorts/FFXL9LJwdQg", 0), // 0
        RingtoneProperty("Crosley Candlestick", 1920, "https://www.youtube.com/shorts/HkgjI0x_JCo", 1), // 1
        RingtoneProperty("Bakelite Ericsson", 1930, "https://www.youtube.com/shorts/sk_0TqvxdLE", 2) // 2
        )
}