package com.example.resqr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resqr.domain.model.authModel.User
import com.example.resqr.domain.model.usermodel.UserResponse
import com.example.resqr.domain.usecase.user.UserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    private val _userState = MutableStateFlow<UserResponse>(UserResponse.Uninitialized)
    val userState: StateFlow<UserResponse> = _userState

    private val _showAlertDialog = MutableStateFlow(false)
    val showAlert: StateFlow<Boolean> = _showAlertDialog

    fun toggleAlertDialog() {
        _showAlertDialog.value = !_showAlertDialog.value
    }


    fun insertUser(user: User) {
        viewModelScope.launch {
            _userState.value = UserResponse.Loading
            userUseCase.insertUser(user).collect { result ->
                _userState.value = result
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            _userState.value = UserResponse.Loading
            userUseCase.updateUser(user).collect { result ->
                _userState.value = result
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            _userState.value = UserResponse.Loading
            userUseCase.deleteUser(id).collect { result ->
                _userState.value = result
            }
        }
    }

    fun getUser() {
        viewModelScope.launch {
            _userState.value = UserResponse.Loading
            userUseCase.getUser().collect { result ->
                _userState.value = result
            }
        }
    }
}