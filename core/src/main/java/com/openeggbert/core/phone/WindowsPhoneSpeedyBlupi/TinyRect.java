package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

// WindowsPhoneSpeedyBlupi, Version=1.0.0.5, Culture=neutral, PublicKeyToken=6db12cd62dbec439\
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.namespace;
import lombok.ToString;

// WindowsPhoneSpeedyBlupi.TinyRect
@namespace(name = "WindowsPhoneSpeedyBlupi")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TinyRect extends com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.struct<TinyRect> {

    public int Left;

    public int Right;

    public int Top;

    public int Bottom;

    public int Width() {
        return Right - Left;
    }

    public int Height() {
        return Bottom - Top;

    }

    @Override
    public TinyRect copy() {
        return new TinyRect(Left, Right, Top, Bottom);
    }

    @Override
    public TinyRect reset() {
        this.Left = 0;
        this.Right = 0;
        this.Top = 0;
        this.Bottom = 0;
        return this;
    }
}
