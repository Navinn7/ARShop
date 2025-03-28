package com.example.buynow.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buynow.presentation.adapter.ProductAdapter
import com.example.buynow.R
import com.example.buynow.data.model.Product
import com.example.buynow.utils.DefaultCard.GetDefCard
import com.example.buynow.utils.Extensions.cardXXGen
import com.example.buynow.utils.Extensions.toast
import com.example.buynow.data.local.room.CartViewModel
import com.example.buynow.data.local.room.ProductEntity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class ProductDetailsActivity : AppCompatActivity() {

    var productIndex: Int = -1
    lateinit var ProductFrom: String
    private lateinit var cartViewModel: CartViewModel
    private val TAG = "TAG"
    lateinit var productImage_ProductDetailsPage: ImageView
    lateinit var backIv_ProfileFrag: ImageView
    lateinit var productName_ProductDetailsPage: TextView
    lateinit var productPrice_ProductDetailsPage: TextView
    lateinit var productBrand_ProductDetailsPage: TextView
    lateinit var productDes_ProductDetailsPage: TextView
    lateinit var RatingProductDetails: TextView
    lateinit var productRating_singleProduct: RatingBar
    private lateinit var product: Product

    lateinit var RecomRecView_ProductDetailsPage: RecyclerView
    lateinit var newProductAdapter: ProductAdapter
    lateinit var newProduct: ArrayList<Product>

    lateinit var pName: String
    var qua: Int = 1
    var pPrice: Int = 0
    lateinit var pPid: String
    lateinit var pImage: String
    private lateinit var modelURL: String
    lateinit var cardNumber: String
    private lateinit var tryAtHomeButton: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        product = intent.getParcelableExtra("product")!!
        productIndex = intent.getIntExtra("ProductIndex", -1)
        ProductFrom = intent.getStringExtra("ProductFrom").toString()

        productImage_ProductDetailsPage = findViewById(R.id.productImage_ProductDetailsPage)
        productName_ProductDetailsPage = findViewById(R.id.productName_ProductDetailsPage)
        productPrice_ProductDetailsPage = findViewById(R.id.productPrice_ProductDetailsPage)
        productBrand_ProductDetailsPage = findViewById(R.id.productBrand_ProductDetailsPage)
        productDes_ProductDetailsPage = findViewById(R.id.productDes_ProductDetailsPage)
        productRating_singleProduct = findViewById(R.id.productRating_singleProduct)
        RatingProductDetails = findViewById(R.id.RatingProductDetails)
        RecomRecView_ProductDetailsPage = findViewById(R.id.RecomRecView_ProductDetailsPage)
        backIv_ProfileFrag = findViewById(R.id.backIv_ProfileFrag)
        val addToCart_ProductDetailsPage: Button = findViewById(R.id.addToCart_ProductDetailsPage)

        tryAtHomeButton = findViewById(R.id.try_at_home_button)
        setupARViewing()
        cardNumber = GetDefCard()


       


        newProduct = arrayListOf()
        setProductData()
        setRecData()

        RecomRecView_ProductDetailsPage.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
        RecomRecView_ProductDetailsPage.setHasFixedSize(true)
        newProductAdapter = ProductAdapter(newProduct, this)
        RecomRecView_ProductDetailsPage.adapter = newProductAdapter

        backIv_ProfileFrag.setOnClickListener {
            onBackPressed()
        }

        addToCart_ProductDetailsPage.setOnClickListener {

            val bottomSheetDialod = BottomSheetDialog(
                this, R.style.BottomSheetDialogTheme
            )

            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.fragment_add_to_bag,
                findViewById<ConstraintLayout>(R.id.bottomSheet)
            )

            bottomSheetView.findViewById<View>(R.id.addToCart_BottomSheet).setOnClickListener {

                pPrice *= bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom).text.toString()
                    .toInt()
                addProductToBag()
                bottomSheetDialod.dismiss()
            }

            bottomSheetView.findViewById<LinearLayout>(R.id.minusLayout).setOnClickListener {
                if (bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom).text.toString()
                        .toInt() > 1
                ) {
                    qua--
                    bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom)
                        .setText(qua.toString())
                }
            }

            bottomSheetView.findViewById<LinearLayout>(R.id.plusLayout).setOnClickListener {
                if (bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom).text.toString()
                        .toInt() < 10
                ) {
                    qua++
                    bottomSheetView.findViewById<EditText>(R.id.quantityEtBottom)
                        .setText(qua.toString())
                }
            }

            bottomSheetDialod.setContentView(bottomSheetView)
            bottomSheetDialod.show()
        }

    }

    private fun initializeViews() {
        productImage_ProductDetailsPage = findViewById(R.id.productImage_ProductDetailsPage)
        productName_ProductDetailsPage = findViewById(R.id.productName_ProductDetailsPage)
        productPrice_ProductDetailsPage = findViewById(R.id.productPrice_ProductDetailsPage)
        productBrand_ProductDetailsPage = findViewById(R.id.productBrand_ProductDetailsPage)
        productDes_ProductDetailsPage = findViewById(R.id.productDes_ProductDetailsPage)
        productRating_singleProduct = findViewById(R.id.productRating_singleProduct)
        RatingProductDetails = findViewById(R.id.RatingProductDetails)
        RecomRecView_ProductDetailsPage = findViewById(R.id.RecomRecView_ProductDetailsPage)
        backIv_ProfileFrag = findViewById(R.id.backIv_ProfileFrag)
        tryAtHomeButton = findViewById(R.id.try_at_home_button)
    }

    private fun setProductData() {
        Glide.with(applicationContext)
            .load(product.productImage)
            .into(productImage_ProductDetailsPage)

        productName_ProductDetailsPage.text = product.productName
        productPrice_ProductDetailsPage.text = "₹${product.productPrice}"
        productBrand_ProductDetailsPage.text = product.productBrand
        productDes_ProductDetailsPage.text = product.productDes
        productRating_singleProduct.rating = product.productRating
        RatingProductDetails.text = "${product.productRating} Rating on this Product."

        // Store values for cart
        pName = product.productName
        pPrice = product.productPrice.toInt()
        pPid = product.productId
        pImage = product.productImage
        modelURL = product.modelURL
    }

    private fun addProductToBag() {

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)

        cartViewModel.insert(ProductEntity(pName, qua, pPrice, pPid, pImage))
        toast("Add to Bag Successfully")
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



    private fun setRecData() {
        // Always load recommendations from CoverProducts.json regardless of where the user came from
        val jsonFileString = this.let {
            getJsonData(it, "NewProducts.json")  // Always use CoverProducts.json for recommendations
        }
        val gson = Gson()

        val listCoverType = object : TypeToken<List<Product>>() {}.type

        var coverD: List<Product> = gson.fromJson(jsonFileString, listCoverType)

        coverD.forEachIndexed { idx, person ->
            if (idx < 9) {
                newProduct.add(person)
            }
        }
    }

    private fun setupARViewing() {
        tryAtHomeButton.setOnClickListener {
            launchARViewer()
        }
    }

    private fun launchARViewer() {
        try {
            val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
            val intentUri = Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                .appendQueryParameter("file", modelURL) // Using product image URL as model URL for demo
                .appendQueryParameter("mode", "ar_preferred")
                .appendQueryParameter("title", pName)
                .appendQueryParameter("resizable", "false")
                .build()

            sceneViewerIntent.data = intentUri
            sceneViewerIntent.setPackage("com.google.ar.core")

            startActivity(sceneViewerIntent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "AR Viewer not available on this device",
                Toast.LENGTH_SHORT
            ).show()
        }


    }
}


