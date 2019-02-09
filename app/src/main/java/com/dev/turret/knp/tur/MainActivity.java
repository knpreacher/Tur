package com.dev.turret.knp.tur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity implements TurretSocket.TurretServerCallBack {
    Button btnConnect;
    ImageView iv;
    private String TAG = "SOCKET";

    TurretSocket socket;
    AlertDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waitDialog = Dialogs.getWaitDialog(this);
        waitDialog.show();

        try {
            URI uri = new URI("ws://192.168.43.96/ws/");
            socket = new TurretSocket(uri, this);
            socket.connect();
        } catch (URISyntaxException e) {
            //e.printStackTrace();
            waitDialog.dismiss();
            Toast.makeText(this, "Ошибка адреса", Toast.LENGTH_SHORT).show();
        }

        btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setVisibility(View.INVISIBLE);
        iv = findViewById(R.id.imageView);
        iv.setVisibility(View.INVISIBLE);

//        btnConnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        btnConnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                URI uri = null;
//                try {
//                    uri = new URI("ws://192.168.43.96/ws/");
//                    WebSocketClient socketClient = new WebSocketClient(uri) {
//                        @Override
//                        public void onOpen() {
//                            Log.i(TAG, "onOpen: OPENED");
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    iv.setVisibility(View.VISIBLE);
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onTextReceived(String message) {
//                            Log.i(TAG, "onTextReceived: "+ message);
//                        }
//
//                        @Override
//                        public void onBinaryReceived(final byte[] data) {
//                            Log.i(TAG, "onBinReceived: " + data[0]);
//                            Log.i(TAG, "onBinReceived: " + data[data.length-1]);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    BitmapFactory.Options options = new BitmapFactory.Options();
//                                    options.inMutable = true;
////                                    options.inPreferredConfig = Bitmap.Config.RGB_565;
////                                    Bitmap bmp = BitmapFactory.decodeStream(new ByteArrayInputStream(data));
//                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);
//                                    if(bmp!=null) {
//                                        Log.i(TAG, "run: null bmp");
//                                        iv.setImageBitmap(bmp);
////                                        bmp.setWidth(300);
////                                        bmp.setHeight(300);
////                                        iv.setImageBitmap(Bitmap.createScaledBitmap(bmp, 300,
////                                                300, false));
//                                    }
//                                }
//                            });
//
//
//                        }
//
//                        @Override
//                        public void onPingReceived(byte[] data) {
//
//                        }
//
//                        @Override
//                        public void onPongReceived(byte[] data) {
//
//                        }
//
//                        @Override
//                        public void onException(Exception e) {
//
//                        }
//
//                        @Override
//                        public void onCloseReceived() {
//                            Log.i(TAG, "onCloseReceived: ");
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    iv.setVisibility(View.INVISIBLE);
//                                }
//                            });
//
//                        }
//                    };
//                    socketClient.connect();
//                } catch (URISyntaxException e) {
//                    Toast.makeText(MainActivity.this, "URI error", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

    }

    private void updateImage(byte[] data){
        BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inMutable = true;
                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                                    if(bmp!=null) {
                                        //Log.i(TAG, "run: null bmp");
                                        iv.setImageBitmap(bmp);
//                                        bmp.setWidth(300);
//                                        bmp.setHeight(300);
//                                        iv.setImageBitmap(Bitmap.createScaledBitmap(bmp, 300,
//                                                300, false));
                                    }
    }

    @Override
    public void onConnect() {
        waitDialog.dismiss();
        iv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBin(byte[] data) {
        updateImage(data);
    }

    @Override
    public void onDisconnect() {
        waitDialog.dismiss();
        Dialogs.showYesNoDialog(this,
                "Соединение разорвано",
                "Выполнить повторное подключение?",
                new Dialogs.YesNoDialogCallBack() {
                    @Override
                    public void onYes() {
                        waitDialog.show();
                        socket.connect();
                    }

                    @Override
                    public void onNo() {
                        MainActivity.this.finish();
                    }
                });
    }
}
