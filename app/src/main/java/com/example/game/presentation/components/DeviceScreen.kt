package com.example.game.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.BluetoothDevice
import com.example.game.presentation.BluetoothUiState

@Composable
fun DeviceScreen(
    state: BluetoothUiState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onDeviceClick: (BluetoothDevice) -> Unit,
    onStartServer: () -> Unit
    ){
    Column(
        modifier = Modifier.fillMaxSize()

    ) {
        BluetoothDeviceList(pairedDevice = state.pairedDevices,scannedDevice = state.scannedDevices,
            onClick = onDeviceClick,
            modifier = Modifier.fillMaxWidth()
                .weight(1f))
       Row(
           modifier = Modifier.fillMaxWidth(),
           horizontalArrangement = Arrangement.SpaceAround
       ){
           Button(onClick = onStartScan) {
               Text(text = "Start scan")
           }
           Button(onClick = onStopScan) {
               Text(text = "Stop scan")
       }
           Button(onClick = onStartServer) {
               Text(text = "Start Server")
           }
    }
    }
}

@Composable
fun BluetoothDeviceList(
    pairedDevice: List<BluetoothDevice>,
    scannedDevice: List<BluetoothDevice>,
    onClick: (BluetoothDevice) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Text(text = "Paired Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp))
        }
        items(pairedDevice){ device ->
            Text(text = device.name ?: "(No name)",
                modifier = Modifier.fillMaxWidth()
                    .clickable { onClick(device) }
                    .padding(16.dp))
        }
        item {
            Text(text = "Scanned Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp))
        }
        items(scannedDevice){ device ->
            Text(text = device.name ?: "(No name)",
                modifier = Modifier.fillMaxWidth()
                    .clickable { onClick(device) }
                    .padding(16.dp))
        }
    }
}
