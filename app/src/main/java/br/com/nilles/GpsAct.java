package br.com.nilles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GpsAct extends Fragment {

    public GpsAct() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // O layout vai ser inflado a partir daqui
        return inflater.inflate(R.layout.gps_frag, container, false);
    }

}
