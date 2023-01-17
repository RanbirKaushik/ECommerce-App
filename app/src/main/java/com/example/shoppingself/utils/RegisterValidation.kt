package com.example.shoppingself.utils

sealed class RegisterValidation(){

    object Success : RegisterValidation()
    data class Failed(val message: String) : RegisterValidation()
}

data class RegisterFeildValidation(
    val email : RegisterValidation,
    val password : RegisterValidation
)
