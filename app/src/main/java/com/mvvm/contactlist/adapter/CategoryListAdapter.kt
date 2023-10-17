package com.mvvm.contactlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.contactlist.databinding.ListRowCategoryBinding
import com.mvvm.contactlist.db.entities.CategoryEntity
import com.mvvm.contactlist.viewmodel.CategoryViewModel

class CategoryListAdapter(
    diffCallback: DiffUtil.ItemCallback<CategoryEntity>,
    private val viewModel: CategoryViewModel
) : ListAdapter<CategoryEntity, CategoryListAdapter.CategoryListViewHolder>(diffCallback) {

    override fun submitList(list: MutableList<CategoryEntity>?) {
        super.submitList(null)
        super.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(ListRowCategoryBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        holder.bindTo(getItem(position), viewModel)
    }

    class DiffCallback : DiffUtil.ItemCallback<CategoryEntity>() {
        override fun areItemsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
            return oldItem > newItem
        }
    }

    class CategoryListViewHolder(private val binding: ListRowCategoryBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bindTo(entity: CategoryEntity, viewModel: CategoryViewModel) {
            binding.model = entity
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}