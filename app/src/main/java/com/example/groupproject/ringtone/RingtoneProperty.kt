package com.example.groupproject.ringtone

import com.example.groupproject.R

data class RingtoneProperty(
    val name: String,
    val year: String,
    val muisicFile: Int
) {
    companion object {
        private var idCounter = 0
    }

    val id: Int = generateID()
    private fun generateID(): Int {
        return idCounter++
    }
}


class RingtoneManager {
    companion object {
        val allMyRingtones: Array<RingtoneProperty> = arrayOf(
            RingtoneProperty("GowBell", "1902", R.raw.gowbell_1902),
            RingtoneProperty("Crosley Candlestick", "1920", R.raw.crosley_candles_1920),
            RingtoneProperty("Bakelite Ericsson", "1930", R.raw.bakelite_ericsson_1930),
            RingtoneProperty("Versailles", "1935", R.raw.versailles_1935),
            RingtoneProperty("Siemens", "1942", R.raw.siemens_1942),
            RingtoneProperty("French", "1956", R.raw.french_bakelite_1956),
            RingtoneProperty("Fujitsu Global", "1963", R.raw.fujitsu_global_1963),
            RingtoneProperty("Danmark 2", "1977", R.raw.danmark_2_1977),
            RingtoneProperty("Radio Shack 17_1050", "1987", R.raw.radio_shack_17_1050_1987),
            RingtoneProperty("Nokia 101", "1992", R.raw.nokia_101_1992),
            RingtoneProperty("Samsung E360", "2005", R.raw.samsung_e360_2005),
            RingtoneProperty("Iphone", "2007", R.raw.iphone_2g_2007),
            RingtoneProperty("Blackberry Curve", "2009", R.raw.blackberry_curve_2009),
            RingtoneProperty("HTC One", "2012", R.raw.htc_quietly_brilliant_2012)
        )
    }
}

