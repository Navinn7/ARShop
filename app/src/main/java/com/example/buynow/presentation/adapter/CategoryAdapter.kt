package com.example.buynow.presentation.adapter


import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buynow.data.model.Category

import com.example.buynow.R
import com.example.buynow.presentation.activity.ProductListActivity

class CategoryAdapter(private val context: Context, private val categoryList: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val categoryView = LayoutInflater.from(parent.context).inflate(R.layout.category_single,parent,false)
        return ViewHolder(categoryView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryName_CateSingle.text = category.Name  // Changed to match Category.kt property

        Glide.with(context)
            .load(category.Image)  // Changed to match Category.kt property
            .into(holder.categoryImage_CateSingle)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductListActivity::class.java)
            intent.putExtra("categoryName", category.Name)  // Changed to match Category.kt property
            context.startActivity(intent)
        }
    }



    override fun getItemCount(): Int {
        return categoryList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val categoryImage_CateSingle: ImageView = itemView.findViewById(R.id.categoryImage_CateSingle)
        val categoryName_CateSingle: TextView = itemView.findViewById(R.id.categoryName_CateSingle)

    }

}