package com.example.a30days.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Tip(
    val day: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)
