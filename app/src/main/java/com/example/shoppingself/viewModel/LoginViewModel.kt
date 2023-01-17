package com.example.shoppingself.viewModel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingself.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel  @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    //so now this time we are going to use mutable shared flow because we will send one time event
    //so mutable shared flow send the one time event to the ui to take action

    private val _login = MutableSharedFlow<Resources<FirebaseUser>>()
    val login = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<Resources<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun loginWithEmailAndPass(email: String,password:String){
        viewModelScope.launch { _login.emit(Resources.Loading()) }
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                viewModelScope.launch {
                    it.user?.let {
                        _login.emit(Resources.Success(it))
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _login.emit(Resources.Error(it.message.toString()))

                }
            }
    }

    fun resetPassword(email: String){
        viewModelScope.launch {
            _resetPassword.emit(Resources.Loading())

            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {

                    viewModelScope.launch {
                        _resetPassword.emit(Resources.Success(email))
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _resetPassword.emit(Resources.Error(it.message.toString()))
                    }

                }
        }

    }


}