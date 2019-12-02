package br.com.nilles;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    private static final int REQUEST_MICROPHONE = 100;
    private TextToSpeech voiceMic;
    private SpeechRecognizer speechRec;
    private Locale locale;
    private FloatingActionButton fab;
    //private GestureDetectorCompat gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locale = new Locale("pt", "BR");
        //gesture = new GestureDetectorCompat(this, new LearnGesture());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);
        }

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 2);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale);
                speechRec.startListening(speechIntent);
            }
        });

        initializeTextToSpeech();
        initializeSpeechRecognizer();
    }

    /*public boolean onTouchEvent(MotionEvent event) {
        this.gesture.onTouchEvent(event);
        return super.onTouchEvent(event);
    }*/

    /*class LearnGesture extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float vX, float vY) {

            private float sense = 195;
            if(event2.getX() - event1.getX() > sense){

                Intent intent = new Intent(getApplicationContext(), SupportAct.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left, R.anim.anim_slide_out_torigth);

            } else if(event1.getX() - event2.getX() > sense){

                Intent intent = new Intent(getApplicationContext(), GpsAct.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_rigth, R.anim.anim_slide_out_toleft);
            }
            return true;
        }
    }*/


    @Override
    protected void onPause() {
        voiceMic.shutdown();
        super.onPause();
    }

    @Override
    protected void onResume() {
        initializeSpeechRecognizer();
        initializeTextToSpeech();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            voiceMic.shutdown();
            super.onDestroy();
        }
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
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

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResults(Bundle bundle) {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void finalResults(String command) {

        command.toLowerCase(locale);

        if (command.indexOf("olá") != -1) {
            speak("Esta é a tela inicial da aplicação. Pressione o botão de aúdio localizado no canto inferior direito e diga 'Mapa' ou 'Suporte' para ir as respectivas telas. Ou diga 'sair' para finalizar a aplicação");
        } else if (command.indexOf("que horas são") != -1) {
            Date now = new Date();
            String time = DateUtils.formatDateTime(getApplicationContext(), now.getTime(), DateUtils.FORMAT_SHOW_TIME);
            speak("Agora são exatas:" + time);
        } else if (command.indexOf("sair") != -1) {
            finishAffinity();
        } else if (command.indexOf("tempo") != -1) {
            speak("O tempo agora");
        } else if (command.indexOf("mapa") != -1) {
            Intent intentMap = new Intent(getApplicationContext(), GpsAct.class);
            intentMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentMap);
            overridePendingTransition(R.anim.anim_rigth, R.anim.anim_slide_out_toleft);
        } else if (command.indexOf("suporte") != -1) {
            Intent intentSupport = new Intent(getApplicationContext(), SupportAct.class);
            intentSupport.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentSupport);
            overridePendingTransition(R.anim.anim_left, R.anim.anim_slide_out_torigth);
        }
    }

    private void initializeTextToSpeech() {
        voiceMic = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                voiceMic.setLanguage(locale);
                speak("Olá, meu nome é Nilees. Pressione o botão de aúdio localizado no canto inferior direito e diga 'Olá' para mais informações.");
            }
        });
    }

    private void speak(String message) {
        if (Build.VERSION.SDK_INT >= 21) {
            voiceMic.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            voiceMic.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione Duas Vezes para sair da aplicação", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(mRunnable, 2000);
    }
}