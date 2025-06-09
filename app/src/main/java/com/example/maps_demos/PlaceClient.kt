package com.example.maps_demos

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

class PlaceClient : Application() {

    lateinit var placesClient: PlacesClient
        private set

    override fun onCreate() {
        super.onCreate()

        Places.initializeWithNewPlacesApiEnabled(applicationContext, BuildConfig.PLACES_API_KEY)

        placesClient = Places.createClient(this)
    }
}
