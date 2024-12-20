package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

import com.openeggbert.jdotnet.System.Event;
import com.openeggbert.jdotnet.System.EventHandler;
import com.openeggbert.jdotnet.System.EventHandlerImpl;



    public class AccelerometerDummyImpl implements Accelerometer
    {
        private final EventHandler<AccelerometerEventArgs> CurrentValueChangedInternal = new EventHandlerImpl<AccelerometerEventArgs>();
        public @Event EventHandler<AccelerometerEventArgs> CurrentValueChanged() {return CurrentValueChangedInternal;}

        public void Start()
        {
            //throw new AccelerometerFailedException();
        }

        public void Stop()
        {
            //throw new AccelerometerFailedException();
        }
    }
