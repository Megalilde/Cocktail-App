package com.example.mycocktail.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.mycocktail.R
import com.example.mycocktail.databinding.FragmentErrorBinding
import com.example.mycocktail.ui.viewmodel.DrinkViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ErrorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ErrorFragment : Fragment(){

    private val viewModel: DrinkViewModel by activityViewModels()
    private var binding: FragmentErrorBinding? = null
    private var retryCallback: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentErrorBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.retryStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                parentFragmentManager.popBackStack()
                requireActivity().findViewById<FrameLayout>(R.id.bn_navigation).visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), "Retry failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        binding?.btnErrorRetry?.setOnClickListener {
            retryCallback?.invoke()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(retryCallback: () -> Unit) : ErrorFragment{
            val fragment = ErrorFragment()
            fragment.retryCallback = retryCallback
            return fragment
        }
    }
}