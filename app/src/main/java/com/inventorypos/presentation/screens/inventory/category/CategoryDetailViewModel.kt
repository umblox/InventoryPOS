package com.inventorypos.presentation.screens.inventory.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventorypos.domain.model.Category
import com.inventorypos.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: Long = savedStateHandle.get<Long>("categoryId") ?: 0L

    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category

    init {
        loadCategory()
    }

    private fun loadCategory() {
        viewModelScope.launch {
            _category.value = categoryRepository.getCategoryById(categoryId)
        }
    }
}

