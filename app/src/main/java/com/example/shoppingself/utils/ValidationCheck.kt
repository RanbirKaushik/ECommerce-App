package com.example.shoppingself.utils


import android.util.Patterns

fun validateEmail(email:String) : RegisterValidation {

    if (email.isEmpty()){
        return RegisterValidation.Failed("Email can't be empty...")
    }


    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return RegisterValidation.Failed("Wrong Email format...")
    }
    else
        return RegisterValidation.Success
}


fun validatePassword(password:String) : RegisterValidation{
    if (password.isEmpty()){
        return RegisterValidation.Failed("Password can't be empty")
    }
    if (password.length<6){
        return RegisterValidation.Failed("Length should be greater than 6")
    }
    else
        return RegisterValidation.Success
}