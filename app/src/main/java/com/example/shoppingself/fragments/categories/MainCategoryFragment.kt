package com.example.shoppingself.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingself.R
import com.example.shoppingself.adapters.BestDealAdapter
import com.example.shoppingself.adapters.BestProductAdapter
import com.example.shoppingself.adapters.SpecialProductAdapter
import com.example.shoppingself.databinding.FragmentMainCategoriesBinding
import com.example.shoppingself.utils.Resources
import com.example.shoppingself.viewModel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_categories){

    private lateinit var binding: FragmentMainCategoriesBinding
    private lateinit var specialProductAdapter : SpecialProductAdapter
    private lateinit var bestProductAdapter: BestProductAdapter
    private lateinit var bestDealAdapter: BestDealAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpecialProductRecyclerView()
        initBestDealRecyclerView()
        initBestProductRecyclerView()
        fetchSpecialProduct()
        fetchBestDealProduct()
        fetchBestProduct()
    }

    private fun fetchBestProduct() {
        lifecycleScope.launch {
            viewModel.bestProduct.collectLatest {
                when(it){
                    is Resources.Loading ->{
                        binding.progressbarbelow.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        bestProductAdapter.differ.submitList(it.data)
                        binding.progressbarbelow.visibility = View.GONE

                    }
                    is Resources.Error ->{
                        binding.progressbarbelow.visibility = View.GONE
                    }
                }
            }
        }

        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ v,_,scroll_Y,_,_ ->

            if (v.getChildAt(0).bottom <= v.height + scroll_Y){
                viewModel.fetchBestProduct()

            }

        })
    }

    private fun fetchBestDealProduct() {
        lifecycleScope.launch{
            viewModel.bestDealProduct.collectLatest {
                when(it){
                    is Resources.Loading ->{
                        showLoading()
                    }
                    is Resources.Success -> {
                        bestDealAdapter.differ.submitList(it.data)
                        hideLoading()

                    }
                    is Resources.Error -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun fetchSpecialProduct() {
        lifecycleScope.launch {
            viewModel.specialProduct.collectLatest {
                when(it){
                    is Resources.Loading ->{
                        showLoading()
                    }
                    is Resources.Success ->{
                        specialProductAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resources.Error ->{
                        hideLoading()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun initBestDealRecyclerView() {
        bestDealAdapter = BestDealAdapter()
        binding.rvBestDeals.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = bestDealAdapter
        }
    }

    private fun initBestProductRecyclerView() {
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProduct.apply {
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = bestProductAdapter
        }
    }

    private fun hideLoading() {
        binding.progressbar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressbar.visibility = View.VISIBLE
    }

    private fun initSpecialProductRecyclerView() {
        specialProductAdapter = SpecialProductAdapter()
        binding.rvSpecialProduct.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = specialProductAdapter
        }

    }
}