package br.com.nilles;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.Locale;

public class PermissionAct extends AppCompatActivity {

    private Button btnAccept;
    private String prevStarted = "prevStarted";
    private TextToSpeech voiceMic;
    private SpeechRecognizer speechRec;
    private Locale locale;

    @Override
    protected void onResume() {
        super.onResume();
        voiceMic.shutdown();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_layout);

        locale = new Locale("pt", "BR");

        btnAccept = (Button)findViewById(R.id.accept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(PermissionAct.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent =  new Intent(PermissionAct.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PermissionAct.this);
                                    builder.setTitle("Permission Denied")
                                            .setMessage("Permissão para acessar a localização foi negada. Você necessita aceitar par utilizar aplicação")
                                            .setNegativeButton("Cancel",null)
                                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                }
                                            })
                                            .show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Permission Denied", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .check();
            }
        });
        initializeTextToSpeech();
    }

    // devo fazer algo para que o audio fosse executado apneas uma vez
    private void initializeTextToSpeech() {
        voiceMic = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(voiceMic.getEngines().size() == 0) {
                    Toast.makeText(PermissionAct.this, "error", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    voiceMic.setLanguage(locale);
                    speak("This screen ask to you a permission to find your location");
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
}
