package com.example.mapas
    import android.Manifest
    import android.content.Context
    import android.content.pm.PackageManager
    import android.location.Location
    import android.location.LocationListener
    import android.location.LocationManager
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.widget.Toast

    import com.google.android.gms.maps.CameraUpdateFactory
    import com.google.android.gms.maps.GoogleMap
    import com.google.android.gms.maps.OnMapReadyCallback
    import com.google.android.gms.maps.SupportMapFragment
    import com.google.android.gms.maps.model.LatLng
    import com.google.android.gms.maps.model.MarkerOptions

    class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

        private lateinit var mMap: GoogleMap

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_maps)
            init()
        }

        private fun init() {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        override fun onMapReady(googleMap: GoogleMap) {
            mMap = googleMap

            // Add a marker in Cesar School and move the camera
            /*val cesarSchool = LatLng(-8.059616, -34.8730747)
            mMap.addMarker(MarkerOptions().position(cesarSchool).title("Cesar School"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(cesarSchool))*/
            callMap()
        }

        private fun callMap() {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                mMap.isMyLocationEnabled = true

                val cesarSchool = LatLng(-8.059616, -34.8730747)
                mMap.addMarker(MarkerOptions().position(cesarSchool).title("Cesar School"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(cesarSchool))

                try {
                    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                    val locationListener = object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            val latitude = location?.latitude?: -8.059616
                            val longitude = location?.longitude?: -34.8730747

                            val latlng = LatLng(latitude, longitude)

                            val marker =
                                mMap.addMarker(MarkerOptions().position(latlng).title("Esta é minha localização"))

                            marker.position = latlng
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                        }

                        override fun onProviderEnabled(provider: String?) {

                        }

                        override fun onProviderDisabled(provider: String?) {

                        }

                    }
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0f,
                        locationListener
                    )
                } catch (ex: SecurityException){
                    Toast.makeText(this, ex.message,Toast.LENGTH_LONG).show()
                }
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1010
                )
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            when(requestCode){
                1010 -> {
                    if (grantResults.isNotEmpty() && checkAllPermissionAreGranted(grantResults)) {
                        callMap()
                    }
                }
            }
        }

        private fun checkAllPermissionAreGranted(grantResults: IntArray): Boolean {
            var result = true
            grantResults.forEach { grant ->
                if (grant != PackageManager.PERMISSION_GRANTED){
                    result = false
                }
            }
            return result
        }

    }


