package com.example.terrorcellar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import androidx.room.Query

@Dao
interface MovieReviewDAO {
    @Insert
    suspend fun addMovieReview(movieReview:MovieReview)

    @Query("SELECT * FROM moviereview WHERE id = :id")
    fun getMovieReview(id: Int): LiveData<MovieReview>

    @Delete
    suspend fun deleteMovieReview(movieReview: MovieReview)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllMovieReview(movieReviewList: List<MovieReview>)

    @Update
    suspend fun updateMovieReview(movieReview: MovieReview)

    @Query("SELECT * FROM MovieReview")
    fun getAllMovieReviews(): LiveData<List<MovieReview>>

}