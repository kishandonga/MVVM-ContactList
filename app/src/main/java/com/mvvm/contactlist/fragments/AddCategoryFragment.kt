package com.mvvm.contactlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.mvvm.contactlist.databinding.FragmentAddCategoryBinding
import com.mvvm.contactlist.utilities.InjectorUtils.provideAddCategoryViewModelFactory
import com.mvvm.contactlist.viewmodel.CategoryViewModel

class AddCategoryFragment : Fragment() {
    private lateinit var binding: FragmentAddCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val factory = provideAddCategoryViewModelFactory(requireContext())
        val categoryViewModel = ViewModelProvider(this, factory)[CategoryViewModel::class.java]
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = categoryViewModel
        categoryViewModel.errorSuccMessage.observe(viewLifecycleOwner) { integer: Int ->
            Snackbar.make(
                binding.root, getString(integer), Snackbar.LENGTH_SHORT
            ).show()
        }
        categoryViewModel.setCategoryInAdapter()
        return binding.root
    }
}