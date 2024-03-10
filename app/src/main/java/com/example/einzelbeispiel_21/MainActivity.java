package com.example.einzelbeispiel_21;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private final String hostName = "se2-submission.aau.at";
    private final int portNumber = 20080;
    private EditText nummer;
    private TextView serverReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.new_layout);

        Button button = findViewById(R.id.button);
        button.setEnabled(true);
        nummer = findViewById(R.id.nummer);
        Button buttonBerechnung = findViewById(R.id.Berechnung);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onButtonClick();
            }
        });

        serverReply = findViewById(R.id.server_reply);

        buttonBerechnung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonBerechnungClick();
            }
        });
    }
    protected void onButtonBerechnungClick(){
        String berechnungInString = nummer.getText().toString();

        //int modulo = Integer.parseInt(berechnungInString) % 7;
        int martikelNummer = Integer.parseInt(berechnungInString);
        int sumOfDigits = 0;
        while (martikelNummer >0){
            sumOfDigits += martikelNummer % 10;
            martikelNummer = martikelNummer/10;
        }
        String binarySumOfDigits = Integer.toBinaryString(sumOfDigits);
        serverReply.setText(binarySumOfDigits);

    }

    protected void onButtonClick (){

        String numberInString = nummer.getText().toString();

            new Thread (new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket newSocket = new Socket(hostName, portNumber);

                        PrintWriter out = new PrintWriter(newSocket.getOutputStream(),true);
                        out.println(numberInString);
                        BufferedReader in = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
                        //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                        String server_reply = in.readLine();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                serverReply.setText(server_reply);
                            }
                        });

                        out.close();
                        in.close();
                        newSocket.close();

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }).start();
    }
}