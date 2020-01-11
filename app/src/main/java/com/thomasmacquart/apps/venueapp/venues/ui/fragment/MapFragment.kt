package com.thomasmacquart.apps.venueapp.venues.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.thomasmacquart.apps.venueapp.R
import com.thomasmacquart.apps.venueapp.VenueApp
import com.thomasmacquart.apps.venueapp.core.extensions.exhaustive
import com.thomasmacquart.apps.venueapp.venues.data.entities.LatitudeLongitude
import com.thomasmacquart.apps.venueapp.venues.data.entities.MapBounds
import com.thomasmacquart.apps.venueapp.venues.data.entities.Venue
import com.thomasmacquart.apps.venueapp.venues.ui.viewmodel.MapViewModel
import com.thomasmacquart.apps.venueapp.venues.ui.viewmodel.MapViewState
import kotlinx.android.synthetic.main.map_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

const val REQUEST_CODE_LOCATION = 123

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap

    @Inject
    lateinit var viewModelFactory: MapViewModel.Factory

    private val viewModel by viewModels<MapViewModel> { viewModelFactory }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        (requireActivity().applicationContext as VenueApp).appComponent.inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragment = SupportMapFragment()
        childFragmentManager.beginTransaction().add(R.id.map_fragment_container, fragment).commit()
        fragment.getMapAsync(this)

        viewModel.getUiObservable().observe(this, Observer {
            when (it) {
                is MapViewState.UpdateMapState -> populateData(it.venues)
                is MapViewState.ErrorState -> showError(it.error)
            }.exhaustive
        })
    }

    private fun showError(error: String) {
        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocation()
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.setOnCameraIdleListener(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latlng = LatLng(it.latitude, it.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17f))
                }

            }

        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.let {
            val action =
                MapFragmentDirections.actionMapFragmentToDetailsFragment2(
                    it.tag as String
                )
            view?.findNavController()?.navigate(action)
        }

        return true
    }

    private fun populateData(venues: List<Venue>) {
        venues.forEach { venue ->
            val latlng = LatLng(venue.location.latitude, venue.location.longitude)

            mMap.addMarker(
                MarkerOptions().position(latlng)
                    .title(venue.name)
            ).tag = venue.id
        }
    }

    override fun onCameraIdle() {
        val latLng = mMap.cameraPosition.target
        viewModel.loadVenues(latLng.latitude, latLng.longitude, getMapBounds())
    }

    private fun getMapBounds(): MapBounds {
        val bounds = mMap.projection.visibleRegion.latLngBounds
        return MapBounds(
            LatitudeLongitude(
                bounds.southwest.latitude,
                bounds.southwest.longitude
            ),
            LatitudeLongitude(
                bounds.northeast.latitude,
                bounds.northeast.longitude
            )
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun enableMyLocation() {
        if (hasLocationPermission()) {
            mMap.isMyLocationEnabled = true
        } else {
            EasyPermissions.requestPermissions(
                this, "coucou",
                REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}