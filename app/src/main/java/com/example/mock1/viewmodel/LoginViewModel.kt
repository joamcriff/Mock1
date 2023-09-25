package com.example.mock1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mock1.Model.UsersModel
import com.example.mock1.data.repository.UserRepository
import io.grpc.internal.SharedResourceHolder.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _userLiveData = MutableLiveData<Resource<UsersModel>>()
    val userLiveData: LiveData<Resource<UsersModel>> = _userLiveData

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.signInWithEmailAndPassword(email, password)
            _userLiveData.value = result
        }
    }

    class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
                return LoginViewModel(repository) as T
            }
            throw IllegalAccessException("Unable construct viewModel")
        }
    }
}
