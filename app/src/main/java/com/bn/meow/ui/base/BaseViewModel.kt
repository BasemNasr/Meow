package com.bn.meow.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected lateinit var launchIn: Job
    protected lateinit var secondIn: Job
    protected lateinit var mAllJob: Job
    private val mRetryList by lazy { ArrayList<AllStateEvent>() }


    val handlerException by lazy {
        CoroutineExceptionHandler { _, ex ->
            viewModelScope.launch {

            }
        }
    }

    abstract fun setStateEvent(state: AllStateEvent)
    open class AllStateEvent {
        object RetryRequest : AllStateEvent()
    }

    @OptIn(InternalCoroutinesApi::class)
    fun addEvent(event: AllStateEvent) {
        synchronized(ArrayList::class.java) {
            val newRequest = mRetryList.find { it == event }
            if (newRequest == null) mRetryList.add(event)
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    fun clearEvent(event: AllStateEvent) {
        synchronized(ArrayList::class.java) {
            val newRequest = mRetryList.find { it == event }
            if (newRequest != null) mRetryList.remove(event)
        }
    }

    fun cancelAllRequests() {
        if (this::launchIn.isInitialized) launchIn.cancel()
        if (this::secondIn.isInitialized) secondIn.cancel()
    }

    fun checkSecondJob(): Boolean {
        return this::secondIn.isInitialized
    }

    fun checkFirstJob(): Boolean {
        return this::launchIn.isInitialized
    }

    override fun onCleared() {
        super.onCleared()
        mRetryList.clear()
        if (::launchIn.isInitialized) launchIn.cancel()
        if (::secondIn.isInitialized) secondIn.cancel()
        if (::mAllJob.isInitialized) mAllJob.cancel()
    }
}