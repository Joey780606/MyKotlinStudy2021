package pcp.com.mykotlin2021.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed

import androidx.lifecycle.ViewModel
import timber.log.Timber

class BluetoothViewModel : ViewModel() {
    // Scan will use
    private val bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
    private var mScanning = false

    private val SCAN_PERIOD: Long = 10000

    init {
        Timber.i("BluetoothViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("BluetoothViewModel destroyed")
    }

    public fun scanLeDevice() {
        if(!mScanning) {
            Handler(Looper.getMainLooper()).postDelayed({
                mScanning = false
                bluetoothLeScanner.stopScan(leScanCallback)
            }, SCAN_PERIOD)
            mScanning = true
            bluetoothLeScanner.startScan(leScanCallback)
        } else {
            mScanning = false
            bluetoothLeScanner.startScan(leScanCallback)
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Timber.i("Scan result:" + result.device.name + " , " + result.device.address)
            //leDeviceListAdapter!!.addDevice(result.device)
            //leDeviceListAdapter.notifyDataSetChanged()
        }
    }
}