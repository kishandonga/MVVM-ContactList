package com.mvvm.contactlist.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mvvm.contactlist.databinding.ListRowCategoryBinding;
import com.mvvm.contactlist.db.entities.CategoryEntity;
import com.mvvm.contactlist.viewmodel.CategoryViewModel;

public class CategoryListAdapter extends ListAdapter<CategoryEntity, CategoryListAdapter.CategoryListViewHolder> {

    private CategoryViewModel viewModel;

    /**
     * @param diffCallback DiffUtil.ItemCallback<CategoryEntity> CategoryEntity model comparison
     * @param viewModel    CategoryViewModel for the click event bind with adapter
     */
    public CategoryListAdapter(@NonNull DiffUtil.ItemCallback<CategoryEntity> diffCallback, CategoryViewModel viewModel) {
        super(diffCallback);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public CategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryListViewHolder(ListRowCategoryBinding.inflate(LayoutInflater.from(parent.getContext())));
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

        private ListRowCategoryBinding binding;

        /**
         * @param itemView ListRowCategoryBinding lis_row_category
         */
        CategoryListViewHolder(@NonNull ListRowCategoryBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        /**
         * @param entity    CategoryEntity for the category table
         * @param viewModel CategoryViewModel for the category view model
         */
        void bindTo(CategoryEntity entity, CategoryViewModel viewModel) {
            binding.setModel(entity);
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }
}
