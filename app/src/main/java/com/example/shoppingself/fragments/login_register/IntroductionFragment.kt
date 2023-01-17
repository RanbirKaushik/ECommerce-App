package com.example.shoppingself.fragments.login_register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shoppingself.R
import com.example.shoppingself.ShoppingActivity
import com.example.shoppingself.databinding.FragmentIntroductionBinding
import com.example.shoppingself.viewModel.IntroductionViewModel
import com.example.shoppingself.viewModel.IntroductionViewModel.Companion.ACCOUNT_OPTION_FRAGMENT
import com.example.shoppingself.viewModel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_introduction) {

    private lateinit var binding: FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.navigate.collect{

                when(it){
                    SHOPPING_ACTIVITY ->{
                        Intent(requireActivity(),ShoppingActivity::class.java).also {   intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }

                    }
                    ACCOUNT_OPTION_FRAGMENT ->{
                        findNavController().navigate(it)

                    }
                    else -> Unit
                }
            }

        }



        binding.btnFirstscreen.setOnClickListener {
            viewModel.buttonClicked()
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionFragment)
        }



    }

}