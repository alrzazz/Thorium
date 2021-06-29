package com.example.thorium.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.SyncStateContract.Helpers.insert
import android.telephony.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.thorium.R
import com.example.thorium.db.AppDatabase
import com.example.thorium.db.entities.Status
import com.example.thorium.repositories.StatusRepository
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.*

class MainActivity : AppCompatActivity() {

    private val techTypes = arrayListOf(
        "NETWORK_TYPE_UNKNOWN",
        "NETWORK_TYPE_GPRS",
        "NETWORK_TYPE_EDGE",
        "NETWORK_TYPE_UMTS",
        "NETWORK_TYPE_CDMA",
        "NETWORK_TYPE_EVDO_0",
        "NETWORK_TYPE_EVDO_A",
        "NETWORK_TYPE_1xRTT",
        "NETWORK_TYPE_HSDPA",
        "NETWORK_TYPE_HSUPA",
        "NETWORK_TYPE_HSPA",
        "NETWORK_TYPE_IDEN",
        "NETWORK_TYPE_EVDO_B",
        "NETWORK_TYPE_LTE",
        "NETWORK_TYPE_EHRPD",
        "NETWORK_TYPE_HSPAP",
        "NETWORK_TYPE_GSM",
        "NETWORK_TYPE_TD_SCDMA",
        "NETWORK_TYPE_IWLAN"
    )

    private lateinit var map : MapView;
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    lateinit var locationManager: LocationManager
    private var telephonyManager: TelephonyManager ?= null
    private val LOCATION_TIME_INTERVAL:Long = 6000L // get gps location every 1 min
    private val LOCATION_DISTANCE:Float = 1000F // set the distance value in meter
//    private val wordViewModel: StatusViewModel by viewModels {
//        StatusViewModelFactory((application as StatusApplication).repository)
//    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?


        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main)

        map = findViewById<MapView>(R.id.map)
        map.isHorizontalMapRepetitionEnabled = false
        map.isVerticalMapRepetitionEnabled = false
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);


        var marker = Marker(map)
        var location = getLocation()
        if (location != null) {
            var pos = GeoPoint(location.latitude, location.longitude)
            marker.position = pos
            val mapController = map.controller
            mapController.setZoom(18)
            mapController.animateTo(pos)
        }

//        marker.position = map.mapCenter as GeoPoint
        marker.icon = ContextCompat.getDrawable(this, R.drawable.ic_location_on)
        marker.title = "Test Marker"

//        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        map.overlays.add(marker)
        map.invalidate()
    }

    override fun onResume() {
        super.onResume();
        map.onResume();
    }

    override fun onPause() {
        super.onPause();
        map.onPause();
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>();
        var i = 0;
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i]);
            i++;
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun getLocation(): Location? {
        var locationNetwork : Location? = null
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 34)
        }


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_TIME_INTERVAL, LOCATION_DISTANCE, object : LocationListener {
            @SuppressLint("MissingPermission")
            override fun onLocationChanged(p0: Location) {
                locationNetwork = p0
                var telephonyInfo = getTelInfo()
                Log.d("1", telephonyInfo.toString())

//                StatusViewModel.insert(Status(
//                    cellID = telephonyInfo["cellID"],
//                    plmnID = telephonyInfo["plmnID"],
//                    netGen = telephonyInfo["netGen"],
//                    code = telephonyInfo["code"],
//                    arfcn = telephonyInfo["arfcn"],
//                    latitude = p0.latitude,
//                    longitude = p0.longitude
//                ))

            }
        })

        val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (localNetworkLocation != null){
            locationNetwork = localNetworkLocation
            var telephonyInfo = getTelInfo()
            Log.d("1", telephonyInfo.toString())
//                StatusViewModel.insert(Status(
//                    cellID = telephonyInfo["cellID"],
//                    plmnID = telephonyInfo["plmnID"],
//                    netGen = telephonyInfo["netGen"],
//                    code = telephonyInfo["code"],
//                    arfcn = telephonyInfo["arfcn"],
//                    latitude = p0.latitude,
//                    longitude = p0.longitude
//                ))
        }


        return locationNetwork
    }
    /*private fun requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            permissionsToRequest.add(permission);
        }
    }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }*/

    @SuppressLint("MissingPermission")
    fun getNetWorkType(): String {
        return this.techTypes[telephonyManager?.networkType!!]
    }

    @SuppressLint("MissingPermission")
    fun getCurrentCell(): CellInfo? {
        val allCellInfo = telephonyManager?.allCellInfo
        when (this.getNetWorkType()) {
            "NETWORK_TYPE_EDGE" -> {
                if (allCellInfo != null) {
                    for (cellInfo in allCellInfo) {
                        if (cellInfo is CellInfoGsm) {
                            return cellInfo
                        }
                    }
                }
            }
            "NETWORK_TYPE_GPRS" -> {
                if (allCellInfo != null) {
                    for (cellInfo in allCellInfo) {
                        if (cellInfo is CellInfoGsm)
                            return cellInfo
                    }
                }
            }
            "NETWORK_TYPE_HSPA" -> {
                if (allCellInfo != null) {
                    for (cellInfo in allCellInfo) {
                        if (cellInfo is CellInfoWcdma)
                            return cellInfo
                    }
                }
            }
            "NETWORK_TYPE_UMTS" -> {
                if (allCellInfo != null) {
                    for (cellInfo in allCellInfo) {
                        if (cellInfo is CellInfoWcdma)
                            return cellInfo
                    }
                }
            }
            "NETWORK_TYPE_LTE" -> {
                if (allCellInfo != null) {
                    for (cellInfo in allCellInfo) {
                        if (cellInfo is CellInfoLte)
                            return cellInfo
                    }
                }
            }
            else ->
                return allCellInfo?.get(0)
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun getTelInfo(): HashMap<String?, String?> {
        var teleInfo = hashMapOf<String?, String?>()
        var currentCell = getCurrentCell()
        when (currentCell) {
            is CellInfoGsm -> {
                val cellIdentityGsm = currentCell.cellIdentity
                teleInfo["cellID"] = cellIdentityGsm.cid.toString()
                teleInfo["ac"] = cellIdentityGsm.lac.toString()
                teleInfo["arfcn"] = cellIdentityGsm.arfcn.toString()
                teleInfo["plmnID"] = cellIdentityGsm.mobileNetworkOperator.toString()
                teleInfo["netGen"] = "2"
                teleInfo["code"] = "BSIC"

            }
            is CellInfoWcdma -> {
                val cellIdentityWcdma = currentCell.cellIdentity
                teleInfo["cellID"] = cellIdentityWcdma.cid.toString()
                teleInfo["ac"] = cellIdentityWcdma.lac.toString()
                teleInfo["arfcn"] = cellIdentityWcdma.uarfcn.toString()
                teleInfo["plmnID"] = cellIdentityWcdma.mobileNetworkOperator.toString()
                teleInfo["netGen"] = "3"
                teleInfo["code"] = "PSC"
            }
            is CellInfoLte -> {
                val cellIdentityLte = currentCell.cellIdentity
                teleInfo["cellID"] = cellIdentityLte.ci.toString()
                teleInfo["ac"] = cellIdentityLte.tac.toString()
                teleInfo["arfcn"] = cellIdentityLte.earfcn.toString()
                teleInfo["plmnID"] = cellIdentityLte.mobileNetworkOperator.toString()
                teleInfo["netGen"] = "4"
                teleInfo["code"] = "PCI"
            }
        }
        return teleInfo
    }
}