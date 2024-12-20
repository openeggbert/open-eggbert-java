package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

public class AccelerometerFactory {

    public static Accelerometer Create() {
        return new AccelerometerDummyImpl();
    }
}
