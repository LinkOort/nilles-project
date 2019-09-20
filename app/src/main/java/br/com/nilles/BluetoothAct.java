package br.com.nilles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BluetoothAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_frag);

        Button button = (Button)findViewById(R.id.btnBluetooth);
        button.setOnClickListener(new View.OnClickListener() {
            @Override

           //just a test

            public void onClick(View v) {
                finish();
            }
        });
    }
}
