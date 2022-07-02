package com.example.bs_test.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


fun ViewModel.mainScope(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch { withContext(Dispatchers.Main) { block() } }

fun ViewModel.ioScope(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch { withContext(Dispatchers.IO) { block() } }