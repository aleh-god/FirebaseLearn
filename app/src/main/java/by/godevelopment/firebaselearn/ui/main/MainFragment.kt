package by.godevelopment.firebaselearn.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(LOG_KEY, "MainFragment onCreate")
        if (viewModel.checkCurrentUser())
            findNavController().navigate(R.id.action_main_fragment_to_home_fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setupTriggerUI()
        return binding.root
    }

    private fun setupTriggerUI() {
        Log.i(LOG_KEY, "MainFragment setupTriggerUI")
        viewLifecycleOwner.lifecycleScope.launch {
            Log.i(LOG_KEY, "MainFragment setupTriggerUI lifecycleScope.launch")
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.i(LOG_KEY, "MainFragment setupTriggerUI lifecycleScope.launch .STARTED")
                viewModel.eventState.collect {
                    Log.i(LOG_KEY, "MainFragment viewModel.eventState.collect $it")
                    when (it) {
                        is EventState.RunNav -> {
                            Log.i(LOG_KEY, "MainFragment findNavController().navigate ${it.destination}")
                            findNavController().navigate(it.destination)
                        }
                        is EventState.Alert -> {
                            Log.i(LOG_KEY, "MainFragment alertMessage ${it.alertMessage}")
                            binding.helloMessage.text = it.alertMessage
                        }
                        is EventState.Hold -> {
                            binding.helloMessage.text = getString(R.string.welcome_message)
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        Log.i(LOG_KEY, "MainFragment onStop()")
        viewModel.resetEventState()
        super.onStop()
    }
}
