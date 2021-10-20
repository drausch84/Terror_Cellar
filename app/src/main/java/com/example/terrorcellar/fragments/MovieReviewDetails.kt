package com.example.terrorcellar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.terrorcellar.R


class MovieReviewDetails : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_review_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val movieRating=view.findViewById<RatingBar>(R.id.movieRating)
        val moviePic=view.findViewById<ImageView>(R.id.Poster)
        val movieTitle=view.findViewById<TextView>(R.id.movieHeader)
        val movieReviewText=view.findViewById<TextView>(R.id.movieReviewText)
        val backButton=view.findViewById<ImageView>(R.id.backButton)
        val editReviewBtn=view.findViewById<Button>(R.id.editReviewBtn)

        val rating = bundle!!.getInt("rating")
        val name = bundle.getString("name")
        val review = bundle.getString("review")
        val poster = bundle.getString("poster")
        val id = bundle.getInt("id")
        Glide.with(requireActivity()).load(poster).into(moviePic);
        movieTitle.text = name
        movieReviewText.text = review
        movieRating.numStars = rating.toInt()

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_movieReviewDetails_to_movieListFragment)
        }

        editReviewBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("name", name)
            bundle.putInt("id",id)
            bundle.putInt("rating",rating)
            bundle.putString("poster", poster)
            bundle.putString("review", review)
            findNavController().navigate(R.id.action_movieReviewDetails_to_addReviewFragment,bundle)
        }
    }


}