package com.example.bs_test.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bs_test.data.model.Item
import com.example.bs_test.data.network.Resource
import com.example.bs_test.data.network.Status
import com.example.bs_test.data.network.api.ApiError
import com.example.bs_test.data.repository.MainRepository
import com.example.bs_test.data.storage.PreferenceStorage
import com.example.bs_test.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    val limit = 10
    var page = 1
    var totalCount = 0
    var itemsCount = 0
    var order: String = ""
    var quary: String = ""
    val sort = ""
    var mIsLoading: Boolean = false
    var mIsLastPage: Boolean = false
    var edit: Boolean = false
    var postList: MutableList<Item> = mutableListOf()
    val commonError = MutableLiveData<String>()
    val messageEvent = MutableLiveData<Resource<String>>()
    val postData = MutableLiveData<Resource<MutableList<Item>>>()


    fun getReposByList(search : String?,sort : String,order : String) {
        mIsLoading = true
        viewModelScope.launch {
            postData.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getGitRepositories(search, sort,order,limit, page).let {
                    if (it.status == Status.SUCCESS) {
                        page++
                        mIsLoading = false
                        it.data?.let { data ->
                            Log.e("getReposByList",data.items!!.toString()+"")
                            totalCount = data.totalCount!!
                            itemsCount += data.items!!.size
                            mIsLastPage = data.items!!.size != limit
                            postList.addAll(data.items)
                            postData.postValue(Resource.success(data.items))
                        }
                    } else {
                        mIsLoading = false
                        postData.postValue(Resource.error(ApiError(it.status.toString(), it.error!!.message, ""), null))
                    }
                }
            } else {
                mIsLoading = false
                postData.postValue(Resource.error(ApiError(code = "", "No internet connection", lang = "")))
            }
        }
    }

}