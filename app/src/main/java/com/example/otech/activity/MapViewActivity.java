package com.example.otech.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otech.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapViewActivity extends AppCompatActivity {
    
    private MapView mapView;
    private TextView tvAddress;
    private MaterialButton btnOpenInGoogleMaps;
    private CircularProgressIndicator progressBar;
    
    private String address;
    private double latitude;
    private double longitude;
    private boolean isGeocoded = false;
    
    private ExecutorService executorService;
    private Handler mainHandler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Configure osmdroid
        Configuration.getInstance().setUserAgentValue(getPackageName());
        
        setContentView(R.layout.activity_map_view);
        
        // Get intent data
        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);
        
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        
        initViews();
        setupToolbar();
        
        // Geocode address if coordinates are default
        if (latitude == 21.0285 && longitude == 105.8542) {
            geocodeAddress();
        } else {
            setupMap();
        }
    }
    
    private void initViews() {
        mapView = findViewById(R.id.mapView);
        tvAddress = findViewById(R.id.tvAddress);
        btnOpenInGoogleMaps = findViewById(R.id.btnOpenInGoogleMaps);
        progressBar = findViewById(R.id.progressBar);
        
        tvAddress.setText(address);
        
        btnOpenInGoogleMaps.setOnClickListener(v -> openInGoogleMaps());
    }
    
    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Địa chỉ giao hàng");
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void geocodeAddress() {
        progressBar.setVisibility(View.VISIBLE);
        
        executorService.execute(() -> {
            try {
                // Use Nominatim (OpenStreetMap) Geocoding API
                String encodedAddress = URLEncoder.encode(address + ", Vietnam", "UTF-8");
                String urlString = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json&limit=1";
                
                URL url = new URL(urlString);
                @SuppressWarnings("deprecation")
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", getPackageName());
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    // Parse JSON response
                    JSONArray jsonArray = new JSONArray(response.toString());
                    if (jsonArray.length() > 0) {
                        JSONObject location = jsonArray.getJSONObject(0);
                        double lat = location.getDouble("lat");
                        double lon = location.getDouble("lon");
                        
                        // Update coordinates
                        mainHandler.post(() -> {
                            latitude = lat;
                            longitude = lon;
                            isGeocoded = true;
                            progressBar.setVisibility(View.GONE);
                            setupMap();
                        });
                    } else {
                        // No results found, use default
                        mainHandler.post(() -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(this, "Không tìm thấy tọa độ chính xác, hiển thị vị trí mặc định", Toast.LENGTH_SHORT).show();
                            setupMap();
                        });
                    }
                } else {
                    throw new Exception("HTTP error code: " + responseCode);
                }
                
                connection.disconnect();
                
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Không thể tìm vị trí, hiển thị vị trí mặc định", Toast.LENGTH_SHORT).show();
                    setupMap();
                });
            }
        });
    }
    
    private void setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(false);
        
        // Set zoom level
        mapView.getController().setZoom(isGeocoded ? 16.0 : 12.0);
        
        // Create geo point from coordinates
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapView.getController().setCenter(startPoint);
        
        // Add marker with custom style
        Marker marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Địa chỉ giao hàng");
        marker.setSnippet(address);
        marker.setInfoWindow(null); // Disable info window, we show card instead
        mapView.getOverlays().add(marker);
    }
    
    private void openInGoogleMaps() {
        try {
            // Try to open in Google Maps app
            String uri = String.format("geo:%f,%f?q=%f,%f(%s)", 
                latitude, longitude, latitude, longitude, 
                Uri.encode(address));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // Fallback to web browser
                String webUri = String.format("https://www.google.com/maps/search/?api=1&query=%f,%f", 
                    latitude, longitude);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUri));
                startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở Google Maps", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDetach();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
