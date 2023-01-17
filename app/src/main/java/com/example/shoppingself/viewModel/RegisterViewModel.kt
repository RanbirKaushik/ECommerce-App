package com.example.shoppingself.viewModel

import androidx.lifecycle.ViewModel
import com.example.shoppingself.data.User
import com.example.shoppingself.utils.*
import com.example.shoppingself.utils.Constants.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db : FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resources<User>>(Resources.Unspecified())
    val register: StateFlow<Resources<User>> = _register

    private val _validation = Channel<RegisterFeildValidation>()

    val validation = _validation.receiveAsFlow()

    fun createUserWithEmailAndPass(user: User, password: String) {

        if (checkValidation(user, password)) {


            runBlocking {
                _register.emit(Resources.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener { it ->

                    it.user?.let {
                        saveUserInfo(it.uid,user)
                        //_register.value = Resources.Success(it)
                    }
                }
                .addOnFailureListener {

                    _register.value = Resources.Error(it.message.toString())
                }
        } else {
            val registerFeildValidation = RegisterFeildValidation(
                validateEmail(user.email),
                validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFeildValidation)
            }
        }
    }

    private fun saveUserInfo(userId: String,user: User) {

        db.collection(USER_COLLECTION)
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resources.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resources.Error(it.message.toString())
            }

    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)

        val passwordValidation = validatePassword(password)

        val shouldRegister = emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success

        return shouldRegister
    }

}