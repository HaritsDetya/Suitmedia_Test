package com.example.km_test.network

data class UserResponse(
    val data: List<User>,
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int
)

data class User(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
) {
    fun getFullName(): String = "$first_name $last_name"
}