package dev.rohitverma882.fastboot.sample.ui.devicedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs

import dev.rohitverma882.fastboot.sample.databinding.FragmentDeviceDetailsBinding

class DeviceDetailsFragment : Fragment() {
    private var _binding: FragmentDeviceDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DeviceDetailsViewModel by viewModels()
    private val args: DeviceDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDeviceDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fastbootDevice.observe(viewLifecycleOwner) {
            binding.device = it
        }

        val deviceId = args.deviceId
        viewModel.connectToDevice(deviceId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}