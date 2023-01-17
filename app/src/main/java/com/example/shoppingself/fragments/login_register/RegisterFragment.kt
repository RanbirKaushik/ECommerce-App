package com.example.shoppingself.fragments.login_register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shoppingself.data.User
import com.example.shoppingself.databinding.FragmentRegisterBinding
import com.example.shoppingself.utils.RegisterValidation
import com.example.shoppingself.utils.Resources
import com.example.shoppingself.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//when any activity or fragment which is going to use dagger hilt then we have to annonate it with @AndroidEntryPoint
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnRegister.setOnClickListener {
                val user = User(
                    edFirstName.text.toString().trim(),
                    edLastName.text.toString().trim(),
                    edEmail.text.toString().trim()
                )
                val password = edPassword.text.toString()
                viewModel.createUserWithEmailAndPass(user,password)
            }
        }


        lifecycleScope.launchWhenCreated {
            viewModel.register.collect{
                when(it){
                    is Resources.Loading -> {

                        binding.btnRegister.startAnimation()

                    }
                    is Resources.Success -> {

                        Log.d("tag", it.data.toString())
                        binding.btnRegister.revertAnimation()
                    }
                    is Resources.Error -> {
                        Log.d("tag",it.message.toString())
                        binding.btnRegister.revertAnimation()

                    }

                    else -> Unit

                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect{   validation->
                if(validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edEmail.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edPassword.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }



            }
        }
    }

}