package com.naveed.drinkRecipes.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.OnProgressListener
import com.downloader.PRDownloader
import com.downloader.Progress
import com.naveed.drinkRecipes.R
import com.naveed.drinkRecipes.ui.adapter.HomeDrinksAdapter
import com.naveed.drinkRecipes.databinding.FragmentHomeBinding
import com.naveed.drinkRecipes.interfaceRcv.OnFavouriteClickListener
import com.naveed.drinkRecipes.model.DrinkList
import com.naveed.drinkRecipes.otherClasses.TinyDB
import com.naveed.drinkRecipes.viewModel.DrinksViewModel
import com.naveed.drinkRecipes.viewModel.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL


@AndroidEntryPoint
class HomeFragment : Fragment(), SearchView.OnQueryTextListener, OnFavouriteClickListener {


    private val mViewModel: DrinksViewModel by viewModels()
    private val mFavViewModel: FavouriteViewModel by viewModels()

    var binding: FragmentHomeBinding? = null

    var searchByName: Boolean = true

    var drinkList: DrinkList? = null

    lateinit var adapter: HomeDrinksAdapter

    lateinit var tinyDB: TinyDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding?.root;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tinyDB = TinyDB(requireActivity())
        mViewModel.getListByName("Margarita").observe(requireActivity()) {
            if (it?.drinks != null) {
                if (isAdded) {
                    drinkList = it
                    adapter = HomeDrinksAdapter(requireContext(), it, this, mFavViewModel)
                    binding?.drinksRecyclerview?.adapter = adapter
                }
            }
            else
            {
                Toast.makeText(requireContext() , "Unable To Fetch" ,Toast.LENGTH_SHORT ).show()
            }
        }

        binding?.searchView?.setOnQueryTextListener(this)

        if(tinyDB.getBoolean("IsFirstTime"))
        {
            if (tinyDB.getBoolean("isLastSearchByName")) {
                binding?.radioGroup?.check(R.id.radioName)
                binding?.searchView?.queryHint = "Margarita"

            } else {
                binding?.radioGroup?.check(R.id.radioAlphabet)
                binding?.searchView?.queryHint = "Type Alphabet only e.g a"
            }

        }
        else
        {
            tinyDB.putBoolean("IsFirstTime",true)
            tinyDB.putBoolean("isLastSearchByName" , true)
        }

        binding?.radioGroup?.setOnCheckedChangeListener { _, id ->


            if (id == R.id.radioName) {
                searchByName = true
                tinyDB.putBoolean("isLastSearchByName", true)
                binding?.searchView?.queryHint = "Margarita"
            } else if (id == R.id.radioAlphabet) {
                searchByName = false
                tinyDB.putBoolean("isLastSearchByName", false)
                binding?.searchView?.queryHint = "Type Alphabet only e.g a"

            }
            Log.e("CheckBox", "onViewCreated: $searchByName")
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        mViewModel.getListByName(query!!).observe(requireActivity()) {
            if (it?.drinks != null) {
                if (isAdded) {
                    drinkList = it
                    adapter = HomeDrinksAdapter(requireContext(), it, this, mFavViewModel)
                    binding?.drinksRecyclerview?.adapter = adapter
                }
            }
        }
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {

        //Second Option is that we can limit the edit text by applying input Filter
        //but that approach does not sound good to me so i did this.
        if (!searchByName) {
            if (query?.length!! <= 1) {
                mViewModel.getListByAlphabet(query).observe(requireActivity()) {
                    if (it != null && it.drinks.isNotEmpty()) {
                        if (isAdded) {
                            drinkList = it
                            adapter = HomeDrinksAdapter(requireContext(), it, this, mFavViewModel)
                            binding?.drinksRecyclerview?.adapter = adapter
                        }
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "You Selected Search By Alphabet\nPlease Select Search By Name",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return false
    }

    override fun onFavouriteItemClickListener(pos: Int): Boolean {
        GlobalScope.launch(Dispatchers.Main) {


            PRDownloader.download(drinkList?.drinks!![pos].image , requireActivity().getExternalFilesDir(null)?.path , "${drinkList?.drinks!![pos].id}.jpg")
                .build().start(object : OnDownloadListener {
                    override fun onDownloadComplete() {


                    }
                    override fun onError(error: Error?) {

                    }

                })
            val pathDownload = requireActivity().getExternalFilesDir(null)?.path + "/${drinkList?.drinks!![pos].id}.jpg"
            val model = drinkList?.drinks!![pos]
            model.apply {
                imageOffline = pathDownload
            }
            mFavViewModel.insertFavouriteDrink(model)
            Toast.makeText(
                requireContext(),
                "Added To Favourite",
                Toast.LENGTH_SHORT
            ).show()
            if (isAdded) {
                adapter =
                    HomeDrinksAdapter(
                        requireContext(),
                        drinkList!!,
                        this@HomeFragment,
                        mFavViewModel
                    )
                binding?.drinksRecyclerview?.adapter = adapter
            }


        }



        return true
    }

    override fun onRemoveFavouriteClickListener(pos: Int): Boolean {
        GlobalScope.launch(Dispatchers.Main) {

            mFavViewModel.deleteRow(drinkList?.drinks!![pos])

            Toast.makeText(
                requireContext(),
                "Removed From Favourites",
                Toast.LENGTH_SHORT
            ).show()
            adapter =
                HomeDrinksAdapter(requireContext(), drinkList!!, this@HomeFragment, mFavViewModel)
            binding?.drinksRecyclerview?.adapter = adapter

        }



        return true
    }
}