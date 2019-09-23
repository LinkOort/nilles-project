package br.com.nilles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SupportAct extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // O layout vai ser inflado a partir daqui
        final View support = inflater.inflate(R.layout.support_frag, container, false);
        Button button = (Button) support.findViewById(R.id.btnBluetooth);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        return support;
    }
}
