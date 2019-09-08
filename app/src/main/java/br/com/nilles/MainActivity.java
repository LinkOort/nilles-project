package br.com.nilles;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MICROPHONE = 360;
    private TabLayout tabLayout;
    private TextToSpeech voiceMic;
    private SpeechRecognizer speechRec;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //pedindo permissão para o ativar o audio.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 2);
                speechRec.startListening(intent);
            }
        });


        tabLayout = (TabLayout) findViewById(R.id.tabBar);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        //alterar o nome das TABS

        tabLayout.addTab(tabLayout.newTab().setText("Menu"));
        tabLayout.addTab(tabLayout.newTab().setText("GPS"));
        tabLayout.addTab(tabLayout.newTab().setText("Support"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        initializeTextToSpeech();
        initializeSpeechReconizer();
    }

    private void initializeSpeechReconizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)){
            //aqui criamos o método de reconhecimento de voz
            speechRec = SpeechRecognizer.createSpeechRecognizer(this);
            speechRec.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    //mantemos o foco aqui neste método
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    finalResults(results.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void finalResults(String command) {
        command = command.toLowerCase();


        if(command.indexOf("what") != -1){
            if(command.indexOf("your name") != -1) {
                speak("My name is Nilles.");
            }
        }
        if(command.indexOf("hello") != -1){
                speak("Hi, I'm Nilles, your new voice assistent. How can I help you?");
        }

        if (command.indexOf("time") != -1) {
            Date now = new Date();
            String time = DateUtils.formatDateTime(this, now.getTime(),DateUtils.FORMAT_SHOW_TIME);
            speak("agora são" + time);
        }
        if (command.indexOf("maps") != -1) {
            Intent maps = new Intent(this, GpsAct.class);
            speak("Tela de GPS" );
            startActivity(maps);
        }
        else if (command.indexOf("support") != -1) {
            Intent support = new Intent(this, SupportAct.class);
            speak("Tela de Suporte" );
            startActivity(support);
        }
    }

    private void initializeTextToSpeech() {
        voiceMic = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(voiceMic.getEngines().size() == 0) {
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    voiceMic.setLanguage(Locale.US);
                    speak("Hi! Welcome, press the voice button and say 'Hello' to start.");
                }
            }
        });
    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21) {
            voiceMic.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            voiceMic.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        voiceMic.shutdown();
    }

}
