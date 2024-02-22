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
            RingtoneProperty("Bakelite Ericsson", "1930",R.raw.bakelite_ericsson_1930),
            RingtoneProperty("Versailles", "1935", R.raw.versailles_1935),
            RingtoneProperty("Siemens", "1942", R.raw.siemens_1942),
            RingtoneProperty("French", "1956",R.raw.french_bakelite_1956),
            RingtoneProperty("Fujitsu Global", "1963", R.raw.fujitsu_global_1963),
            RingtoneProperty("Danmark 2", "1977", R.raw.danmark_2_1977),
            RingtoneProperty("Telestra Telecom", "1981", R.raw.telestra_telecom_1981),
            RingtoneProperty("Motorola Dynatac", "1983", R.raw.motorola_dynatac_1983),
            RingtoneProperty("Siemens C1 Suitcase", "1985", R.raw.siemens_c1_suitcase_1985),
            RingtoneProperty("Radio Shack 17_1050", "1987", R.raw.radio_shack_17_1050_1987),
            RingtoneProperty("Samsung SH-100", "1988", R.raw.samsung_sh_100),
            RingtoneProperty("Motorola Microtac 9800x", "1989", R.raw.motorola_microtac_9800x_1989),
            RingtoneProperty("AEG teleport", "1990", R.raw.aeg_teleport_1990),
            RingtoneProperty("Nokia 101", "1992", R.raw.nokia_101_1992),
            RingtoneProperty("IBM Simon", "1993", R.raw.ibm_simon_1993),
            RingtoneProperty("Ericsson T28", "1994", R.raw.ericsson_t28_1994),
            RingtoneProperty("Nokia 1610", "1995", R.raw.nokia_1610_1995),
            RingtoneProperty("Motorola Startac", "1996", R.raw.motorola_startac_1996),
            RingtoneProperty("Nokia 5110", "1997", R.raw.nokia_5110_1997),
            RingtoneProperty("Siemens S10", "1998", R.raw.siemens_s10_1998),
            RingtoneProperty("Ericsson T28", "1999", R.raw.ericsson_t28_1999),
            RingtoneProperty("Nokia 3310", "1998", R.raw.nokia_3310_2000),
            RingtoneProperty("Samsung E360", "2005",R.raw.samsung_e360_2005),
            RingtoneProperty("Iphone", "2007", R.raw.iphone_2g_2007),
            RingtoneProperty("Blackberry Curve", "2009", R.raw.blackberry_curve_2009),
            RingtoneProperty("HTC One", "2012", R.raw.htc_quietly_brilliant_2012)
        )
    }
}

