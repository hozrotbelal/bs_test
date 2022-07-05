package com.example.bs_test.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bs_test.R
import com.example.bs_test.data.interfaces.SearchSelectionListener
import com.example.bs_test.data.model.Item
import com.example.bs_test.databinding.BottomFooterViewBinding
import com.example.bs_test.databinding.RowSearchListBinding
import com.example.bs_test.ui.viewmodel.MainViewModel

class SearchAdapter(val context: Context,
                    private val list: MutableList<Item>,
                    private val highLightViewModel: MainViewModel,
                    private val listener: SearchSelectionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val footer: Int = 0
    private val item: Int = 1
    private var stopPagination: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == footer) {
            val binding: BottomFooterViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.bottom_footer_view, parent, false)
            FooterViewHolder(binding)
        } else {
            val binding: RowSearchListBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.row_search_list, parent, false)
            CustomViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (list.size == position) {
            return footer
        }
        return item
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CustomViewHolder) {
//            holder.binding.data = list[position]
//            val isSelected = highLightViewModel.isPostSelected(list[position].id)
//            holder.itemView.tik_mark.isSelected = isSelected
//            holder.binding.foreground.visibility = if (isSelected) View.VISIBLE else View.GONE
//
//            holder.itemView.tik_mark.setSafeOnClickListener {
//                val postDetails = list[position]
//                val selectedPost = SelectedPost(postDetails.id, postDetails.music!!.artwork, postDetails.music.musicName, postDetails.music.artistName, postDetails.publishStartDateTime)
//                listener.onPostSelect(HighLightPostSelection(selectedPost, !holder.itemView.tik_mark.isSelected))
//            }
        } else if (holder is FooterViewHolder) {
            if (stopPagination) {
                holder.binding.parentLayout.visibility = View.GONE
                holder.binding.progressBar.visibility = View.GONE
            } else {
                holder.binding.parentLayout.visibility = View.VISIBLE
                holder.binding.progressBar.visibility = View.VISIBLE
            }
            listener.onPagination()
        }
    }

    inner class CustomViewHolder(layoutHighlightsNonSelectedBinding: RowSearchListBinding) :
        RecyclerView.ViewHolder(layoutHighlightsNonSelectedBinding.root) {
        val binding = layoutHighlightsNonSelectedBinding
    }

    inner class FooterViewHolder(gridFooterViewBinding: BottomFooterViewBinding) :
        RecyclerView.ViewHolder(gridFooterViewBinding.root) {
        val binding = gridFooterViewBinding
    }

//    fun selectionUpdate(highLightPostSelection: Item) {
//        list.forEachIndexed { index, postDetails ->
//            if (postDetails.id == highLightPostSelection.selectedPost.id) {
//                notifyItemChanged(index)
//                return
//            }
//        }
//    }

    fun hasData(): Boolean {
        return list.size != 0
    }

    fun addAll(postList: MutableList<Item>) {
        postList.forEach {
            add(it)
        }
    }

    private fun add(postDetails: Item) {
        list.add(postDetails)
        notifyItemChanged(list.size - 1)
    }

    fun stopPagination(stop: Boolean) {
        stopPagination = stop
    }

}