package br.com.nilles;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.text.format.DateUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SupportAct extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce;
    private TextToSpeech voiceMic;
    private Button button;
    private SpeechRecognizer speechRec;
    private Locale localel;
    private Handler mHandler = new Handler();
    private GestureDetectorCompat gesture;
    private static int TIME_OUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_frag);

        button = (Button)findViewById(R.id.btnBluetooth);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        localel = new Locale("pt", "BR");

        gesture = new GestureDetectorCompat(this, new LearnGesture());


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 2);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, localel);
                speechRec.startListening(intent);
            }
        });

        initializeTextToSpeech();
        initializeSpeechRecognizer();
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.gesture.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float vX, float vY) {

            float sense = 60;

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

    }

    @Override
    protected void onPause() {
        super.onPause();
        voiceMic.shutdown();
    }

    //quando a aplicação é "minimizada" com este método as ações de inicializar o reconhecimento de voz e texto serão reativadas
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
        //aqui é confirmado se o microfone está disponível para que o reconhecimento comece
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

                //aqui é onde irémos pegar o resultado do que foi falado pelo usuário
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    //metodo finalResults é chamado aqui
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

        //aqui no método é instanciado algumas funções de voz da aplicação (pretendo aprimorar depois e trocar os "if" por switch case)

        Locale locale = new Locale("pt", "BR");
        command = command.toLowerCase(localel);

        if (command.indexOf("olá") != -1) {
            speak("Eu sou Nilees, sua nova assistente de voz, como posso lhe ajudar?");
        }
        if (command.indexOf("que horas são") != -1) {
            Date now = new Date();
            String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
            speak("Agora são exatas:" + time);
        }
        if (command.indexOf("sair") != -1) {
            finishAffinity();
        }
        if (command.indexOf("tempo") != -1) {
            speak("O tempo agora");
        }
        if (command.indexOf("mapa") != -1) {
            Intent intentMapa1 = new Intent(getApplicationContext(), GpsAct.class);
            intentMapa1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentMapa1);
        }
        if (command.indexOf("menu") != -1) {
            Intent intentMenu1 = new Intent(getApplicationContext(), MainActivity.class);
            intentMenu1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentMenu1);

        }
    }

    private void initializeTextToSpeech() {
        voiceMic = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                Locale locale = new Locale("pt", "BR");
                voiceMic.setLanguage(locale);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speak("está é a janela de suporte, aqui você consegue ativar o bluetooth.");
                    }
                }, TIME_OUT);
                speak("pressione o botão de aúdio e diga");

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