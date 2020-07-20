package com.mvvm.contactlist.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.mvvm.contactlist.R;
import com.mvvm.contactlist.databinding.FragmentContactListBinding;
import com.mvvm.contactlist.utilities.Const;
import com.mvvm.contactlist.utilities.InjectorUtils;
import com.mvvm.contactlist.viewmodel.ContactListViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactListFragment extends Fragment {

    private FragmentContactListBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ContactListViewModel.ContactListViewModelFactory factory = InjectorUtils.provideContactListViewModelFactory(requireContext());
        ContactListViewModel contactListViewModel = new ViewModelProvider(this, factory).get(ContactListViewModel.class);

        binding = FragmentContactListBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewModel(contactListViewModel);

        contactListViewModel.setContactInAdapter();
        contactListViewModel.setCategoryInAdapter();

        contactListViewModel.getToUpdateContact().observe(getViewLifecycleOwner(), integer -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Const.CONTACT_ID, integer);
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.nav_add_contact, bundle);
        });

        contactListViewModel.getSearchText().observe(getViewLifecycleOwner(), s ->
                contactListViewModel.addDisposable(
                        Observable.fromIterable(contactListViewModel.getContactEntities())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .filter(entity -> entity.getFirstName().toLowerCase().contains(s.toLowerCase()) ||
                                        entity.getLastName().toLowerCase().contains(s.toLowerCase()))
                                .toList()
                                .subscribe(models -> {
                                            contactListViewModel.getAdapter().submitList(models);
                                            contactListViewModel.getAdapter().notifyDataSetChanged();
                                        },
                                        throwable -> Log.e(Const.TAG, Log.getStackTraceString(throwable)))));

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.contact_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                binding.elCategory.toggle();
                break;
            case R.id.action_search:
                binding.elSearch.toggle();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}