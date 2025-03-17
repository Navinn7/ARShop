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

class SaleProductAdapter(private val saleProductList: ArrayList<Product>, private val ctx: Context) :
    RecyclerView.Adapter<SaleProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView = LayoutInflater.from(parent.context).inflate(R.layout.single_product, parent, false)
        return ViewHolder(productView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: Product = saleProductList[position]

        holder.apply {
            productBrandName_singleProduct.text = product.productBrand
            productName_singleProduct.text = product.productName
            productPrice_singleProduct.text = "₹${product.productPrice}"
            productRating_singleProduct.rating = product.productRating

            Glide.with(ctx)
                .load(product.productImage)
                .placeholder(R.drawable.bn)
                .into(productImage_singleProduct)

            // Handle discount visibility
            if (product.productHave) {
                discountTv_singleProduct.text = product.productDisCount
                discount_singleProduct.visibility = View.VISIBLE
            } else {
                discount_singleProduct.visibility = View.VISIBLE
                discountTv_singleProduct.text = "New"
            }

            // Handle item click
            itemView.setOnClickListener {
                val intent = Intent(ctx, ProductDetailsActivity::class.java)
                intent.apply {
                    putExtra("product", product) // Pass the entire product object
                    putExtra("ProductIndex", position)
                    putExtra("ProductFrom", "Cover")
                }
                ctx.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = saleProductList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage_singleProduct: ImageView = itemView.findViewById(R.id.productImage_singleProduct)
        val productRating_singleProduct: RatingBar = itemView.findViewById(R.id.productRating_singleProduct)
        val productBrandName_singleProduct: TextView = itemView.findViewById(R.id.productBrandName_singleProduct)
        val discountTv_singleProduct: TextView = itemView.findViewById(R.id.discountTv_singleProduct)
        val productName_singleProduct: TextView = itemView.findViewById(R.id.productName_singleProduct)
        val productPrice_singleProduct: TextView = itemView.findViewById(R.id.productPrice_singleProduct)
        val discount_singleProduct: LinearLayout = itemView.findViewById(R.id.discount_singleProduct)
    }
}