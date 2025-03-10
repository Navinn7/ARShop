package com.example.buynow.presentation.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buynow.data.model.Product
import com.example.buynow.presentation.activity.ProductDetailsActivity
import com.example.buynow.R
import com.example.buynow.R.drawable.*

class ProductAdapter(private var productList: ArrayList<Product>, private val context: Context) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.productName.text = product.productName
        holder.productBrand.text = product.productBrand
        holder.productPrice.text = "₹${product.productPrice}"
        holder.productRating.rating = product.productRating

        Glide.with(context)
            .load(product.productImage)
            .into(holder.productImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            // Pass the entire product object instead of just the index
            intent.putExtra("product", product)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productList.size

    fun updateList(newList: ArrayList<Product>) {
        productList = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage_singleProduct)
        val productName: TextView = itemView.findViewById(R.id.productName_singleProduct)
        val productBrand: TextView = itemView.findViewById(R.id.productBrandName_singleProduct)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice_singleProduct)
        val productRating: RatingBar = itemView.findViewById(R.id.productRating_singleProduct)
    }
}