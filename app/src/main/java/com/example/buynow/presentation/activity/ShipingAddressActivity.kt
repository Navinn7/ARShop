package com.example.buynow.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buynow.R
import com.example.buynow.data.model.ShippingAddress
import com.example.buynow.presentation.adapter.AddressAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.view.LayoutInflater
import android.widget.EditText

class ShipingAddressActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shiping_address)

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize RecyclerView and Adapter
        setupRecyclerView()

        // Initialize FAB and set click listener
        val addAddressButton = findViewById<FloatingActionButton>(R.id.addAddress_ShippingPage)
        addAddressButton.setOnClickListener {
            showAddAddressDialog()
        }

        // Load addresses
        loadAddresses()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.addressesRecyclerView)
        addressAdapter = AddressAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ShipingAddressActivity)
            adapter = addressAdapter
        }
    }

    private fun loadAddresses() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("shipping_addresses")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        showToast("Error loading addresses: ${error.message}")
                        return@addSnapshotListener
                    }

                    val addressList = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(ShippingAddress::class.java)
                    } ?: listOf()

                    addressAdapter.updateAddresses(addressList)
                }
        }
    }

    private fun showAddAddressDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_address, null)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Add Shipping Address")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                // Get references to EditText fields
                val fullName = dialogView.findViewById<EditText>(R.id.etFullName).text.toString()
                val streetAddress = dialogView.findViewById<EditText>(R.id.etStreetAddress).text.toString()
                val city = dialogView.findViewById<EditText>(R.id.etCity).text.toString()
                val state = dialogView.findViewById<EditText>(R.id.etState).text.toString()
                val zipCode = dialogView.findViewById<EditText>(R.id.etZipCode).text.toString()
                val phoneNumber = dialogView.findViewById<EditText>(R.id.etPhoneNumber).text.toString()

                if (validateInputs(fullName, streetAddress, city, state, zipCode, phoneNumber)) {
                    saveAddressToFirebase(
                        ShippingAddress(
                            id = "",
                            fullName = fullName,
                            streetAddress = streetAddress,
                            city = city,
                            state = state,
                            zipCode = zipCode,
                            phoneNumber = phoneNumber,
                            userId = auth.currentUser?.uid ?: ""
                        )
                    )
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun validateInputs(
        fullName: String,
        streetAddress: String,
        city: String,
        state: String,
        zipCode: String,
        phoneNumber: String
    ): Boolean {
        return when {
            fullName.isEmpty() -> {
                showToast("Please enter full name")
                false
            }
            streetAddress.isEmpty() -> {
                showToast("Please enter street address")
                false
            }
            city.isEmpty() -> {
                showToast("Please enter city")
                false
            }
            state.isEmpty() -> {
                showToast("Please enter state")
                false
            }
            zipCode.isEmpty() -> {
                showToast("Please enter ZIP code")
                false
            }
            phoneNumber.isEmpty() -> {
                showToast("Please enter phone number")
                false
            }
            else -> true
        }
    }

    private fun saveAddressToFirebase(address: ShippingAddress) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            showToast("Please login first")
            return
        }

        // Create a new document reference with auto-generated ID
        val newAddressRef = db.collection("shipping_addresses").document()

        // Add the document ID to the address object
        val addressWithId = address.copy(id = newAddressRef.id)

        // Save to Firebase
        newAddressRef.set(addressWithId)
            .addOnSuccessListener {
                showToast("Address saved successfully")
            }
            .addOnFailureListener { e ->
                showToast("Error saving address: ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}