package com.raymond86;

/**
 * Created by hy110831 on 21/10/2017.
 */

public class Command {

    private byte forwardVal;
    private byte backwardVal;
    private byte leftVal;
    private byte rightVal;

    public Command(byte forwardVal, byte backwardVal, byte leftVal, byte rightVal) {
        this.forwardVal = forwardVal;
        this.backwardVal = backwardVal;
        this.leftVal = leftVal;
        this.rightVal = rightVal;
    }

    public static Command reset() {
        return new Command((byte)0, (byte)0, (byte)0, (byte)0);
    }

    public byte getForwardVal() {
        return forwardVal;
    }

    public void setForwardVal(byte forwardVal) {
        this.forwardVal = forwardVal;
    }

    public byte getBackwardVal() {
        return backwardVal;
    }

    public void setBackwardVal(byte backwardVal) {
        this.backwardVal = backwardVal;
    }

    public byte getLeftVal() {
        return leftVal;
    }

    public void setLeftVal(byte leftVal) {
        this.leftVal = leftVal;
    }

    public byte getRightVal() {
        return rightVal;
    }

    public void setRightVal(byte rightVal) {
        this.rightVal = rightVal;
    }
}
