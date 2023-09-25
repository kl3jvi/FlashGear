package com.kl3jvi.yonda.connectivity

//class ConnectionServiceImpl : ConnectionService, KoinComponent {
//
//    private val central: BluetoothCentralManager by inject()
//    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    override val connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
//
//    init {
//        central.observeConnectionState { peripheral: BluetoothPeripheral, state: ConnectionState ->
//            Log.e("Peripheral '${peripheral.name}'", "is $state")
//            connectionState.update { state }
//            when (state) {
//                ConnectionState.CONNECTED -> handlePeripheral(peripheral)
//                ConnectionState.DISCONNECTED -> scope.launch {
//                    delay(15000)
//                    // Check if this peripheral should still be auto connected
//                    if (central.getPeripheral(peripheral.address)
//                            .getState() == ConnectionState.DISCONNECTED
//                    ) {
//                        central.autoConnectPeripheral(peripheral)
//                    }
//                }
//
//                else -> {}
//            }
//        }
//    }
//
//    private fun handlePeripheral(peripheral: BluetoothPeripheral) {
//        scope.launch {
//            peripheral.requestConnectionPriority(ConnectionPriority.HIGH)
//            peripheral.getCharacteristic(XIAOMI_SERVICE, CHAR_WRITE)?.let {
//                if (it.supportsWritingWithResponse()) {
//                    peripheral.writeCharacteristic(
//                        it,
//                        Ampere().getRequestString().hexToBytes(),
//                        WriteType.WITH_RESPONSE,
//                    )
//                }
//            }
//            peripheral.getCharacteristic(XIAOMI_SERVICE, CHAR_READ)?.let {
//                val descriptor = it.getDescriptor(CHAR_WRITE)
//                if (descriptor != null) {
//                    peripheral.writeDescriptor(descriptor, Ampere().getRequestString().hexToBytes())
//                }
//                peripheral.observe(it) { value ->
//                    // Process the updated value of the characteristic
//                    // ...
//                    Log.e("value", value.asHexString())
//                }
//            }
//        }
//    }
//
//    override fun scanBleDevices(): Flow<BluetoothPeripheral> = callbackFlow {
//        runCatching {
//            central.scanForPeripherals(
//                resultCallback = { bluetoothPeripheral: BluetoothPeripheral, _: ScanResult ->
//                    trySend(bluetoothPeripheral)
//                },
//                scanError = {
//                    cancel(Error(scanFailure = it).getErrorMessage())
//                },
//            )
//        }.onFailure {
//            cancel(Error(throwable = it).getErrorMessage())
//        }
//        awaitClose(::stopScanning)
//    }
//
//    override fun stopScanning() = central.stopScan()
//
//    override fun connectToPeripheral(peripheral: BluetoothPeripheral) {
//        peripheral.observeBondState {
//            Log.e("Bond state is", "$it")
//        }
//        scope.launch {
//            try {
//                stopScanning()
//                central.connectPeripheral(peripheral)
//            } catch (connectionFailed: ConnectionFailedException) {
//                Log.e("connection", "failed")
//            }
//        }
//    }
//
//    override suspend fun readFromScooter(peripheral: BluetoothPeripheral) {
//        scope.launch {
//            peripheral.getCharacteristic(XIAOMI_SERVICE, CHAR_READ)?.let {
//                peripheral.observe(it) { value ->
//                    Log.e("result", value.asHexString())
//                }
//            }
//        }
//    }
//
//    override suspend fun sendCommand(peripheral: BluetoothPeripheral): ByteArray {
//        return ByteArray(0)
//    }
//
//    companion object {
//        val XIAOMI_SERVICE: UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e") // WRITE
//        val CHAR_WRITE: UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e") // WRITE
//        val CHAR_READ: UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e") // READ
//    }
//}
