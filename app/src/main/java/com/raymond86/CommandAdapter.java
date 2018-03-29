package com.raymond86;


import android.util.Log;

/**
 * Created by hy110831 on 21/10/2017.
 */
public class CommandAdapter {
    
    private static float MAX_SPEED = 255.0f;
    private static float MAX_ORIENTATION = 255.0f;

    public Command getCommand(float horizontal, float vertical) {
        Byte forward = 0;
        Byte backward = 0;
        Byte left = 0;
        Byte right = 0;
        if (vertical > 0) {
            forward = (byte) ((int) Math.floor(Math.abs(vertical * MAX_SPEED - 0.5)));
        } else if (vertical < 0){
            backward = (byte) ((int) Math.floor(Math.abs(vertical * MAX_SPEED + 0.5)));
        }

        if (horizontal > 0) {
            right = (byte) ((int) Math.floor(Math.abs(horizontal * MAX_ORIENTATION - 0.5)));
        } else if (horizontal < 0){
            left = (byte) ((int) Math.floor(Math.abs(horizontal * MAX_ORIENTATION + 0.5)));
        }
        Log.d("tag", "forward:" + (forward & 0xff) + " backward:" + (backward & 0xff) + " left:" + (left & 0xff) + " right:" + (right & 0xff));
        return new Command(forward, backward, left, right);
    }

    public static void setMaxSpeed(float maxSpeed) {
        MAX_SPEED = maxSpeed;
    }


    public static void setMaxOrientation(float maxOrientation) {
        MAX_ORIENTATION = maxOrientation;
    }

    public static float getMaxSpeed() {
        return MAX_SPEED;
    }

    public static float getMaxOrientation() {
        return MAX_ORIENTATION;
    }
}
