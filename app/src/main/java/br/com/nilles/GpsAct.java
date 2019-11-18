package br.com.nilles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GpsAct extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public GpsAct() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // O layout vai ser inflado a partir daqui
        return inflater.inflate(R.layout.gps_frag, container, false);

    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng brazil = new LatLng(-23, -46);
        mMap.addMarker(new MarkerOptions().position(brazil).title("São Paulo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(brazil));

    }

}
