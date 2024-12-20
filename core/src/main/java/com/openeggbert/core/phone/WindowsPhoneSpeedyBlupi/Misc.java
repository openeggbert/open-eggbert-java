package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

// WindowsPhoneSpeedyBlupi, Version=1.0.0.5, Culture=neutral, PublicKeyToken=6db12cd62dbec439
// WindowsPhoneSpeedyBlupi.Misc
import com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi.Def.KeyboardPress;
import static com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.default_.default_;
import com.openeggbert.jdotnet.System.*;
import com.openeggbert.jxna.Microsoft.Xna.Framework.*;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Rectangle;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.out;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.namespace;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.static_;


@namespace(name = "WindowsPhoneSpeedyBlupi")
@static_
    public class Misc
    {
    private Misc() {
        //Not meant to be instantiated.
    }
        public static Rectangle RotateAdjust(Rectangle rect, double angle)
        {
             TinyPoint tinyPoint = default_(new TinyPoint());
            tinyPoint.X = rect.Width / 2;
            tinyPoint.Y = rect.Height / 2;
            TinyPoint p = tinyPoint;
            TinyPoint tinyPoint2 = RotatePointRad(angle, p);
            int num = tinyPoint2.X - p.X;
            int num2 = tinyPoint2.Y - p.Y;
            return new Rectangle(rect.Left() - num, rect.Top() - num2, rect.Width, rect.Height);
        }

        public static TinyPoint RotatePointRad(double angle, TinyPoint p)
        {
            return RotatePointRad(default_(new TinyPoint()), angle, p);
        }

        public static TinyPoint RotatePointRad(TinyPoint center, double angle, TinyPoint p)
        {
             TinyPoint tinyPoint = default_(new TinyPoint());
             TinyPoint result = default_(new TinyPoint());
            tinyPoint.X = p.X - center.X;
            tinyPoint.Y = p.Y - center.Y;
            double num = Math_.Sin(angle);
            double num2 = Math_.Cos(angle);
            result.X = (int)((double)tinyPoint.X * num2 - (double)tinyPoint.Y * num);
            result.Y = (int)((double)tinyPoint.X * num + (double)tinyPoint.Y * num2);
            result.X += center.X;
            result.Y += center.Y;
            return result;
        }

        public static double DegToRad(double angle)
        {
            return angle * Math_.PI / 180.0;
        }

        public static int Approch(int actual, int finalValue, int step)
        {
            if (actual < finalValue)
            {
                actual = Math_.Min(actual + step, finalValue);
            }
            else if (actual > finalValue)
            {
                actual = Math_.Max(actual - step, finalValue);
            }
            return actual;
        }

        public static int Speed(double speed, int max)
        {
            if (speed > 0.0)
            {
                return Math_.Max((int)(speed * (double)max), 1);
            }
            if (speed < 0.0)
            {
                return Math_.Min((int)(speed * (double)max), -1);
            }
            return 0;
        }

        public static TinyRect Inflate(TinyRect rect, int value)
        {
             TinyRect result = default_(new TinyRect());
            result.Left = rect.Left - value;
            result.Right = rect.Right + value;
            result.Top = rect.Top - value;
            result.Bottom = rect.Bottom + value;
            return result;
        }

        public static boolean IsInside(TinyRect rect, TinyPoint p)
        {
            if (p.X >= rect.Left && p.X <= rect.Right && p.Y >= rect.Top)
            {
                return p.Y <= rect.Bottom;
            }
            return false;
        }

        public static boolean IntersectRect(@out TinyRect dst, TinyRect src1, TinyRect src2)
        {
            dst = default_(dst);
            dst.Left = Math_.Max(src1.Left, src2.Left);
            dst.Right = Math_.Min(src1.Right, src2.Right);
            dst.Top = Math_.Max(src1.Top, src2.Top);
            dst.Bottom = Math_.Min(src1.Bottom, src2.Bottom);
            return !IsRectEmpty(dst);
        }

        public static boolean UnionRect(@out TinyRect dst, TinyRect src1, TinyRect src2)
        {
            
            dst = default_(dst);
            dst.Left = Math_.Min(src1.Left, src2.Left);
            dst.Right = Math_.Max(src1.Right, src2.Right);
            dst.Top = Math_.Min(src1.Top, src2.Top);
            dst.Bottom = Math_.Max(src1.Bottom, src2.Bottom);
            return !IsRectEmpty(dst);
        }

        private static boolean IsRectEmpty(TinyRect rect)
        {
            if (rect.Left < rect.Right)
            {
                return rect.Top >= rect.Bottom;
            }
            return true;
        }

        public static KeyboardPress intToKeyboardPress(int i) {
            switch (i)
            {
                case 0: return KeyboardPress.None;
                case 1: return KeyboardPress.Up;
                case 2: return KeyboardPress.Right;
                case 3: return KeyboardPress.Down;
                case 4: return KeyboardPress.Left;
                case 5: return KeyboardPress.LeftControl;
                case 6: return KeyboardPress.Space;
                case 7: return KeyboardPress.Escape;
                case 8: return KeyboardPress.Pause;
                default: throw new Exception_("Unsupported number for KeyboardPress: " + i);
            }
        }
        public static int keyboardPressToInt(KeyboardPress kp)
        {
            switch (kp)
            {
                case None: return 0;
                case Up: return 1;
                case Right: return 2;
                case Down: return 3;
                case Left: return 4;
                case LeftControl: return 5;
                case Space: return 6;
                case Escape: return 7;
                case Pause: return 8;
                default: throw new Exception_("Unsupported KeyboardPress: " + kp);
            }
        }
    }
