package com.makao.inquilino;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.makao.inquilino.databinding.ActivityMapaBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btnRegresar = findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Crear una instancia de LatLng para la ubicación deseada
        LatLng licoreria = new LatLng(-12.048471160704953, -75.19727580242669);
        // Cambiar el tipo de mapa a satelital
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Crear una instancia de CameraUpdate para acercar la cámara al mapa
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(licoreria, 20);

        // Utilizar animateCamera() para suavizar el movimiento de la cámara
        mMap.animateCamera(cameraUpdate);

        // Agregar un marcador en la ubicación y establecer el título
        mMap.addMarker(new MarkerOptions().position(licoreria).title("LICORERIA"));
    }
}