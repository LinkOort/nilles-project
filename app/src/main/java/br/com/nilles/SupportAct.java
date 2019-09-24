package br.com.nilles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SupportAct extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View support = inflater.inflate(R.layout.support_frag, container, false);
        Button button = (Button) support.findViewById(R.id.btnBluetooth);
        button.setOnClickListener(this);
        return support;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBluetooth:
                getActivity().finish();
        }
    }
}
