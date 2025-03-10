package com.emperormoh.myplayground.presentation.screens.common

fun convertNetworkIndexToName(index: Int): String {
    return when (index) {
        0 -> "9mobile"
        1 ->  "Airtel"
        2 ->  "Globacom"
        3 ->  "MTN"
        else -> { "MTN"}
    }
}