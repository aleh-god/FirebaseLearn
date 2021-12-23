package by.godevelopment.firebaselearn.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.godevelopment.firebaselearn.R
import by.godevelopment.firebaselearn.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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

        setupUI()
        return binding.root
    }

    private fun setupUI() {
        lifecycleScope.launchWhenCreated {
            viewModel.eventState.collect {
                if (it.runNavReg) {
                    findNavController().navigate(R.id.action_main_fragment_to_registerFragment)
                    onDestroy()     // Не успел за вечер нагуглить как правильно настроить параметры action. Может дестрой сработает?
                }
                if (it.runNavHome) {
                    findNavController().navigate(R.id.action_main_fragment_to_homeFragment)
                    onDestroy()
                }
                if (it.alertMessage != null)
                    Toast.makeText(context, it.alertMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}