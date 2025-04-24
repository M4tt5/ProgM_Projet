package com.example.game

import com.example.game.data.chat.BluetoothMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val isConnected: StateFlow<Boolean>
    val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
    val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
    val errors: SharedFlow<String>

    fun startDiscovery()
    fun stopDiscovery()

    fun startBluetoothServer(): Flow<com.example.game.data.chat.ConnectionResult>
    fun connectToDevice(device: BluetoothDeviceDomain): Flow<com.example.game.data.chat.ConnectionResult>
    fun closeConnection()

    suspend fun trySendMessage(message: String): BluetoothMessage?
    fun release()
}