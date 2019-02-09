package com.dev.turret.knp.tur;

import android.app.Activity;
import android.content.Context;

import java.net.URI;

import tech.gusavila92.websocketclient.WebSocketClient;

public class TurretSocket extends WebSocketClient {
    interface TurretServerCallBack{
        void onConnect();
        void onText(String text);
        void onBin(byte[] data);
        void onDisconnect();
    }

    TurretServerCallBack callBack;
    Activity context;

    public TurretSocket(URI uri, Context context) {
        super(uri);
        this.context = (Activity) context;
        callBack = (TurretServerCallBack) context;
    }

    @Override
    public void onOpen() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callBack.onConnect();
            }
        });
    }

    @Override
    public void onTextReceived(final String message) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callBack.onText(message);
            }
        });
    }

    @Override
    public void onBinaryReceived(final byte[] data) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callBack.onBin(data);
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
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callBack.onDisconnect();
            }
        });
    }
}
