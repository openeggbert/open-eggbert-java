package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

import com.openeggbert.jdotnet.System.*;

public interface Accelerometer {

    void Start();

    void Stop();

    @Event
    EventHandler<AccelerometerEventArgs> CurrentValueChanged();
}
