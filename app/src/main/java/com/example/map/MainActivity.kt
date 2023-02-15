package com.example.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.map.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(), LocationListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    private val LOCATION_PERMISSION_REQ_CODE = 1000

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationManager: LocationManager


    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnGetLocation.setOnClickListener {
            getCurrentLocation()
        }

        binding.btnOpenMap.setOnClickListener {
            openMap()
        }

    }

    private fun getCurrentLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), LOCATION_PERMISSION_REQ_CODE
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10f, this)
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {

        latitude = location.latitude
        longitude = location.longitude

        binding.latitude.text = "Latitude : " + location.latitude
        binding.longitude.text = "Longitude : " + location.longitude

        binding.btnOpenMap.visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQ_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openMap() {

        val bundle = Bundle()
        bundle.putDouble("latitude", latitude)
        bundle.putDouble("longitude", longitude)

        val intent = Intent(this, MapActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}

