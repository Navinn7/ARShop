package com.example.buynow.data.model

data class ShippingAddress(
    val id: String = "",
    val fullName: String = "",
    val streetAddress: String = "",
    val city: String = "",
    val state: String = "",
    val zipCode: String = "",
    val phoneNumber: String = "",
    val userId: String = "" // To associate address with specific user
)