package br.com.nilles;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class SupportAct extends AppCompatActivity {

    private FloatingActionButton fab;
    private boolean doubleBackToExitPressedOnce;
    private TextToSpeech voiceMic;
    private Button button;
    private SpeechRecognizer speechRec;
    private Locale locale;
    private Handler mHandler = new Handler();
    //private GestureDetectorCompat gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_frag);

        locale = new Locale("pt", "BR");

        button = (Button)findViewById(R.id.btnBluetooth);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        //gesture = new GestureDetectorCompat(this, new LearnGesture());
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 2);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale);
                speechRec.startListening(intent);
            }
        });

        initializeTextToSpeech();
        initializeSpeechRecognizer();
    }

    /*public boolean onTouchEvent(MotionEvent event) {
        this.gesture.onTouchEvent(event);
        return super.onTouchEvent(event);
    } class LearnGesture extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float vX, float vY) {

            private float sense = 195;
            if(event2.getX() - event1.getX() > sense){

                Intent intent = new Intent(getApplicationContext(), GpsAct.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left, R.anim.anim_slide_out_torigth);
            } else if(event1.getX() - event2.getX() > sense){

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_rigth, R.anim.anim_slide_out_toleft);

            }
            return true;
        }
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        voiceMic.shutdown();
    }

    @Override
    protected void onResume() {
        initializeSpeechRecognizer();
        initializeTextToSpeech();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            voiceMic.shutdown();
        }
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(getApplicationContext())) {
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

        if (command.indexOf("sair") != -1) {
            finishAffinity();
        } else if (command.indexOf("mapa") != -1) {
            Intent intentMap = new Intent(getApplicationContext(), GpsAct.class);
            intentMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentMap);
            overridePendingTransition(R.anim.anim_left, R.anim.anim_slide_out_torigth);

        } else if (command.indexOf("menu") != -1) {
            Intent intentMenu = new Intent(getApplicationContext(), MainActivity.class);
            intentMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentMenu);
            overridePendingTransition(R.anim.anim_rigth, R.anim.anim_slide_out_toleft);
        }
    }

    private void initializeTextToSpeech() {
        voiceMic = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                voiceMic.setLanguage(locale);
                speak("Olá, esta é a janela de suporte, aqui você consegue ativar o bluetooth. pressione o botão de aúdio no canto inferior direito do seu celular. diga 'menu' ou 'mapa' para ir para as respectivas telas. Ou diga 'sair' para finalizar a aplicação");
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