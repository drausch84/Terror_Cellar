package com.example.terrorcellar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review(
    var moviePoster: Int,
    var movieName: String,
    var movieRating: Int,
    var movieReview: String
): Parcelable{

}
