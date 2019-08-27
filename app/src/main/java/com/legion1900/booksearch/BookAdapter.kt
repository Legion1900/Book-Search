package com.legion1900.booksearch

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.legion1900.booksearch.parser.Work
import com.squareup.picasso.Picasso

class BookAdapter(val dataSource: MutableList<Work>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    val picasso = Picasso.get()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemLayout = R.layout.book_item_view
        val inflator = LayoutInflater.from(parent.context)
        val view = inflator.inflate(itemLayout, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int = dataSource.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val data = dataSource[position]
        holder.apply {
            author.text = data.author.name
            title.text = data.title
            avgRating.text = data.avgRating.toString()
//          TODO: add placeholder and error
            picasso.load(Uri.parse(data.imgUrl)).into(bookImg)
        }
    }

    fun extendData(nextBooks: List<Work>) {
        dataSource.addAll(nextBooks)
        notifyDataSetChanged()
    }

    fun swapData(newSearch: List<Work>) {
        dataSource.clear()
        dataSource.addAll(newSearch)
        notifyDataSetChanged()
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImg = itemView.findViewById<ImageView>(R.id.book_image)
        val author: TextView = itemView.findViewById(R.id.author)
        val title: TextView = itemView.findViewById(R.id.title)
        val avgRating: TextView = itemView.findViewById(R.id.avg_rating)
    }
}