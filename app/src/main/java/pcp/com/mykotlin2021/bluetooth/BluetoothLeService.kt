package pcp.com.mykotlin2021.bluetooth

import android.app.Service
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import timber.log.Timber
import java.util.*

/*
  Author: Joey
  可列記錄的
  1. BroadcastReceiver 的用法
  2. intent 相關參數
 */
class BluetoothLeService : Service() {
    private var bluetoothCallBackFunctions: BluetoothCallBackFunctions? = null
    private var bluetoothManager: BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var context: Context? = null

    private val CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb") //Horse Kotlin: 為 Notification使用
    // Java就是 private static final UUID

    private var mWantConnectDevice: BluetoothDevice?= null
    var isOTAScan = false  //是否在OTA的scan

    fun initialize(context: Context) {
        this.context = context
        this.bluetoothCallBackFunctions = null
    }

    fun activateBlueTooth(activate: Boolean, btCallbackFuns: BluetoothCallBackFunctions): Boolean {
        bluetoothManager = context?.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager?.adapter

        if(bluetoothAdapter == null)
            return false

        if(bluetoothAdapter!!.isEnabled() && !activate) { //Horse important: isEnabled() - true: Bluetooth on, false: Bluetooth off
            bluetoothAdapter?.disable()
            context?.unregisterReceiver(bluetoothReceiver)
            context?.unregisterReceiver(mBondingBroadcastReceiver)  //Bonding check
        }

        if( activate ) {
            var filter: IntentFilter = IntentFilter()
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            filter.addAction(BluetoothDevice.ACTION_FOUND)
            filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)

            bluetoothAdapter?.enable()
            //Horse important: try to distinguish the different between below (Maybe below is suitable for activity
              //val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
              //startActivityForResult(enableBtIntent)
            context?.registerReceiver(bluetoothReceiver, filter)
            context?.registerReceiver(mBondingBroadcastReceiver,
                IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            )
        }

        bluetoothCallBackFunctions = btCallbackFuns
        return true
    }

    private val mmGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() { //Horse Kotlin want, 裝置連線時的相關Callback回應處理
    // Original:  private final BluetoothGattCallback mmGattCallback = new BluetoothGattCallback() {
        override fun onPhyUpdate(gatt: BluetoothGatt, txPhy: Int, rxPhy: Int, status: Int) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status)
        }

        override fun onPhyRead(gatt: BluetoothGatt, txPhy: Int, rxPhy: Int, status: Int) {
            super.onPhyRead(gatt, txPhy, rxPhy, status)
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            bluetoothCallBackFunctions?.onConnectionStateChange(gatt, status, newState)
            if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                gatt.close()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            bluetoothCallBackFunctions?.onServicesDiscovered(gatt, status)
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
            bluetoothCallBackFunctions?.onCharacteristicRead(gatt, characteristic, status)
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            bluetoothCallBackFunctions?.onCharacteristicWrite(gatt, characteristic, status)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)
            bluetoothCallBackFunctions?.onCharacteristicChanged(gatt, characteristic)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            super.onDescriptorRead(gatt, descriptor, status)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)
            bluetoothCallBackFunctions?.onDescriptorWrite(gatt, descriptor, status)
        }

        override fun onReliableWriteCompleted(gatt: BluetoothGatt, status: Int) {
            super.onReliableWriteCompleted(gatt, status)
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
            bluetoothCallBackFunctions?.onReadRemoteRssi(gatt, rssi, status)
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
        }
    }

    private val mBinder: IBinder = LocalBinder() //Horse Kotlin want

//    class LocalBinder : Binder() {
//        //Horse Kotlin want
//        internal val service: BluetoothLeService
//            get() = this@BluetoothLeService
//    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): BluetoothLeService = this@BluetoothLeService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    } //Horse Kotlin want

    override fun onUnbind(intent: Intent?): Boolean {   //Horse Kotlin want
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close()
        return super.onUnbind(intent)
    }

    fun close() {}

    private val bluetoothReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        ///Horse Kotlin 連線狀態的改變
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null) {
                when (action) {
                    BluetoothAdapter.ACTION_STATE_CHANGED -> {
                        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                        if (bluetoothCallBackFunctions != null) {
                            when (state) {
                                BluetoothAdapter.STATE_OFF, BluetoothAdapter.STATE_TURNING_OFF -> {   //Horse important: 二個狀況都要做相同處理的情況
                                    Timber.i("onBluetoothOff 00 ota")
                                    isOTAScan = false
                                    bluetoothCallBackFunctions!!.onBluetoothOff()
                                }
                                BluetoothAdapter.STATE_ON -> bluetoothCallBackFunctions!!.onBluetoothOn()
                            }
                        }
                    }
                    BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    }
                    BluetoothDevice.ACTION_FOUND -> {
                    }
                }
            }
        }
    }

    // public boolean scanDevices(boolean action)   // Scan用的,這段先不做

    // private BluetoothAdapter.LeScanCallback leScanCallback =   // 5.0以下的scan,先不做

    // void fCheckBle(boolean bOnOff) { //Scan的相關設定,先不處理

    fun connectDevice(bleDevice: BluetoothDevice, action: Boolean): Boolean ///Horse Kotlin 連線
    {
        var bReturnStatus = false
        if (bluetoothAdapter != null) {
            mWantConnectDevice = bluetoothAdapter!!.getRemoteDevice(bleDevice.address)
            if (mWantConnectDevice != null) {
                if (action) {
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                            mWantConnectDevice!!.connectGatt(context, false, mmGattCallback, BluetoothDevice.TRANSPORT_LE, BluetoothDevice.PHY_LE_1M_MASK /*, mHandler*/)
                        }
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                            mWantConnectDevice!!.connectGatt(context, false, mmGattCallback, BluetoothDevice.TRANSPORT_LE)
                        }
                        else -> {
                            mWantConnectDevice!!.connectGatt(context, false, mmGattCallback)
                        }
                    }
                    bReturnStatus = true
                }
            }
        }
        return bReturnStatus
    }

    fun f_readCharacteristic(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {     //Horse important 讀characteristic
        if (gatt == null) {
            //Log.w(TAG, "BluetoothAdapter not initialized");
            return
        }
        Timber.i("BluetoothLe f_readCharacteristic")
        gatt.readCharacteristic(characteristic)
    }

    fun f_setCharacteristicNotification(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic, enabled: Boolean) {    //Horse important Nearly readCharacteristic, from DeviceControlActivity.java call
        if (gatt == null) {      ///Horse Kotlin
            return
        }
        gatt.setCharacteristicNotification(characteristic, enabled)

        // This is specific to Heart Rate Measurement.
        //if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
        val descriptor =
            characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG)
        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        gatt.writeDescriptor(descriptor)
    }

    private val mBondingBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {       //Horse Kotlin Bonding狀況的改變
            val device =
                intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
            val previousBondState =
                intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1)
            bluetoothCallBackFunctions!!.onBondingStatus(device, bondState, previousBondState)
            when (bondState) {
                BluetoothDevice.BOND_NONE -> {
                }
                BluetoothDevice.BOND_BONDING -> {
                }
                BluetoothDevice.BOND_BONDED -> {
                }
            }
        }
    }

    fun setIsOTAScan(mOTAStatus: Boolean) {  //Horse Kotlin 是否在OTA狀況,但這位置不好
        Timber.i( "set OTA scan: $mOTAStatus")
        isOTAScan = mOTAStatus
    }
}