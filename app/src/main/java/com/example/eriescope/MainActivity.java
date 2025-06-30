package com.example.eriescope;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        TextView eriescopes = findViewById(R.id.eriescopes);
        eriescopes.setText("Welcome from code");

        Button homeBtn = findViewById(R.id.home_btn);
        homeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    try {
                        Socket socket = new Socket("www.android.com", 80);
                        socket.setSoTimeout(5000);
                        OutputStream outputStream = socket.getOutputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String request = "GET / HTTP/1.1\r\nHost: www.android.com\r\nUser-Agent: app\r\nAccept: */*\r\n\r\n";
                        outputStream.write(request.getBytes());
                        outputStream.flush();

                        StringBuilder sb = new StringBuilder();

                        String line;
                        while ((line = reader.readLine()) != null) {
                            String finalLine = line;
                            runOnUiThread(() -> eriescopes.append(finalLine));
                        }
                        String result = sb.toString();
                        outputStream.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
            }
        });

    }
}