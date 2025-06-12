package com.connect.eduhubconnect.dto

data class AddressDTO(
    val city: String,
    val country: String,
    val id: Int,
    val pincode: String,
    val state: String,
    val street: String,
    val userId: Int
)