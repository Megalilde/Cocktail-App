package com.example.mycocktail.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.mycocktail.R
import com.example.mycocktail.databinding.FragmentDrinkDetailBinding
import com.example.mycocktail.ui.viewmodel.DrinkViewModel
import com.example.mycocktail.utils.Constants
import com.squareup.picasso.Picasso

class DrinkDetailFragment : Fragment() {
    private var id: String? = null
    private var name: String? = null
    private val viewModel: DrinkViewModel by activityViewModels()
    private var _binding: FragmentDrinkDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString(Constants.ID_DRINK)
            name = it.getString(Constants.NAME_DRINK)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrinkDetailBinding.inflate(inflater,container,false)
        fetchDrinkDetails()
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActionBar()
        setupUiFragment()
    }

    // Scrittura dell'observe senza la recycleview
    private fun setupUiFragment(){
        viewModel.drinkDetail.observe(viewLifecycleOwner){ result ->
            result.fold(
                onSuccess = { drink ->
                    Picasso.get().load(drink?.get(0)?.strDrinkThumb.toString()).into(_binding?.itemImage)
                    _binding?.itemText?.text = drink?.get(0)?.strDrink
                    _binding?.itemTextInstruction?.text = drink?.get(0)?.strInstructions
                    _binding?.itemTextIngredient1?.text = drink?.get(0)?.strIngredient1
                    _binding?.itemTextIngredient2?.text = drink?.get(0)?.strIngredient2
                    _binding?.itemTextIngredient3?.text = drink?.get(0)?.strIngredient3

                },
                onFailure = {
                    if(id != null){
                        val errorFragment = ErrorFragment.newInstance {
                            id?.let { it1 -> viewModel.fetchDrinkByIdFromRepository(it1) }
                        }
                        (activity as? MainActivity)?.replaceFragment(errorFragment)
                    }else{
                        val errorFragment = ErrorFragment.newInstance {
                                name?.let { it1 -> viewModel.fetchDrinkByNameFromRepository(it1) }
                        }
                        (activity as? MainActivity)?.replaceFragment(errorFragment)
                    }

                }
            )
        }
    }


    private fun fetchDrinkDetails(){
        if(id != null && (viewModel.drinkDetail.value?.getOrNull()?.get(0)?.idDrink != id || viewModel.drinkDetail.value?.getOrNull().isNullOrEmpty())) {
            id?.let { viewModel.fetchDrinkByIdFromRepository(it) }
        }
        else if(name != null && (viewModel.drinkDetail.value?.getOrNull()?.get(0)?.strDrink != name || viewModel.drinkDetail.value?.getOrNull().isNullOrEmpty())){
            name?.let { viewModel.fetchDrinkByNameFromRepository(it)}
        }
    }


    private fun setupActionBar(){
        (activity as? AppCompatActivity)?.setSupportActionBar(_binding?.toolbarDetailFragment)

        val actionBar = (activity as? AppCompatActivity)?.supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        _binding?.toolbarDetailFragment?.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(id: String? = null, name: String? = null) =
            DrinkDetailFragment().apply {
                arguments = Bundle().apply {
                    id?.let { putString(Constants.ID_DRINK, it) }
                    name?.let { putString(Constants.NAME_DRINK, it) }
                }
            }
    }
}