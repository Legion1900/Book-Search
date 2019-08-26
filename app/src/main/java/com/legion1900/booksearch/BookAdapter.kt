package com.legion1900.booksearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.legion1900.booksearch.parser.Work

class BookAdapter(var dataSource: List<Work>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

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
        }
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val author = itemView.findViewById<TextView>(R.id.author)
        val title: TextView = itemView.findViewById(R.id.title)
        val avgRating: TextView = itemView.findViewById(R.id.avg_rating)
    }

}