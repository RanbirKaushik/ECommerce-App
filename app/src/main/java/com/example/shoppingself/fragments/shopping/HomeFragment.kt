package com.example.shoppingself.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shoppingself.R
import com.example.shoppingself.adapters.HomeViewPagerAdapter
import com.example.shoppingself.databinding.FragmentHomeBinding
import com.example.shoppingself.fragments.categories.*
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home){

    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(

            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoriesFragment(),
            FurnitureFragment()
        )

        binding.viewpagerHome.isUserInputEnabled = false
        val viewPagerAdapter = HomeViewPagerAdapter(categoriesFragment,childFragmentManager,lifecycle)

        binding.viewpagerHome.adapter= viewPagerAdapter


        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){ tab, position->
            when(position){

                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Accessories"
                5 -> tab.text = "Furniture"
            }

        }.attach()
    }

}