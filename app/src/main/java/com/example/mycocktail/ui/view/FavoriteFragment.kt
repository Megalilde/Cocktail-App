package com.example.mycocktail.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycocktail.R
import com.example.mycocktail.databinding.FragmentFavoriteBinding
import com.example.mycocktail.databinding.FragmentHomeBinding
import com.example.mycocktail.ui.adapters.DrinkListFavoriteItemsAdapter
import com.example.mycocktail.ui.viewmodel.DrinkViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: DrinkViewModel by activityViewModels()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var drinkListFavoriteItemsAdapter: DrinkListFavoriteItemsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        viewModel.fetchFavoriteDrinksFromApi()
        //viewModel.fetchFavoriteDrinksFromApi()
        //viewModel.fetchFavoriteDrinks()
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupAdapter()
        setupRecycleView()
        super.onViewCreated(view, savedInstanceState)
    }



    private fun setupAdapter(){
        drinkListFavoriteItemsAdapter = DrinkListFavoriteItemsAdapter(requireContext(), mutableListOf())
        setupListener()
    }

    private fun setupListener(){
        drinkListFavoriteItemsAdapter.setOnClickListener(object: DrinkListFavoriteItemsAdapter.OnClickListener{
            override fun onClick(position: Int, id: String) {
                goToDetailFragmentById(id)
            }
        })
    }

    private fun setupRecycleView(){
        _binding?.rvFavoriteList?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        _binding?.rvFavoriteList?.setHasFixedSize(true)
        _binding?.rvFavoriteList?.adapter = drinkListFavoriteItemsAdapter

        viewModel.favoriteDrinkList.observe(viewLifecycleOwner){ drinks ->
            if(drinks != null){
                drinkListFavoriteItemsAdapter.updateItems(drinks)
            }else{
                Toast.makeText(requireContext(), "Non ci sono dati", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun goToDetailFragmentById(id: String){
        val fragment: DrinkDetailFragment = DrinkDetailFragment.newInstance(id,null)
        (activity as? MainActivity)?.replaceFragment(fragment)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoriteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}