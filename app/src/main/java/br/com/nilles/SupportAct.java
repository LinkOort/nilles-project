package br.com.nilles;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;


public class SupportAct extends Fragment implements View.OnClickListener {

    private TextToSpeech voiceMic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View support = inflater.inflate(R.layout.support_frag, container, false);
        Button button = (Button) support.findViewById(R.id.btnBluetooth);
        button.setOnClickListener(this);
        initializeTexToSpeech();
        return support;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBluetooth:
                getActivity().finish();
                break;
        }
    }

    private void initializeTexToSpeech() {
        voiceMic = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                Locale locale = new Locale("pt", "BR");
                voiceMic.setLanguage(locale);
                speak("Tela de Suporte");

            }

        });
    }

    private void speak(String speak) {
        voiceMic.speak(speak, TextToSpeech.QUEUE_FLUSH, null);
    }

}

