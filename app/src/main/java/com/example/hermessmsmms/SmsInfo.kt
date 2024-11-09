package com.example.hermessmsmms

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
class SmsInfo (
    val sender: String,
    val message: String
): Parcelable