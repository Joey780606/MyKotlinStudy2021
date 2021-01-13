package pcp.com.mykotlin2021.bluetooth

import android.bluetooth.BluetoothDevice

class BleScanDevice {
    var address = ""    //Mac address
        get() = field
    var device: BluetoothDevice? = null
    var type = 0 //Scan device type (Maybe have several type device)
    var findTime: Long = 0  //Find time
        set(value) { field = value}

    constructor(addr: String, dev: BluetoothDevice, type: Int, findtime: Long) {
        this.address = addr
        this.device = dev
        this.type = type
        this.findTime = findtime
    }
}