package com.raymond86;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import com.xw.repo.BubbleSeekBar;


public class MainActivity extends AppCompatActivity implements JoystickListener, CarController.ConnectListener, View.OnClickListener, BubbleSeekBar.OnProgressChangedListener {

    TextView connectStatus;

    final CarController carController = new CarController(this, new Handler(Looper.getMainLooper()));
    final CommandAdapter adapter = new CommandAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Joystick joyStick = findViewById(R.id.joystick);
        connectStatus = findViewById(R.id.connect_status);
        MaterialFancyButton control = findViewById(R.id.control);
        control.setOnClickListener(this);
        joyStick.setJoystickListener(this);
    }

    @Override
    public void onDown() {

    }

    @Override
    public void onDrag(float degrees, float offset) {
        carController.sendCommand(adapter.getCommand(degrees, offset));
    }

    @Override
    public void onUp() {
        carController.sendCommand(Command.reset());
    }

    @Override
    public void connectStatusChanged(boolean isConnected) {
        if (isConnected) {
            connectStatus.setText(R.string.connected);
        } else {
            connectStatus.setText(R.string.disconnected);
        }
    }

    @Override
    public void onClick(View v) {
        showGameControlDialog();
    }

    private void showGameControlDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_settings, false)
                .show();
        BubbleSeekBar speedSettings = (BubbleSeekBar) dialog.findViewById(R.id.speed_settings);
        speedSettings.setProgress(CommandAdapter.getMaxSpeed() * speedSettings.getMax() / 255.0f);
        BubbleSeekBar orientationSettings = (BubbleSeekBar) dialog.findViewById(R.id.orientation_settings);
        orientationSettings.setProgress(CommandAdapter.getMaxOrientation() * orientationSettings.getMax() / 255.0f);
        speedSettings.setOnProgressChangedListener(this);
        orientationSettings.setOnProgressChangedListener(this);
    }

    @Override
    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
        if (bubbleSeekBar.getId() == R.id.speed_settings) {
            CommandAdapter.setMaxSpeed(progressFloat / bubbleSeekBar.getMax() * 255.0f);
        } else if (bubbleSeekBar.getId() == R.id.orientation_settings) {
            CommandAdapter.setMaxOrientation(progressFloat / bubbleSeekBar.getMax() * 255.0f);
        }
    }

    @Override
    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

    }

    @Override
    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

    }
}
