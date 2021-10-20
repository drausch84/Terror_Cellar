package com.example.terrorcellar.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.terrorcellar.database.MovieReview
import com.example.terrorcellar.databinding.ListItemBinding

class ReviewRecyclerAdapter(private var items: List<MovieReview>,private val itemOnClickInterface: ItemOnClickInterface) : RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewViewHolder>(){

    class ReviewViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){
    }
    private var context:Context?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        //add onItemClicked function as argument when a ViewHolder is created
        context=parent.context
        val reviewVH = ReviewViewHolder((ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)))
        return reviewVH
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val currentReview = items[position]

        Glide.with(context!!).load(currentReview.moviePoster).into(holder.binding.moviePic);
        holder.binding.movieTitle.text = currentReview.movieName
        holder.binding.movieDesc.text = currentReview.movieReview
        holder.binding.movieRating.numStars = currentReview.movieRating

        holder.binding.cvMain.setOnClickListener {
            Log.e("@@@@", "onBindViewHolder: "+currentReview.movieName)
            itemOnClickInterface.onItemClickListener(currentReview)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(movieData: List<MovieReview>?) {
        items = movieData!!
        notifyDataSetChanged()
    }

    interface ItemOnClickInterface{
        fun onItemClickListener(currentReview: MovieReview)
        fun onDeleteClickListener(currentReview: MovieReview)
    }

}