package com.mvvm.contactlist.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mvvm.contactlist.R
import com.mvvm.contactlist.databinding.FragmentContactListBinding
import com.mvvm.contactlist.db.entities.ContactEntity
import com.mvvm.contactlist.utilities.Const
import com.mvvm.contactlist.utilities.InjectorUtils.provideContactListViewModelFactory
import com.mvvm.contactlist.viewmodel.ContactListViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Locale

class ContactListFragment : Fragment() {
    private lateinit var binding: FragmentContactListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = provideContactListViewModelFactory(requireContext())
        val contactListViewModel =
            ViewModelProvider(this, factory)[ContactListViewModel::class.java]
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = contactListViewModel
        contactListViewModel.setContactInAdapter()
        contactListViewModel.setCategoryInAdapter()

        contactListViewModel.toUpdateContact.observe(viewLifecycleOwner) { integer: Int ->
            val bundle = Bundle()
            bundle.putInt(Const.CONTACT_ID, integer)
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.nav_add_contact, bundle)
        }

        contactListViewModel.searchText.observe(viewLifecycleOwner) { s: String ->
            contactListViewModel.addDisposable(
                Observable.fromIterable(contactListViewModel.contactEntities)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter { entity: ContactEntity ->
                        entity.firstName.lowercase(Locale.getDefault()).contains(
                            s.lowercase(Locale.getDefault())
                        ) || entity.lastName.lowercase(Locale.getDefault())
                            .contains(s.lowercase(Locale.getDefault()))
                    }
                    .toList()
                    .subscribe(
                        { models ->
                            contactListViewModel.adapter.submitList(models)
                        }
                    ) { throwable: Throwable? ->
                        Log.e(
                            Const.TAG,
                            Log.getStackTraceString(throwable)
                        )
                    })
        }

        /*
        //TODO: NavigationUI with Drawer toggle not working, this is bug when used below code for the menu
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.contact_list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_filter -> binding.elCategory.toggle()
                    R.id.action_search -> binding.elSearch.toggle()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)*/

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.contact_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> binding.elCategory.toggle()
            R.id.action_search -> binding.elSearch.toggle()
        }
        return super.onOptionsItemSelected(item)
    }
}