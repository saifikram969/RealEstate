package com.btjnonbrokerage.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImgList( val  img : ArrayList<String>) : Parcelable
