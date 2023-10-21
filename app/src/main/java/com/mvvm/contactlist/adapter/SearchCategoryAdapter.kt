package com.mvvm.contactlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.contactlist.databinding.ListRowSearchCategoryBinding
import com.mvvm.contactlist.db.entities.CategoryEntity
import com.mvvm.contactlist.viewmodel.ContactListViewModel

class SearchCategoryAdapter(
    diffCallback: DiffUtil.ItemCallback<CategoryEntity>,
    private val viewModel: ContactListViewModel
) : ListAdapter<CategoryEntity, SearchCategoryAdapter.CategoryListViewHolder>(diffCallback) {

    override fun submitList(list: MutableList<CategoryEntity>?) {
        super.submitList(null)
        super.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(
            ListRowSearchCategoryBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
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

    class CategoryListViewHolder(private val binding: ListRowSearchCategoryBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bindTo(entity: CategoryEntity, viewModel: ContactListViewModel) {
            binding.model = entity
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}