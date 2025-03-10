package com.example.buynow.presentation.activity

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buynow.R
import com.example.buynow.data.model.Product
import com.example.buynow.presentation.adapter.ProductAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class ProductListActivity : AppCompatActivity() {
    private lateinit var categoryProducts: ArrayList<Product>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        categoryName = intent.getStringExtra("categoryName") ?: ""

        val backButton: ImageView = findViewById(R.id.backButton)
        val categoryTitle: TextView = findViewById(R.id.categoryTitle)
        val productsRecyclerView: RecyclerView = findViewById(R.id.productsRecyclerView)

        categoryTitle.text = categoryName
        categoryProducts = arrayListOf()

        // Load and filter products
        loadProducts()

        // Setup RecyclerView
        productsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        productAdapter = ProductAdapter(categoryProducts, this)
        productsRecyclerView.adapter = productAdapter

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadProducts() {
        // Load from NewProducts.json
        val newProductsJson = getJsonData(this, "NewProducts.json")
        val coverProductsJson = getJsonData(this, "CoverProducts.json")

        val gson = Gson()
        val productListType = object : TypeToken<List<Product>>() {}.type

        val newProducts: List<Product> = gson.fromJson(newProductsJson, productListType)
        val coverProducts: List<Product> = gson.fromJson(coverProductsJson, productListType)

        // Filter products by category
        categoryProducts.addAll(newProducts.filter { it.productCategory == categoryName })
        categoryProducts.addAll(coverProducts.filter { it.productCategory == categoryName })
    }

    private fun getJsonData(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }
}