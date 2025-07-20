package com.example.maps_demos

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.model.EncodedPolyline
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.SearchAlongRouteParameters
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.android.libraries.places.api.net.SearchByTextResponse
import com.google.maps.android.PolyUtil

class PlaceSARActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private lateinit var placesClient: com.google.android.libraries.places.api.net.PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_sar)

        supportActionBar?.title = "Place SAR"


        placesClient = (application as PlaceClient).placesClient

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun searchAlongRouteRequest() {
        val encodedPolyline = EncodedPolyline.newInstance(
            "wblcFptchVIFOd@G@EVw@Ms@dHKR}ApNA`AF~@Hf@TjAb@bBb@~@n@p@^Rd@~@Vz@HVz@nDLt@?d@Kr@c@~@mD`G?`@aEfGkCnDuChDm`@bb@[`@{GhHeEdEciBnnBkC`DkC~DaClEuKjT_Z|l@Qb@iR~_@}EzJ_AdB_Und@kAfCaOjZkg@vcAqBzD_]rr@iBlEaBxEgArD}AlG}AhHsA`IeAnH{@dIq@dJgL~iBq@rHu@vGgAtHwArHaBhHkBzG_DpJ}Nbc@iBhGkA|EgC|LcIjb@oAhG_AvDgAdDkApC_BzCiBpCsFvGii@vn@scAxlAmLjNgSzUeRjT{TzWqExEmG|FuNlMmMhLaRvPqOlNmbAl}@mFlF{PlOmJfIoElE}LtMiSbU_H`I}}@jcAwl@vp@oAbBqA~BeAhCm@tBg@fCWrBQ~BI|DaB~rBO~D[bEa@`Dm@pDaAdE{@vC_BbEkB~Def@|z@sEzHKJeS~]}K`S{\\~l@cXpe@sBpDm@bAuCxDkBrBiC~BwCtByBnAcBx@}Bt@{Bn@gh@|LaOpDeFhAoDj@aE^kVrA_E^iEr@yD~@uBr@gMjF_EnAcCh@eFr@_DRsAD}@Jsu@xCWDqIV}BCeCOyDm@cBa@_DmA}JeE_CwAsBcBiBoBuAqBmOoX{CuEkB_CoDqDkVoUoD{CeE_DkEkC_FeCqB}@sDuAoDgAeCe@cCW}CK}BDaDTeOlBcuBrYaNlBq@Dyd@rGyFt@yBb@eBf@oCnAoBlAkIpGkAp@wBbAaCt@oFdAwKjBoGxA{FbByIjC_HfB_@KmNdDuC|@uFzBcH|C{@\\[?sBv@}@VaBVoA@y@EmAQcA[w@]aBkAeAkA}BuDUKs@uAqBsCwBcCgAiAiN_MyKsJsG{GkBaBiBuA{BwAwDkBcOaHiC_AiCg@}BQcCAcBHqBVkB`@qEjAu@LgCVgAHwG@sG?mABsH^eNr@mBXy@NqBt@uAt@aBlAkAlA}BtCyApBiAdB_BxB{A`B}@j@oAf@s@PeCVcIf@gAAkAQy@YiAo@_A{@_DgEgJqM_DeEaM}PoBiCzAsBw@kAdAGVk@f@q@z@C"
        )

        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val textQuery = "Spicy Vegetarian Food"

        val searchAlongRouteParameters: SearchAlongRouteParameters =
            SearchAlongRouteParameters.newInstance(encodedPolyline)

        val searchByTextRequest = SearchByTextRequest.builder(textQuery, placeFields)
            .setMaxResultCount(10)
            .setSearchAlongRouteParameters(searchAlongRouteParameters)
            .build()


        placesClient.searchByText(searchByTextRequest)
            .addOnSuccessListener { response: SearchByTextResponse ->
                val places = response.places
                Log.i("Places returned", "Number of places: ${places.size}")
                for (place in places) {
                    Log.i("SAR", "ID: ${place.id}")
                    Log.i("SAR", "Name: ${place.name}")
                    Log.i("SAR", "--------------------")
                }

                addMarkersForPlaces(places)
                addPolylineToMap(encodedPolyline)

                response.places.firstOrNull()?.latLng?.let { placeLocation ->
                    moveCamera(placeLocation)
                } ?: run {
                    Log.w("SAR", "Can't get location for first result")
                }
            }
    }

    private fun addMarkersForPlaces(places: List<Place>) {
        places.forEach { place ->
            Log.d(
                "TextSearchPlaces",
                "Place ID: ${place.id}, Name: ${place.name}, Location: ${place.latLng?.latitude}, ${place.location?.longitude}"
            )
            place.latLng?.let { latLng ->
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(place.displayName)
                    .snippet(place.id)
                googleMap.addMarker(markerOptions)
            }
        }
    }

    private fun addPolylineToMap(encodedPolyline: EncodedPolyline) {
        val polylineString: String? = encodedPolyline.encodedPolyline

        if (polylineString != null) {
            val decodedPath: List<LatLng> = PolyUtil.decode(polylineString)

            val polylineOptions = PolylineOptions()
                .addAll(decodedPath)
                .width(10f)
                .color(0xFF0000FF.toInt())

            googleMap.addPolyline(polylineOptions)

        }
    }

    private fun moveCamera(latLng: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10f)
        googleMap.animateCamera(cameraUpdate)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        Log.i("onMapReady", "Map loaded")
        searchAlongRouteRequest()
    }
}