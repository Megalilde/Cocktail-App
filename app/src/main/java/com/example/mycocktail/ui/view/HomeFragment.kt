package com.example.mycocktail.ui.view

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycocktail.databinding.FragmentHomeBinding
import com.example.mycocktail.ui.adapters.CategoryDrinkItemsAdapter
import com.example.mycocktail.ui.adapters.DrinkListItemsAdapter
import com.example.mycocktail.data.datamodels.Drink
import com.example.mycocktail.ui.adapters.SuggestionDrinkItemsAdapter
import com.example.mycocktail.ui.viewmodel.DrinkViewModel
import com.example.mycocktail.utils.SuggestionFilter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: DrinkViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryDrinkAdapter: CategoryDrinkItemsAdapter
    private lateinit var suggestionDrinkAdapter: SuggestionDrinkItemsAdapter
    private lateinit var drinkListAdapter: DrinkListItemsAdapter



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
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        viewModel.initRepoContext(requireContext())
        viewModel.initDatabaseContext(requireContext())
        if (viewModel.drinkList.value?.getOrNull().isNullOrEmpty()){
            viewModel.initMethods()
        }

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupRecycleViews()

    }

    private fun setupAdapters(){
        categoryDrinkAdapter = CategoryDrinkItemsAdapter(requireContext(), mutableListOf())
        suggestionDrinkAdapter = SuggestionDrinkItemsAdapter(requireContext(), mutableListOf())
        drinkListAdapter = DrinkListItemsAdapter(requireContext(), mutableListOf())
        setupAdapterListeners()
    }



    private fun setupAdapterListeners(){
        setupCategoryDrinkAdapterListener()
        setupDrinkListAdapterListener()
        setupSearchViewListener()
        setupSearchViewTextEditListener()
        setupSuggestionDrinkAdapterListener()
        setupScreenListener()

    }

    private fun setupCategoryDrinkAdapterListener(){
        categoryDrinkAdapter.setOnClickListener(object: CategoryDrinkItemsAdapter.OnClickListener{
            override fun onClick(position: Int, type: String) {
                viewModel.changeCategory(type)
                viewModel.loadSuggestionName()
            }
        })
    }

    private fun setupDrinkListAdapterListener(){
        drinkListAdapter.setOnClickListener(object: DrinkListItemsAdapter.OnClickListener{
            override fun onClick(position: Int, id: String) {
                goToDetailFragmentById(id)
            }
        })
        // Todo Prende il modello cliccato e lo aggiunge nelle shared.
        drinkListAdapter.setOnClickImageListener(object: DrinkListItemsAdapter.OnClickImageListener{
            override fun onClick(model: Drink) {
                if (model.isFavorite){
                    viewModel.addFavoriteDrinkFromApi(model)
                    //viewModel.addFavoritesDrinksFromApi(model)
                    //viewModel.addFavoritesDrinks(model)
                }
                else{
                    viewModel.removeFavoriteDrinkFromApi(model)
                    //viewModel.removeFavoritesDrinksFromApi(model)
                    //viewModel.removeFavoritesDrinks(model)
                }

            }
        })
    }

    private fun setupSearchViewListener(){
        // Se premo invio sulla searchbar
        binding.etSearchView.setOnKeyListener{ view, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                // Questi due servono per chiudere la keyboard
                val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                goToDetailFragmentByName(binding.etSearchView.text.toString())
                true
            }else{
                false
            }
        }
    }

    private fun setupSearchViewTextEditListener(){
        binding.etSearchView.addTextChangedListener { text ->
            binding.rcSearchList.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
            val query = text?.toString() ?: ""
            val filteredSuggestions = SuggestionFilter(viewModel).getFilteredSuggestions(query)
            filteredSuggestions?.take(5)?.let { suggestionDrinkAdapter.updateItems(it) }
        }
    }

    private fun setupSuggestionDrinkAdapterListener(){
        // Quando clicco un item della suggestion
        suggestionDrinkAdapter.setOnClickListener(object: SuggestionDrinkItemsAdapter.OnClickListener{
            override fun onClick(position: Int, name: String) {
                goToDetailFragmentByName(name)
                binding.rcSearchList.visibility = View.GONE
            }
        })
    }
    private fun setupScreenListener(){
        // Quando premo qualsiasi parte dello schermo.
        binding.llFragmentHome.setOnClickListener {
            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
            binding.rcSearchList.visibility = View.GONE
        }
    }

    private fun goToDetailFragmentById(id: String){
        val fragment: DrinkDetailFragment = DrinkDetailFragment.newInstance(id,null)
        (activity as? MainActivity)?.replaceFragment(fragment)
    }

    private fun goToDetailFragmentByName(name: String){
        val fragment: DrinkDetailFragment = DrinkDetailFragment.newInstance(null,name)
        (activity as? MainActivity)?.replaceFragment(fragment)
    }

    private fun setupRecycleViews(){
        setupCategoryRecycleView(binding.rvCategoryList, categoryDrinkAdapter, viewModel.categoryDrinks)
        setupSuggestionRecycleView(binding.rcSearchList, suggestionDrinkAdapter, viewModel.suggestionDrink)
        setupRecycleView(binding.rvCocktailList, drinkListAdapter, viewModel.drinkList)
    }


    // Scrittura dell'observe per la recycleview
    private fun setupCategoryRecycleView(recyclerView: RecyclerView, adapter: CategoryDrinkItemsAdapter, drinksLiveData: MutableLiveData<MutableList<String>>){
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        drinksLiveData.observe(viewLifecycleOwner){ drinks ->
            if(drinks != null){
                adapter.updateItems(drinks)
            }else{
                showError("Non sono disponibili i dati.")
            }

        }

    }

    private fun setupSuggestionRecycleView(recyclerView: RecyclerView, adapter: SuggestionDrinkItemsAdapter, drinksLiveData: MutableLiveData<MutableList<String>?>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.visibility = View.GONE

        drinksLiveData.observe(viewLifecycleOwner){ drinks ->
            drinks?.take(5)?.let { adapter.updateItems(it) }
        }
    }



    private fun setupRecycleView(recyclerView: RecyclerView, adapter: DrinkListItemsAdapter, drinksLiveData: MutableLiveData<Result<MutableList<Drink>?>>){
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        drinksLiveData.observe(viewLifecycleOwner){ result ->
            result.fold(
                onSuccess = { drink ->
                    if (drink != null) {
                        adapter.updateItems(drink)
                    }
                },
                onFailure = { _ ->
                    val errorFragment = ErrorFragment.newInstance {
                        viewModel.fetchDrinksFromRepository()
                    }
                    (activity as? MainActivity)?.replaceFragment(errorFragment)
                    adapter.updateItems(mutableListOf())
                }
            )

        }

    }



    override fun onResume() {
        viewModel.fetchDrinksFromRepository()
        binding.rcSearchList.visibility = View.GONE
        super.onResume()
    }
    private fun showError(message: String) {
        // Mostra un messaggio di errore all'utente, ad esempio con un Toast o un dialogo
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}