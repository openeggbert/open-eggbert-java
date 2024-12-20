package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

// WindowsPhoneSpeedyBlupi, Version=1.0.0.5, Culture=neutral, PublicKeyToken=6db12cd62dbec439
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.namespace;

// WindowsPhoneSpeedyBlupi.TinyPoint
@namespace(name = "WindowsPhoneSpeedyBlupi")
@AllArgsConstructor
@NoArgsConstructor
public class TinyPoint extends com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.struct<TinyPoint> {

    public int X;

    public int Y;

    @Override
    public String toString() {
        return X + ";" + Y;
    }

    @Override
    public TinyPoint copy() {
        return new TinyPoint(X, Y);
    }
    

    @Override
    public TinyPoint reset() {
        this.X = 0;
        this.Y = 0;
        return this;
    }
}
