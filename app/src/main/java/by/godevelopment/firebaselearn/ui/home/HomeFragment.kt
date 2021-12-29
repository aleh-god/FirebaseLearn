package by.godevelopment.firebaselearn.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.godevelopment.firebaselearn.R
import by.godevelopment.firebaselearn.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: HomeFragmentBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupTriggerUI()

        return binding.root
    }

    private fun setupTriggerUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.clickEvent.collect {
                    if (it) {
                            findNavController().navigate(R.id.action_home_fragment_to_main_fragment)
//                            onDestroy()
                        }
                }
            }
        }
    }
}