package com.bn.meow.ui.screens

import androidx.lifecycle.viewModelScope
import com.bn.meow.data.models.CatsResponseItem
import com.bn.meow.data.network.Resource
import com.bn.meow.domain.usecase.GetCatsUseCase
import com.bn.meow.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getCatsUseCase: GetCatsUseCase
) : BaseViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()


    private val _cats = MutableStateFlow<Resource<ArrayList<CatsResponseItem>>?>(null)
    val cats: StateFlow<Resource<ArrayList<CatsResponseItem>>?> = _cats

    override fun setStateEvent(state: AllStateEvent) {
        CoroutineScope(Dispatchers.IO).launch(handlerException) {
            when (state) {
                is MainStateEvent.GetCats -> {
                    viewModelScope.launch {
                        _cats.value = Resource.Loading
                        _cats.value = getCatsUseCase.cats(10)
                       when(_cats.value){
                           is Resource.Failure -> _isRefreshing.emit(false)
                           Resource.Loading -> {
                               _isRefreshing.emit(true)
                           }
                           is Resource.Success -> _isRefreshing.emit(false)
                           else -> {_isRefreshing.emit(false)}
                       }

                    }
                }
            }
        }
    }

}

sealed class MainStateEvent : BaseViewModel.AllStateEvent() {
    object GetCats : MainStateEvent()
}