package com.devapp.cookfriends.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

enum class NetworkStatus {
    Available, // General internet connectivity
    Unavailable,
    Wifi,      // Specifically Wi-Fi
    Cellular   // Specifically Cellular/Mobile Data
}

interface ConnectivityObserver {
    fun observe(): Flow<NetworkStatus>
    fun getCurrentNetworkStatus(): NetworkStatus // For an immediate check
}

@Singleton
class NetworkConnectivityObserver @Inject constructor(
    context: Context
) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<NetworkStatus> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(determineNetworkStatus(network)) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    // Check if any other network is available, otherwise send Unavailable
                    launch { send(determineNetworkStatus(null)) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(NetworkStatus.Unavailable) }
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                        when {
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                                launch { send(NetworkStatus.Wifi) }
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                                launch { send(NetworkStatus.Cellular) }
                            else -> launch { send(NetworkStatus.Available) } // Other types like Ethernet, VPN
                        }
                    } else {
                        launch { send(determineNetworkStatus(null)) }
                    }
                }
            }

            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()

            connectivityManager.registerNetworkCallback(request, callback)

            // Send initial status
            launch { send(getCurrentNetworkStatus()) }


            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }

    override fun getCurrentNetworkStatus(): NetworkStatus {
        return determineNetworkStatus(connectivityManager.activeNetwork)
    }

    private fun determineNetworkStatus(activeNetwork: Network?): NetworkStatus {
        val network = activeNetwork ?: connectivityManager.activeNetwork
        if (network == null) {
            return NetworkStatus.Unavailable
        }

        val capabilities = connectivityManager.getNetworkCapabilities(network)
        if (capabilities == null) {
            return NetworkStatus.Unavailable
        }

        return when {
            !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ||
                    !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) -> NetworkStatus.Unavailable
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkStatus.Wifi
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkStatus.Cellular
            else -> NetworkStatus.Available // For other types like Ethernet, VPN etc.
        }
    }
}
