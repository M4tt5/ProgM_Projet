package com.example.game.data.chat

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.game.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}
