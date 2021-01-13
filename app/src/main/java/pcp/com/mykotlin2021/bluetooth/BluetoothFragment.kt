package pcp.com.mykotlin2021.bluetooth

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import pcp.com.mykotlin2021.R
import pcp.com.mykotlin2021.databinding.FragmentBluetoothBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/*
    Author: Joey
    Study item:
    1. Permission:
     a. Reference website: https://developer.android.com/training/permissions/requesting#kotlin
     b. Finish : Basic permission process
     c. Didn't finish : (in Reference website)
      1) shouldShowRequestPermissionRationale
      2) Handle permission denial
      3) One-time permissions
      4) Auto-reset permissions of unused apps
      5) Request to become the default handler if necessary
 */
/**
 * A simple [Fragment] subclass.
 * Use the [BluetoothFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BluetoothFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val myPermissionsRequestAccessFineLocation = 100;

    private lateinit var binding: FragmentBluetoothBinding
    private lateinit var viewModel: BluetoothViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bluetooth, container, false)
        viewModel = ViewModelProvider(this).get(BluetoothViewModel::class.java)

        binding.btnScan.setOnClickListener { onScan() }
        binding.btnPermission.setOnClickListener { checkPermission() }
        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            myPermissionsRequestAccessFineLocation -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(context, "Now your app already have permission", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "You deny the permission", Toast.LENGTH_LONG).show()
                }
                return
            }
            else -> {
                Toast.makeText(context, "request permission but other status", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BluetoothFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                BluetoothFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun onScan() {
        viewModel.scanLeDevice()
    }

    private fun checkPermission() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {  // Need > 5.1
            when { //Horse important, 類似 if
                // ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {  //Horse important 這樣寫不行, context 會有問題,要改下方
                context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED -> {  //Horse important : ?是空值檢查,不為空再去做後面的處理
                    Toast.makeText(context, "You already have permission before", Toast.LENGTH_LONG).show()
                }
                else -> {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), myPermissionsRequestAccessFineLocation)
                }
            }
        }
    }
}