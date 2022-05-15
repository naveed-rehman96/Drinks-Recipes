package com.naveed.drinkRecipes.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.naveed.drinkRecipes.ui.adapter.FavouriteDrinksAdapter
import com.naveed.drinkRecipes.databinding.FragmentFavouriteBinding
import com.naveed.drinkRecipes.interfaceRcv.OnFavouriteClickListener
import com.naveed.drinkRecipes.model.Drinks
import com.naveed.drinkRecipes.viewModel.FavouriteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FavouriteFragment : Fragment() , OnFavouriteClickListener {

    private var mFavViewModel: FavouriteViewModel ?= null
    var drinkList: ArrayList<Drinks> = ArrayList()

    lateinit var adapter: FavouriteDrinksAdapter

    var binding : FragmentFavouriteBinding ?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        mFavViewModel = ViewModelProvider(requireActivity())[FavouriteViewModel::class.java]
        return binding?.root;
    }
    private fun isNetworkConnected(): Boolean {
        val cm = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mFavViewModel?.mAllRecords?.observe(requireActivity())  {
            if (it != null) {
                if (isAdded) {
                    drinkList = it as ArrayList<Drinks>
                    adapter = FavouriteDrinksAdapter(requireContext(), it, this , isNetworkConnected())
                    binding?.drinksRecyclerview?.adapter = adapter
                }
            }
        }

    }

    override fun onFavouriteItemClickListener(pos: Int): Boolean {

        return true
    }

    override fun onRemoveFavouriteClickListener(pos: Int): Boolean {

        GlobalScope.launch(Dispatchers.Main) {

            mFavViewModel?.deleteRow(drinkList[pos])
            drinkList.removeAt(pos)
            Toast.makeText(
                requireContext(),
                "Removed From Favourites",
                Toast.LENGTH_SHORT
            ).show()
            adapter =
                FavouriteDrinksAdapter(requireContext(), drinkList!!, this@FavouriteFragment,isNetworkConnected())
            binding?.drinksRecyclerview?.adapter = adapter

        }

        return true
    }
}