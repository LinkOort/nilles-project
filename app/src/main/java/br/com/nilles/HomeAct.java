package br.com.nilles;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;;import java.util.Locale;

public class HomeAct extends Fragment {
    TextToSpeech voiceMic;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initializeTextToSpeech();
        return inflater.inflate(R.layout.home_frag, container, false);

    }
    private void initializeTextToSpeech() {
        voiceMic = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                Locale locale = new Locale("pt", "BR");
                voiceMic.setLanguage(locale);
                speak("Fala baianionho");

            }

        });
    }
    private void speak(String speak) {
        voiceMic.speak(speak, TextToSpeech.QUEUE_FLUSH, null);
    }
}
