package com.example.surfgallery.extensions

import java.text.SimpleDateFormat

private const val PATTERN = "dd.WW.y"

fun String.formatTimeStampToDate(): String = SimpleDateFormat(PATTERN).format(this.toLong())
