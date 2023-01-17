package com.example.shoppingself.fragments.login_register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shoppingself.R
import com.example.shoppingself.ShoppingActivity
import com.example.shoppingself.databinding.FragmentLoginBinding
import com.example.shoppingself.utils.Resources
import com.example.shoppingself.viewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnLoginFragment.setOnClickListener {
                val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString()

                viewModel.loginWithEmailAndPass(email, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect{
                when(it){
                    is Resources.Loading ->{

                        binding.btnLoginFragment.startAnimation()

                    }
                    is Resources.Success ->{
                        binding.btnLoginFragment.revertAnimation()
                        Intent(requireActivity(),ShoppingActivity::class.java).also { intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            //we dont't need to go back to login activity from shopping activity...
                        }
                    }
                    is Resources.Error ->{

                        binding.btnLoginFragment.revertAnimation()
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit

                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect{
                when(it){
                    is Resources.Loading ->{



                    }
                    is Resources.Success ->{

                        Snackbar.make(requireView(),"Reset link has been sent to your email",Snackbar.LENGTH_LONG).show()
                    }
                    is Resources.Error ->{
                        Snackbar.make(requireView(),"Error: ${it.message}",Snackbar.LENGTH_LONG).show()


                    }

                    else -> Unit

                }
            }
        }

        binding.tvForgotPassword.setOnClickListener {

            setUpBottomSheetDialog { email->

                viewModel.resetPassword(email)
            }

        }
    }
}