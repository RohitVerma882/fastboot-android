package dev.rohitverma882.fastboot.sample.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import dev.rohitverma882.fastboot.sample.data.FastbootDevice
import dev.rohitverma882.fastboot.sample.data.FastbootDeviceAdapter
import dev.rohitverma882.fastboot.databinding.FragmentMainBinding
import dev.rohitverma882.fastboot.sample.ui.OnFastbootDeviceItemClickListener

class MainFragment : Fragment(), OnFastbootDeviceItemClickListener {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAttachedDevices = binding.rvAttachedDevices
        rvAttachedDevices.setHasFixedSize(true)
        rvAttachedDevices.layoutManager = LinearLayoutManager(context)

        val fastbootDeviceAdapter = FastbootDeviceAdapter()
        fastbootDeviceAdapter.addOnFastbootDeviceItemClickListener(this)
        rvAttachedDevices.adapter = fastbootDeviceAdapter

        viewModel.fastbootDevices.observe(viewLifecycleOwner) {
            fastbootDeviceAdapter.submitList(it)
        }
    }

    override fun onFastbootDeviceItemClick(device: FastbootDevice, view: View) =
        showDeviceDetails(device, view)

    private fun showDeviceDetails(device: FastbootDevice, view: View) {
        val connectAction = MainFragmentDirections.connectAction(device.deviceId)
        view.findNavController().navigate(connectAction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}