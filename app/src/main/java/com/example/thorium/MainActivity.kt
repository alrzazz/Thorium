package com.example.thorium

import android.os.Bundle
import android.preference.PreferenceManager

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var map : MapView;
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main)

        map = findViewById<MapView>(R.id.map)
        map.isHorizontalMapRepetitionEnabled = false
        map.isVerticalMapRepetitionEnabled = false

        map.setMultiTouchControls(true);


        map.setTileSource(TileSourceFactory.MAPNIK);
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
}