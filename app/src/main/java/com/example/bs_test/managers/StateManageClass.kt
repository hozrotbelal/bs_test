package com.example.bs_test.managers

import androidx.lifecycle.MutableLiveData
import com.example.bs_test.data.model.Item
import com.example.bs_test.data.network.Resource

class StateManageClass {

    companion object{
        var searchTxt: String = "android"
        var isFromHome = false
        var refreshAllStoryFeel = false
        var orderBy: String = ""
        var sortBy: String = ""
        var itemList = mutableListOf<Item?>()

        fun clear(){
            searchTxt = ""
            isFromHome = false
            sortBy = ""
            orderBy = ""
            itemList.clear()
        }
    }

}