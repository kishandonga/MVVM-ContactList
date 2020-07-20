package com.mvvm.contactlist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.mvvm.contactlist.databinding.FragmentAddCategoryBinding;
import com.mvvm.contactlist.utilities.InjectorUtils;
import com.mvvm.contactlist.viewmodel.CategoryViewModel;

public class AddCategoryFragment extends Fragment {

    private FragmentAddCategoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CategoryViewModel.AddCategoryViewModelFactory factory = InjectorUtils.provideAddCategoryViewModelFactory(requireContext());
        CategoryViewModel categoryViewModel = new ViewModelProvider(this, factory).get(CategoryViewModel.class);

        binding = FragmentAddCategoryBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(categoryViewModel);

        categoryViewModel.getErrorSuccMessage().observe(getViewLifecycleOwner(), integer ->
                Snackbar.make(binding.getRoot(), getString(integer), Snackbar.LENGTH_SHORT).show());

        categoryViewModel.setCategoryInAdapter();

        return binding.getRoot();
    }
}