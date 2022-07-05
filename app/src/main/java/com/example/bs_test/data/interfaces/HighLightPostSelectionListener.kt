package com.example.bs_test.data.interfaces

import com.example.bs_test.data.model.Item


interface SearchSelectionListener {

    fun onSearchSelect(searchSelection: Item)

    fun onPagination()

}