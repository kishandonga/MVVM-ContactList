package com.mvvm.contactlist.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mvvm.contactlist.databinding.ListRowSearchCategoryBinding;
import com.mvvm.contactlist.db.entities.CategoryEntity;
import com.mvvm.contactlist.viewmodel.ContactListViewModel;

public class SearchCategoryAdapter extends ListAdapter<CategoryEntity, SearchCategoryAdapter.CategoryListViewHolder> {

    private ContactListViewModel viewModel;

    /**
     * @param diffCallback DiffUtil.ItemCallback<CategoryEntity> CategoryEntity model comparison
     * @param viewModel    ContactListViewModel for the click event bind with adapter
     */
    public SearchCategoryAdapter(@NonNull DiffUtil.ItemCallback<CategoryEntity> diffCallback, ContactListViewModel viewModel) {
        super(diffCallback);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public CategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryListViewHolder(ListRowSearchCategoryBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListViewHolder holder, int position) {
        holder.bindTo(getItem(position), viewModel);
    }

    public static class DiffCallback extends DiffUtil.ItemCallback<CategoryEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull CategoryEntity oldItem, @NonNull CategoryEntity newItem) {
            return oldItem.getCategoryId() == newItem.getCategoryId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CategoryEntity oldItem, @NonNull CategoryEntity newItem) {
            return oldItem.compareTo(newItem) > 0;
        }
    }

    static class CategoryListViewHolder extends RecyclerView.ViewHolder {

        private ListRowSearchCategoryBinding binding;

        /**
         * @param itemView ListRowSearchCategoryBinding list_row_search_category
         */
        CategoryListViewHolder(@NonNull ListRowSearchCategoryBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        /**
         * @param entity    CategoryEntity bind with list row
         * @param viewModel ContactListViewModel bind with list row
         */
        void bindTo(CategoryEntity entity, ContactListViewModel viewModel) {
            binding.setModel(entity);
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }
}
