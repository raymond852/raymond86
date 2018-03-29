package com.raymond86;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by hy110831 on 21/10/2017.
 */

public class CarController {

    private static final String TCP_SERVER_HOST = "192.168.4.1";
    private static final int TCP_SERVER_PORT = 8888;
    private final String TAG = this.getClass().getSimpleName();

    private Executor executor;
    private Socket socket;
    private CountDownTimer timer;
    private ConnectListener listener;
    private Handler uiHandler;

    public CarController(ConnectListener listener, Handler handler) {
        executor = Executors.newSingleThreadExecutor();
        connect();
        uiHandler = handler;
        timer = new CountDownTimer(3000, 500) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!socket.isConnected()) {
                    connect();
                }
            }
        };
        this.listener = listener;
    }

    public void sendCommand(final Command command) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (socket != null && socket.isConnected()) {
                    byte[] commandToBytes = {command.getLeftVal(), command.getRightVal(), command.getForwardVal(), command.getBackwardVal(), (byte) 0xff};
                    try {
                        socket.getOutputStream().write(commandToBytes);
                    } catch (IOException e) {
                        Log.e(TAG, "send command failed with exception:", e);
                    }
                }
            }
        });
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public void connect() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (socket == null) {
                    try {
                        socket = new Socket();
                        socket.connect(new InetSocketAddress(TCP_SERVER_HOST, TCP_SERVER_PORT), 5000);
                    } catch (Exception e) {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.connectStatusChanged(isConnected());
                                }
                            }
                        });
                        Log.e(TAG, "connect failed with exception:", e);
                        timer.cancel();
                        timer.start();
                    }
                } else if (!socket.isConnected()) {
                    try {
                        socket.close();
                        socket = new Socket();
                        socket.connect(new InetSocketAddress(TCP_SERVER_HOST, TCP_SERVER_PORT), 5000);
                    } catch (Exception e) {
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.connectStatusChanged(isConnected());
                                }
                            }
                        });
                        Log.e(TAG, "connect failed with exception:", e);
                        timer.cancel();
                        timer.start();
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.connectStatusChanged(isConnected());
                        }
                    }
                });
            }
        });
    }

    public void disconnect() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (timer != null) {
                    timer.cancel();
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception ignored) {

                    }
                }

            }
        });
    }


    interface ConnectListener {
        public void connectStatusChanged(boolean isConnected);
    }

}
