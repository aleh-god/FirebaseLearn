package by.godevelopment.firebaselearn.ui.register

import androidx.lifecycle.ViewModelProvider
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
import by.godevelopment.firebaselearn.databinding.RegisterFragmentBinding
import by.godevelopment.firebaselearn.domain.model.EventState
import by.godevelopment.firebaselearn.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var binding: RegisterFragmentBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.register_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

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
                            Log.i(LOG_KEY, "RegisterFragment alertMessage ${it.alertMessage}")
                            Toast.makeText(context, it.alertMessage, Toast.LENGTH_SHORT).show()
                        }
                        is EventState.Hold -> {}
                    }
                }
            }
        }
    }
}
