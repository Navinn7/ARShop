package com.example.buynow.presentation.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buynow.presentation.adapter.CategoryAdapter
import com.example.buynow.presentation.adapter.CoverProductAdapter

import com.example.buynow.data.model.Category
import com.example.buynow.data.model.Product

import com.example.buynow.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException


class ShopFragment : Fragment() {


    private lateinit var cateList:ArrayList<Category>
    private lateinit var coverProduct:ArrayList<Product>

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var coverProductAdapter: CoverProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val coverRecView_shopFrag : RecyclerView = view.findViewById(R.id.coverRecView_shopFrag)
        val categoriesRecView : RecyclerView = view.findViewById(R.id.categoriesRecView)


        cateList = arrayListOf()
        coverProduct = arrayListOf()

        setCoverData()
        setCategoryData()

        coverRecView_shopFrag.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        coverRecView_shopFrag.setHasFixedSize(true)
        coverProductAdapter = CoverProductAdapter(activity as Context, coverProduct )
        coverRecView_shopFrag.adapter = coverProductAdapter


        categoriesRecView.layoutManager = GridLayoutManager(context,2,LinearLayoutManager.VERTICAL,false)
        categoriesRecView.setHasFixedSize(true)
        categoryAdapter = CategoryAdapter(activity as Context, cateList )
        categoriesRecView.adapter = categoryAdapter


        return view
    }

    private fun setCategoryData() {
        cateList.add(Category("Electronics","https://images.unsplash.com/photo-1511707171634-5f897ff02aa9"))
        cateList.add(Category("Household","https://images.unsplash.com/photo-1517336714731-489689fd1ca8"))
        cateList.add(Category("Sofas","https://www.estre.in/cdn/shop/files/2-min_2d969ef7-e5ee-4cdd-a08a-6b0871211bab.jpg?v=1734121428"))
        cateList.add(Category("Furniture","https://m.media-amazon.com/images/I/91M4bSjWLBL.jpg"))
        cateList.add(Category("Tables","https://images.unsplash.com/photo-1519710164239-da123dc03ef4"))
        cateList.add(Category("Chairs","https://images.unsplash.com/photo-1524758631624-e2822e304c36"))
        cateList.add(Category("TVs","https://www.livemint.com/lm-img/img/2024/05/02/1600x900/75_inch_smart_tv_1714649551876_1714649561405.jpg"))
        cateList.add(Category("Kitchen Appliances","https://www.livemint.com/lm-img/img/2024/12/06/1600x900/Kitchen_Appliances_1733468549152_1733468558787.jpeg"))


    }


    fun getJsonData(context: Context, fileName: String): String? {

        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun setCoverData() {

        val jsonFileString = context?.let {

            getJsonData(it, "CoverProducts.json")
        }
        val gson = Gson()

        val listCoverType = object : TypeToken<List<Product>>() {}.type

        val coverD: List<Product> = gson.fromJson(jsonFileString, listCoverType)

        coverD.forEachIndexed { _, person ->
            coverProduct.add(person)

        }


    }


}


