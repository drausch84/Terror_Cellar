package com.example.terrorcellar.database

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class MovieReview(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @SerializedName("title") var movieName: String="",
    @SerializedName("rating") var movieRating: Int=0,
    @SerializedName("review") var movieReview: String="",
    @SerializedName("poster") var moviePoster: String=""
) : Parcelable
