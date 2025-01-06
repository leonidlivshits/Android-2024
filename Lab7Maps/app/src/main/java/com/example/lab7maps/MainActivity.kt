package com.example.lab7maps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainActivity : AppCompatActivity() {

    private lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(applicationContext, androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext))
        Configuration.getInstance().userAgentValue = packageName

        setContentView(R.layout.activity_main)

        map = findViewById(R.id.map)
        map.setTileSource(XYTileSource(
            "OpenStreetMap",
            0, 19, 256, ".png",
            arrayOf("https://a.tile.openstreetmap.org/", "https://b.tile.openstreetmap.org/", "https://c.tile.openstreetmap.org/")
        ))

        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        val startPoint = GeoPoint(55.75, 37.61) // Москва
        val overlay = MyLocationNewOverlay(map)
        overlay.enableMyLocation()
        map.overlays.add(overlay)
        map.controller.setZoom(12.0)
        map.controller.setCenter(startPoint)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}
