package com.mvvm.contactlist.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mvvm.contactlist.databinding.ListRowContactBinding;
import com.mvvm.contactlist.db.entities.ContactEntity;
import com.mvvm.contactlist.viewmodel.ContactListViewModel;

public class ContactListAdapter extends ListAdapter<ContactEntity, ContactListAdapter.ContactListViewHolder> {

    private final ContactListViewModel viewModel;

    /**
     * @param diffCallback DiffUtil.ItemCallback<ContactEntity> ContactEntity comparison
     * @param viewModel    ContactListViewModel for the click event bind with adapter
     */
    public ContactListAdapter(@NonNull DiffUtil.ItemCallback<ContactEntity> diffCallback, ContactListViewModel viewModel) {
        super(diffCallback);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactListViewHolder(ListRowContactBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListViewHolder holder, int position) {
        holder.bindTo(getItem(position), viewModel);
    }

    public static class DiffCallback extends DiffUtil.ItemCallback<ContactEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull ContactEntity oldItem, @NonNull ContactEntity newItem) {
            return oldItem.getCategoryId() == newItem.getCategoryId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ContactEntity oldItem, @NonNull ContactEntity newItem) {
            return oldItem.compareTo(newItem) > 0;
        }
    }

    static class ContactListViewHolder extends RecyclerView.ViewHolder {

        private final ListRowContactBinding binding;

        /**
         * @param itemView ListRowContactBinding list_row_contact
         */
        ContactListViewHolder(@NonNull ListRowContactBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        /**
         * @param entity    ContactEntity bind with list row
         * @param viewModel ContactListViewModel bind with list row
         */
        void bindTo(ContactEntity entity, ContactListViewModel viewModel) {
            binding.setModel(entity);
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }
}
