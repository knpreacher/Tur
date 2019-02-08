package com.dev.turret.knp.tur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class MainActivity extends AppCompatActivity {
    Button btnConnect;
    ImageView iv;
    private String TAG = "SOCKET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnect = findViewById(R.id.btnConnect);
        iv = findViewById(R.id.imageView);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URI uri = null;
                try {
                    uri = new URI("ws://192.168.2.51/ws/");
                    WebSocketClient socketClient = new WebSocketClient(uri) {
                        @Override
                        public void onOpen() {
                            Log.i(TAG, "onOpen: OPENED");
                        }

                        @Override
                        public void onTextReceived(String message) {
                            Log.i(TAG, "onTextReceived: "+ message);

                        }

                        @Override
                        public void onBinaryReceived(final byte[] data) {
                            Log.i(TAG, "onBinReceived: " + data[0]);
                            Log.i(TAG, "onBinReceived: " + data[data.length-1]);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inMutable = true;
//                                    options.inPreferredConfig = Bitmap.Config.RGB_565;
//                                    Bitmap bmp = BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                                    if(bmp!=null) {
                                        Log.i(TAG, "run: null bmp");
                                        iv.setImageBitmap(bmp);
//                                        bmp.setWidth(300);
//                                        bmp.setHeight(300);
//                                        iv.setImageBitmap(Bitmap.createScaledBitmap(bmp, 300,
//                                                300, false));
                                    }
                                }
                            });


                        }

                        @Override
                        public void onPingReceived(byte[] data) {

                        }

                        @Override
                        public void onPongReceived(byte[] data) {

                        }

                        @Override
                        public void onException(Exception e) {

                        }

                        @Override
                        public void onCloseReceived() {
                            Log.i(TAG, "onCloseReceived: ");
                        }
                    };
                    socketClient.connect();
                } catch (URISyntaxException e) {
                    Toast.makeText(MainActivity.this, "URI error", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
