package pcp.com.mykotlin2021.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
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
    
    private var ScanDevice: ArrayList<BleScanDevice>? = arrayListOf<BleScanDevice>()

    enum class ScanDevOperator{
        ADD, MODIFY, DELETE, CLEAR
    }

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
                Timber.i("Total scan count:" + ScanDevice?.size)
            }, SCAN_PERIOD)
            mScanning = true
            SetScanDevice(ScanDevOperator.CLEAR, null)
            bluetoothLeScanner.startScan(leScanCallback)
        } else {
            mScanning = false
            bluetoothLeScanner.startScan(leScanCallback)
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            if(!SetScanDevice(ScanDevOperator.MODIFY, result.device))
                SetScanDevice(ScanDevOperator.ADD, result.device)
            Timber.i("Scan result:" + result.device.name + " , " + result.device.address)
            //leDeviceListAdapter!!.addDevice(result.device)
            //leDeviceListAdapter.notifyDataSetChanged()
        }
    }

    private fun SetScanDevice(operateType: ScanDevOperator, bleDevice: BluetoothDevice?): Boolean {
        var returnValue = false
        when (operateType) {
            ScanDevOperator.ADD ->  {
                returnValue = false;
                bleDevice?.let {    //Horse important: 表示 bleDevice若不為null才做以下事情
                    var newdev = BleScanDevice(bleDevice.address, bleDevice, 0, System.currentTimeMillis())
                    ScanDevice?.add(newdev)
                    returnValue = true
                }
            }
            ScanDevOperator.MODIFY -> {
                returnValue = false;
                bleDevice?.let {
                    for (dev in ScanDevice.orEmpty()) {  //Horse important: orEmpty() 表示 如果它不是null，則返回此列表，否則返回空列表。
                        if (bleDevice.address.equals(dev.address)) {
                            dev.findTime = System.currentTimeMillis()
                            returnValue = true;
                            break
                        }
                    }
                }
            }
            ScanDevOperator.DELETE -> {
                returnValue = false;
                bleDevice?.let {
                    for (dev in ScanDevice.orEmpty()) {  //Horse important: orEmpty() 表示 如果它不是null，則返回此列表，否則返回空列表。
                        if (bleDevice.address.equals(dev.address)) {
                            ScanDevice?.remove(dev)
                            returnValue = true;
                            break
                        }
                    }
                }
            }
            ScanDevOperator.CLEAR -> { ScanDevice?.clear()
                returnValue = true;
            }
        }
        return returnValue
    }
}