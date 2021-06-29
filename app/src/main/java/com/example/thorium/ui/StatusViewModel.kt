package com.example.thorium.ui

import androidx.lifecycle.*
import com.example.thorium.db.entities.Status
import com.example.thorium.repositories.StatusRepository
import kotlinx.coroutines.launch

class StatusViewModel(private val repository: StatusRepository) : ViewModel() {


    val allStatus: LiveData<List<Status>> = repository.allStatus.asLiveData()


    fun insert(status: Status) = viewModelScope.launch {
        repository.insert(status)
    }
}

class StatusViewModelFactory(private val repository: StatusRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatusViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}