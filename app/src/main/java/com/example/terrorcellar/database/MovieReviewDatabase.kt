package com.example.terrorcellar.database

import android.content.Context
import android.content.res.Resources
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.terrorcellar.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(MovieReview::class), version = 1)
abstract class MovieReviewDatabase: RoomDatabase() {
    abstract fun movieReviewDAO(): MovieReviewDAO

    private class MovieReviewDatabaseCallback(
        private val scope: CoroutineScope,
        private val resources: Resources
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val movieReviewDAO = database.movieReviewDAO()
                    prePopulateDatabase(movieReviewDAO)
                }
            }
        }

        private suspend fun prePopulateDatabase(movieReviewDAO: MovieReviewDAO) {
            val jsonString = resources.openRawResource(R.raw.movie_review).bufferedReader().use {
                it.readText()
            }
            val typeToken = object : TypeToken<List<MovieReview>>() {}.type
            val movieReviews = Gson().fromJson<List<MovieReview>>(jsonString, typeToken)
            movieReviewDAO.insertAllMovieReview(movieReviews)
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: MovieReviewDatabase? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope, resources: Resources): MovieReviewDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    MovieReviewDatabase::class.java,
                    "movies_database")
                    .addCallback(MovieReviewDatabaseCallback(coroutineScope, resources))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}