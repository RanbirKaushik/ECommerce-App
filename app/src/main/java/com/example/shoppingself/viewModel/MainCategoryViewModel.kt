package com.example.shoppingself.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingself.data.Product
import com.example.shoppingself.utils.Resources
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _specialProducts =
        MutableStateFlow<Resources<List<Product>>>(Resources.Unspecified())
    val specialProduct: StateFlow<Resources<List<Product>>> = _specialProducts

    private val _bestDealProduct =
        MutableStateFlow<Resources<List<Product>>>(Resources.Unspecified())
    val bestDealProduct: StateFlow<Resources<List<Product>>> = _bestDealProduct

    private val _bestProduct = MutableStateFlow<Resources<List<Product>>>(Resources.Unspecified())
    val bestProduct: StateFlow<Resources<List<Product>>> = _bestProduct

    private val paginingInfo = PaginingInfo()

    init {

        fetchSpecialProduct()
        fetchBestDealProduct()
        fetchBestProduct()

    }

    fun fetchSpecialProduct() {
        viewModelScope.launch {
            _specialProducts.emit(Resources.Loading())
        }

        firestore.collection("Products")
            .whereEqualTo("category", "special")
            .get().addOnSuccessListener { result ->
                val specialProductList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resources.Success(specialProductList))
                }

            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resources.Error(it.message.toString()))
                }
            }

    }

    fun fetchBestDealProduct() {
        viewModelScope.launch {
            _bestDealProduct.emit(Resources.Loading())
        }

        firestore.collection("Products")
            .whereEqualTo("category", "bestDeal")
            .get().addOnSuccessListener { result ->
                val bestDealProductList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDealProduct.emit(Resources.Success(bestDealProductList))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDealProduct.emit(Resources.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProduct() {
        if (!paginingInfo.isPagingEnd) {
            viewModelScope.launch {
                _bestProduct.emit(Resources.Loading())
            }
            firestore.collection("Products")
                .whereEqualTo("category", "best")
                .orderBy(
                    "id",
                    Query.Direction.ASCENDING
                )
                .limit(paginingInfo.page * 3)
                .get().addOnSuccessListener { result ->
                    val bestProductList = result.toObjects(Product::class.java)
                    paginingInfo.isPagingEnd = bestProductList == paginingInfo.oldPagingList
                    paginingInfo.oldPagingList = bestProductList
                    viewModelScope.launch {
                        _bestProduct.emit(Resources.Success(bestProductList))
                    }
                    paginingInfo.page++
                }.addOnFailureListener {
                    viewModelScope.launch {
                        _bestDealProduct.emit(Resources.Error(it.message.toString()))
                    }
                }
        }
    }
}

internal data class PaginingInfo(
    var page: Long = 1,
    var oldPagingList: List<Product> = emptyList(),
    var isPagingEnd: Boolean = false
)