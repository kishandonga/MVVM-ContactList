package com.mvvm.contactlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.contactlist.adapter.ContactListAdapter.ContactListViewHolder
import com.mvvm.contactlist.databinding.ListRowContactBinding
import com.mvvm.contactlist.db.entities.ContactEntity
import com.mvvm.contactlist.viewmodel.ContactListViewModel

class ContactListAdapter(
    diffCallback: DiffUtil.ItemCallback<ContactEntity>,
    private val viewModel: ContactListViewModel
) : ListAdapter<ContactEntity, ContactListViewHolder>(diffCallback) {

    override fun submitList(list: MutableList<ContactEntity>?) {
        super.submitList(null)
        super.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        return ContactListViewHolder(ListRowContactBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.bindTo(getItem(position), viewModel)
    }

    class DiffCallback : DiffUtil.ItemCallback<ContactEntity>() {
        override fun areItemsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
            return oldItem > newItem
        }
    }

    class ContactListViewHolder(private val binding: ListRowContactBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bindTo(entity: ContactEntity, viewModel: ContactListViewModel) {
            binding.model = entity
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}