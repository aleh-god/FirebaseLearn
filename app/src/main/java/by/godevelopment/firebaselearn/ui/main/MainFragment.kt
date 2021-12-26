package by.godevelopment.firebaselearn.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.godevelopment.firebaselearn.R
import by.godevelopment.firebaselearn.common.LOG_KEY
import by.godevelopment.firebaselearn.databinding.MainFragmentBinding
import by.godevelopment.firebaselearn.domain.model.EventState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModels()

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if (viewModel.checkCurrentUser())
            findNavController().navigate(R.id.action_main_fragment_to_homeFragment)

        setupTriggerUI()
        return binding.root
    }

    private fun setupTriggerUI() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventState.collect {
                    when (it) {
                        is EventState.RunNav -> {
                            findNavController().navigate(it.destination)
                            onDestroy()
                        }
                        is EventState.Alert -> {
                            Log.i(LOG_KEY, "MainFragment alertMessage ${it.alertMessage}")
                            Toast.makeText(context, it.alertMessage, Toast.LENGTH_SHORT).show()
                        }
                        is EventState.Hold -> {}
                    }
                }
            }
        }
    }
}