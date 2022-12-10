package com.example.msmusic

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions




class LikeMyAdapter(private val newsList : ArrayList<News>) : RecyclerView.Adapter<LikeMyAdapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.like_list_item,parent,false)
        return MyViewHolder(itemView,mListener)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newsList[position]
        holder.name.text = currentItem.name
        Glide.with(holder.itemView).load("${currentItem.image}").error(R.drawable.ic_baseline_person_24).into(holder.imageupl)
    }


    override fun getItemCount(): Int {
        return newsList.size
    }
    class MyViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView)
    {
        val name : TextView = itemView.findViewById(R.id.like_song_name)
        val imageupl : ImageView = itemView.findViewById(R.id.like_home_image)

        init {

            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}