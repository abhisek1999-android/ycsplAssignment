package com.example.ycsplassignment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ycsplassignment.databinding.ActivityMapBinding
import com.example.ycsplassignment.sp.EMAIL
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity() {

    private var cameraPosition: CameraPosition? = null
    private lateinit var binding: ActivityMapBinding
    private var latLong: LatLng? = null
    private var zoomLevel=17.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapView.onCreate(savedInstanceState)
        binding.emailId.text = intent.getStringExtra(EMAIL)

//        binding.logOut.setOnClickListener {
//            writeStringPref(this,"", EMAIL)
//            writeStringPref(this,"", PASSWORD)
//
//        }


        binding.currentLoc.setOnClickListener {
            askLocation()
        }
        askLocation()
    }

    private fun askLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 0)
        } else {
            showLocation()
        }



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED &&
            grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            showLocation()
        } else {
            // permission denied, handle accordingly
            latLong= LatLng(26.889069012609692, 75.80286314603065)
            binding.locInfo.text = "Permission denied !! Default location is set to YCSPL Jaipur"
            setMapLocation(latLong!!,"YCSPL")
        }
    }



    @SuppressLint("MissingPermission")
    private fun showLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        val latitude = location?.latitude
                        val longitude = location?.longitude
                        if (latitude != null && longitude != null) {
                            binding.locInfo.text = "Latitude: $latitude, Longitude: $longitude"
                             latLong = LatLng(latitude, longitude)
                            setMapLocation(latLong?:LatLng(26.889069012609692, 75.80286314603065),"Current Location")
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                    override fun onProviderEnabled(provider: String) {}

                    override fun onProviderDisabled(provider: String) {}
                }, null)
        } else {
            // location services disabled, handle accordingly
            binding.locInfo.text = "Location services disabled"
        }
    }


    override fun onResume() {
        super.onResume()
       binding. mapView.onResume()
        try {
            MapsInitializer.initialize(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setMapLocation(latLong?:LatLng(26.889069012609692, 75.80286314603065),"YCSPL")
    }

    private fun setMapLocation(location: LatLng, s: String) {

        binding.mapView.getMapAsync { googleMap ->
            googleMap.addMarker(MarkerOptions().position(location).title(s))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.getMapAsync { googleMap ->
            cameraPosition = googleMap.cameraPosition
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.mapView.getMapAsync { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition ?: googleMap.cameraPosition))
        }
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
       binding.mapView.onLowMemory()
    }
}