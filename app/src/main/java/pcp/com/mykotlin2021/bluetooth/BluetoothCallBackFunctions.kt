package pcp.com.mykotlin2021.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.le.ScanResult

interface BluetoothCallBackFunctions {
    fun onBluetoothOn()
    fun onBluetoothOff()
    fun onScanFailed(status: Int)
    fun onFoundScannedDevice(
        foundDevice: BluetoothDevice?,
        rssi: Int,
        scanResult: ScanResult?
    )
    fun onFoundScannedDeviceLess5_0(
        foundDevice: BluetoothDevice?,
        rssi: Int,
        scanResult: ByteArray?
    )

    fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int)
    fun onBondingStatus(
        device: BluetoothDevice?,
        newState: Int,
        previousBondState: Int
    )

    fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int)
    fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    )

    fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    )

    fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    )

    fun onDescriptorWrite(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    )

    fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int)
}