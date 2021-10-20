package com.example.terrorcellar.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.terrorcellar.R
import com.example.terrorcellar.database.MovieReview
import com.example.terrorcellar.models.MovieViewModel

class AddAndEditReviewFragment : Fragment() {
    private var addMoviePoster: EditText? = null
    private var addMovieReview: EditText? = null
    private var addRating: RatingBar? = null
    private var addMovieName: EditText? = null
    private lateinit var movieViewModel: MovieViewModel
    private var id:Int?= 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val addBtn: Button = view.findViewById(R.id.add_review_btn)
        addMovieName = view.findViewById<EditText>(R.id.addMovieName)
        addMoviePoster = view.findViewById<EditText>(R.id.addMoviePoster)
        addRating = view.findViewById<RatingBar>(R.id.movieRating)
        addMovieReview = view.findViewById<EditText>(R.id.addMovieReviewTextField)
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        val backButton=view.findViewById<ImageView>(R.id.backButton)

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_addReviewFragment_to_movieReviewDetails,bundle)
        }

        if (bundle != null) {
            val name = bundle.getString("name")
            val rating = bundle.getInt("rating")
            id = bundle.getInt("id")
            val poster = bundle.getString("poster")
            val review = bundle.getString("review")
            addMovieName!!.setText(name)
            addMoviePoster!!.setText(poster)
            addMovieReview!!.setText(review)
            addRating!!.rating = rating.toFloat()
            addBtn.setText("Edit Review")
        }

        addBtn.setOnClickListener {
            if(bundle!=null){
                editMovieReview()
            }else {
                addMovieReview()
            }
        }
    }

    private fun editMovieReview() {
        val movieName = addMovieName!!.text.toString()
        val movieReviewString = addMovieReview!!.text.toString()
        val moviePoster = addMoviePoster!!.text.toString()
        val movieRating = addRating!!.rating.toInt()

        if(movieName.isEmpty()){
            Toast.makeText(activity, "Please Enter a Movie Name.", Toast.LENGTH_SHORT).show()
            return
        }
        if(movieReviewString.isEmpty()){
            Toast.makeText(activity, "Please Enter a Movie Review.", Toast.LENGTH_SHORT).show()
            return
        }
        if(moviePoster.isEmpty()){
            Toast.makeText(activity, "Please Enter a Movie Poster URL.", Toast.LENGTH_SHORT).show()
            return
        }

        val movieReview = MovieReview(id!!, movieName, movieRating, movieReviewString, moviePoster)
        movieViewModel.updateMovieReview(movieReview)
        val bundle = Bundle()
        bundle.putString("name", movieName)
        bundle.putInt("id",bundle.getInt("id"))
        bundle.putInt("rating",movieRating)
        bundle.putString("poster", moviePoster)
        bundle.putString("review", movieReviewString)
        findNavController().navigate(R.id.action_addReviewFragment_to_movieReviewDetails,bundle)
    }

    fun addMovieReview() {
        val movieName = addMovieName!!.text.toString()
        val movieReviewString = addMovieReview!!.text.toString()
        val moviePoster = addMoviePoster!!.text.toString()
        val movieRating = addRating!!.rating.toInt()

        if(movieName.isEmpty()){
            Toast.makeText(activity, "Please Enter a Movie Name.", Toast.LENGTH_SHORT).show()
            return
        }
        if(movieReviewString.isEmpty()){
            Toast.makeText(activity, "Please Enter a Movie Review.", Toast.LENGTH_SHORT).show()
            return
        }
        if(moviePoster.isEmpty()){
            Toast.makeText(activity, "Please Enter a Movie Poster URL.", Toast.LENGTH_SHORT).show()
            return
        }
        val movieReview = MovieReview(0, movieName, movieRating, movieReviewString, moviePoster)
        movieViewModel.addMovieReview(movieReview)
        findNavController().navigate(R.id.action_addReviewFragment_to_movieListFragment)

    }


}