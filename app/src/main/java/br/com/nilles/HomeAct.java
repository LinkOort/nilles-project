package br.com.nilles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;;

public class HomeAct extends Fragment {
    


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // O layout vai ser inflado a partir daqui
        return inflater.inflate(R.layout.home_frag, container, false);
    }

}
