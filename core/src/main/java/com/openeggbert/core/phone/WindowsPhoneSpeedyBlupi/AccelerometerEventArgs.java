package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

import com.openeggbert.jdotnet.System.EventArgs;

import lombok.Getter;

public class AccelerometerEventArgs extends EventArgs {

    public float X() {return getX();}
    public float Y() {return getY();}
    public float Z() {return getZ();}
    @Getter
    private float X;
    @Getter
    private float Y;
    @Getter
    private float Z;

    public AccelerometerEventArgs(float x, float y, float z) {
        X = x;
        Y = y;
        Z = z;
    }
}
