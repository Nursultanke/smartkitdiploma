package com.example.smartkitbeta.services

data class SensorsItem(
    val lamp: Int,
    val socket: Int,
    val temperatura: Float,
    val humidity: Float,
    val gas: Int,
    val smoke: Int,
    val motion: Int
)