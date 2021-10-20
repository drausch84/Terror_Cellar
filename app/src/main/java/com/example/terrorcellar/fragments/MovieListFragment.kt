package com.example.terrorcellar.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.terrorcellar.LoginActivity
import com.example.terrorcellar.models.MovieViewModel
import com.example.terrorcellar.R
import com.example.terrorcellar.database.MovieReview
import com.example.terrorcellar.adapter.ReviewRecyclerAdapter
import com.example.terrorcellar.adapter.SwipeToDeleteCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MovieListFragment : Fragment(), ReviewRecyclerAdapter.ItemOnClickInterface {
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var reviewAdapter: ReviewRecyclerAdapter
    var mData: List<MovieReview>? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val signOutBtn = view.findViewById<Button>(R.id.signout_btn)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.webclient_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        signOutBtn.setOnClickListener {
            signOut()
        }
        recyclerView = view.findViewById(R.id.reviewList)
        val addReviewBtn = view.findViewById<FloatingActionButton>(R.id.add_review_btn)
        recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        addReviewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_movieListFragment_to_addReviewFragment)
        }
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        reviewAdapter = ReviewRecyclerAdapter(mutableListOf(), this)
        recyclerView.adapter = reviewAdapter
        val llMain = view.findViewById(R.id.llMain) as LinearLayout
        val progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        Handler().postDelayed({
            progressBar.visibility=View.GONE
            llMain.visibility=View.VISIBLE
            addReviewBtn.visibility=View.VISIBLE
            view.findViewById<TextView>(R.id.user_email_welcome).text = "Logged in as: " + auth.currentUser?.email
        },3000)

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onDeleteClickListener(mData!!.get(viewHolder.adapterPosition))
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        listAllMovie()
    }

    private fun signOut(){
        auth.signOut()
        googleSignInClient.signOut()
        startActivity(Intent(activity, LoginActivity::class.java))
        activity?.finish()

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    fun listAllMovie() {
        movieViewModel.getAllMovieReview()
            .observe(viewLifecycleOwner, object : Observer<List<MovieReview>> {
                override fun onChanged(movieData: List<MovieReview>?) {
                    CoroutineScope(Dispatchers.Main).launch {
                        mData = movieData
                        reviewAdapter.setData(movieData)
                    }
                }
            })
    }

    override fun onItemClickListener(currentReview: MovieReview) {
        val bundle = Bundle()
        bundle.putString("name", currentReview.movieName)
        bundle.putInt("id",currentReview.id)
        bundle.putInt("rating", currentReview.movieRating)
        bundle.putString("poster", currentReview.moviePoster)
        bundle.putString("review", currentReview.movieReview)
        findNavController().navigate(R.id.action_movieListFragment_to_movieReviewDetails, bundle)
    }

    override fun onDeleteClickListener(currentReview: MovieReview) {
        movieViewModel.deleteMovieReview(currentReview)
        listAllMovie()
    }

}