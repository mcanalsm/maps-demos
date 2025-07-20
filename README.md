# Google Maps Platform Demos for Android

This project showcases a few **Google Maps Platform APIs** demos in Android. 
> Built in **Kotlin** 

---

## Features 

| Activity | Description | APIs Used |
|----------|-------------|-----------|
| `DynamicMapActivity.kt` | Displays a dynamic, interactive map with gestures and camera control. | Maps SDK |
| `StreetViewActivity.kt` | Map + street view panorama that updates on map click. | Maps SDK, Street View |
| `PlaceAutocompleteActivity.kt` | Autocomplete widget to search places. | Places API Legacy |
| `PlacesUIKitActivity.kt` |  **Places UI Kit** integration with Search and Details components. | Places API (UI Kit) |
| `PlaceNearbyActivity.kt` | Search for nearby places. | Places API (New) (Nearby Search) |
| `PlaceTextSearchActivity.kt` | Search for places from text input. | Places API (New) (Text Search) |
| `PlaceSARActivity.kt` | **Search Along Route** to find places near a defined path. | Places API (New) (Search Along Route) |
| `PlacePhotosActivity.kt` | Fetches and displays photos associated with a place. | Places API (New) (Photos) |
| `PlaceDetailsActivity.kt` | Retrieves full place details from a Place ID. | Places API (New) (Details) |

---

## Instructions


### 1. Clone the project

Clone the repository using Git:

```bash
git clone https://github.com/mcanalsm/maps-demos.git
cd maps-demos
```

### 2. Create secrets.properties file:

This project uses a `secrets.properties` file to load your API keys securely.
This file is not tracked by Git and must be created manually at the root of your project:

`MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY`\
`PLACES_API_KEY=YOUR_GOOGLE_PLACES_API_KEY`

### 3. Build and run the app

Open the project in Android Studio\
Let Gradle sync finish successfully\
Connect a device or start an emulator\
Click Run ▶ to build and launch the app

