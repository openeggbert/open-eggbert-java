package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

// WindowsPhoneSpeedyBlupi, Version=1.0.0.5, Culture=neutral, PublicKeyToken=6db12cd62dbec439
// WindowsPhoneSpeedyBlupi.InputPad
import com.openeggbert.jdotnet.System.UnauthorizedAccessException;
import com.openeggbert.jdotnet.System.*;
import com.openeggbert.jdotnet.System.Collections.Generic.*;
import com.openeggbert.jdotnet.System.Diagnostics.*;
import com.openeggbert.jdotnet.System.Linq.*;
import static com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi.Def.*;
import static com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.default_.default_;
import com.openeggbert.jdotnet.JDotNet.Microsoft.Devices.Sensors.AccelerometerFailedException;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.ButtonState;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.Keyboard;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.KeyboardState;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.Keys;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.Mouse;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.MouseState;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.Touch.TouchCollection;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.Touch.TouchLocation;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.Touch.TouchLocationState;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.Touch.TouchPanel;
import lombok.Getter;
import lombok.Setter;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.readonly;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.namespace;

    @namespace(name = "WindowsPhoneSpeedyBlupi")

    public class InputPad
    {
        private static @readonly final int padSize = 140;

        private @readonly final Game1 game1;

        private @readonly final Decor decor;

        private @readonly final Pixmap pixmap;

        private @readonly final Sound sound;

        private @readonly final GameData gameData;

        private @readonly final List_<Def.ButtonGlygh> pressedGlyphs;

        private @readonly final Accelerometer accelSensor;

        private @readonly final Slider accelSlider;

        private boolean padPressed;

        private boolean showCheatMenu;

        private TinyPoint padTouchPos;

        private Def.ButtonGlygh lastButtonDown;

        private Def.ButtonGlygh buttonPressed;

        private int touchCount;

        private boolean accelStarted;

        private boolean accelActive;

        private double accelSpeedX;

        private boolean accelLastState;

        private boolean accelWaitZero;

        private int mission;

        @Getter @Setter
        public Def.Phase Phase;
        
        @Getter @Setter
        public int SelectedGamer;

        @Getter @Setter
        public TinyPoint PixmapOrigin;

        public int TotalTouch() {
            return getTotalTouch();
        }

        public int getTotalTouch() {
            return touchCount;
        }

        public Def.ButtonGlygh ButtonPressed() {
            Def.ButtonGlygh result = buttonPressed;
            buttonPressed = Def.ButtonGlygh.None;
            return result;
        }
  
        public boolean ShowCheatMenu() {
            return isShowCheatMenu();
        }

        public boolean isShowCheatMenu() {
            return showCheatMenu;
        }

        public void setShowCheatMenu(boolean showCheatMenu) {
            this.showCheatMenu = showCheatMenu;
        }

        public List_<Def.ButtonGlygh> ButtonGlyphs() {
                List_<Def.ButtonGlygh> buttonGlyphs = new List_<>();
            
                switch (Phase)
                {
                    case Init:
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.InitGamerA);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.InitGamerB);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.InitGamerC);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.InitSetup);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.InitPlay);
                        if (game1.IsTrialMode())
                        {
                            buttonGlyphs.YieldReturn(Def.ButtonGlygh.InitBuy);
                        }
                        if (game1.IsRankingMode())
                        {
                            buttonGlyphs.YieldReturn(Def.ButtonGlygh.InitRanking);
                        }
                        break;
                    case Play:
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.PlayPause);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.PlayAction);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.PlayJump);
                        if (accelStarted)
                        {
                            buttonGlyphs.YieldReturn(Def.ButtonGlygh.PlayDown);
                        }
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat11);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat12);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat21);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat22);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat31);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat32);
                        break;
                    case Pause:
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.PauseMenu);
                        if (mission != 1)
                        {
                            buttonGlyphs.YieldReturn(Def.ButtonGlygh.PauseBack);
                        }
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.PauseSetup);
                        if (mission != 1 && mission % 10 != 0)
                        {
                            buttonGlyphs.YieldReturn(Def.ButtonGlygh.PauseRestart);
                        }
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.PauseContinue);
                        break;
                    case Resume:
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.ResumeMenu);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.ResumeContinue);
                        break;
                    case Lost:
                    case Win:
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.WinLostReturn);
                        break;
                    case Trial:
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.TrialBuy);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.TrialCancel);
                        break;
                    case MainSetup:
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.SetupSounds);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.SetupJump);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.SetupZoom);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.SetupAccel);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.SetupReset);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.SetupReturn);
                        break;
                    case PlaySetup:
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.SetupSounds);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.SetupJump);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.SetupZoom);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.SetupAccel);
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.SetupReturn);
                        break;
                    case Ranking:
                        buttonGlyphs.YieldReturn(Def.ButtonGlygh.RankingContinue);
                        break;
                }
                if (showCheatMenu)
                {
                    buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat1);
                    buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat2);
                    buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat3);
                    buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat4);
                    buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat5);
                    buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat6);
                    buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat7);
                    buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat8);
                    buttonGlyphs.YieldReturn(Def.ButtonGlygh.Cheat9);
                }
                return buttonGlyphs;
            
        }

        private TinyPoint PadCenter()
        {
                TinyRect drawBounds = pixmap.DrawBounds();
                if (gameData.isJumpRight())
                {
                     TinyPoint result = default_(new TinyPoint());
                    result.X = 100;
                    result.Y = drawBounds.Height() - 100;
                    return result;
                }
                 TinyPoint result2 = default_(new TinyPoint());
                result2.X = drawBounds.Width() - 100;
                result2.Y = drawBounds.Height() - 100;
                return result2;
            
        }

        public InputPad(Game1 game1, Decor decor, Pixmap pixmap, Sound sound, GameData gameData)
        {
            //IL_0037: Unknown result type (might be due to invalid IL or missing references)
            //IL_0041: Expected O, but got Unknown
            this.game1 = game1;
            this.decor = decor;
            this.pixmap = pixmap;
            this.sound = sound;
            this.gameData = gameData;
            pressedGlyphs = new List_<>();
            accelSensor = AccelerometerFactory.Create();
            accelSensor.CurrentValueChanged().addEventListener((e) -> HandleAccelSensorCurrentValueChanged(null, e));
            accelSlider = new Slider(
                new TinyPoint(320,400),
                this.gameData.AccelSensitivity()
            );
            lastButtonDown = Def.ButtonGlygh.None;
            buttonPressed = Def.ButtonGlygh.None;
        }

        public void StartMission(int mission)
        {
            this.mission = mission;
            accelWaitZero = true;
        }

        private TinyPoint createTinyPoint(int x, int y)
        {
            TinyPoint tinyPoint = new TinyPoint();
            tinyPoint.X = x;
            tinyPoint.Y = y;
            return tinyPoint;
        }
        public void Update()
        {
            pressedGlyphs.Clear();
            if (accelActive != gameData.AccelActive())
            {
                accelActive = gameData.AccelActive();
                if (accelActive)
                {
                    StartAccel();
                }
                else
                {
                    StopAccel();
                }
            }
            double horizontalChange = 0.0;
            double verticalChange = 0.0;
            int num3 = 0;
            padPressed = false;
            Def.ButtonGlygh buttonGlygh = Def.ButtonGlygh.None;
            TouchCollection touches = TouchPanel.GetState();
            touchCount = touches.Count;
            List_<TinyPoint> touchesOrClicks = new List_<>();
            for (TouchLocation item : touches)
            {
                if (item.State == TouchLocationState.Pressed || item.State == TouchLocationState.Moved)
                {
                     TinyPoint tinyPoint = default_(new TinyPoint());
                    tinyPoint.X = (int)item.Position.X;
                    tinyPoint.Y = (int)item.Position.Y;
                    touchesOrClicks.Add(tinyPoint);
                }
            }

            MouseState mouseState = Mouse.GetState();
            if (mouseState.LeftButton() == ButtonState.Pressed)
            {
                touchCount++;
                TinyPoint click = new TinyPoint();
                click.X = mouseState.X();
                click.Y = mouseState.Y();
                touchesOrClicks.Add(click);
            }

            KeyboardState newState = Keyboard.GetState();
            {
                if (newState.IsKeyDown(Keys.LeftControl)) touchesOrClicks.Add(createTinyPoint(-1, Misc.keyboardPressToInt(KeyboardPress.LeftControl)));
                if (newState.IsKeyDown(Keys.Up)) touchesOrClicks.Add(createTinyPoint(-1, Misc.keyboardPressToInt(KeyboardPress.Up)));
                if (newState.IsKeyDown(Keys.Right)) touchesOrClicks.Add(createTinyPoint(-1, Misc.keyboardPressToInt(KeyboardPress.Right)));
                if (newState.IsKeyDown(Keys.Down)) touchesOrClicks.Add(createTinyPoint(-1, Misc.keyboardPressToInt(KeyboardPress.Down)));
                if (newState.IsKeyDown(Keys.Left)) touchesOrClicks.Add(createTinyPoint(-1, Misc.keyboardPressToInt(KeyboardPress.Left)));
                if (newState.IsKeyDown(Keys.Space)) touchesOrClicks.Add(createTinyPoint(-1, Misc.keyboardPressToInt(KeyboardPress.Space)));
            }
            if (newState.IsKeyDown(Keys.F11))
            {
                game1.ToggleFullScreen ();
                Debug.WriteLine("F11 was pressed.");
            }

            Boolean keyPressedUp = false;
            Boolean keyPressedDown = false;
            Boolean keyPressedLeft = false;
            Boolean keyPressedRight = false;
            for (TinyPoint touchOrClick : touchesOrClicks)
            {
                Boolean keyboardPressed = false;
                if (touchOrClick.X == -1)
                {
                    keyboardPressed = true;
                }
                KeyboardPress keyboardPress = keyboardPressed ? Misc.intToKeyboardPress(touchOrClick.Y) : KeyboardPress.None;
                keyPressedUp = keyboardPress == KeyboardPress.Up ? true : keyPressedUp;
                keyPressedDown = keyboardPress == KeyboardPress.Down ? true : keyPressedDown;
                keyPressedLeft = keyboardPress == KeyboardPress.Left ? true : keyPressedLeft;
                keyPressedRight = keyboardPress == KeyboardPress.Right ? true : keyPressedRight;

                {
                    TinyPoint tinyPoint2 = keyboardPressed ? createTinyPoint(1, 1) : touchOrClick;
                    if (!accelStarted && Misc.IsInside(GetPadBounds(PadCenter(), padSize), tinyPoint2))
                    {
                        padPressed = true;
                        padTouchPos = tinyPoint2;
                    }
                    if (keyboardPress == KeyboardPress.Up || keyboardPress == KeyboardPress.Right || keyboardPress == KeyboardPress.Down || keyboardPress == KeyboardPress.Left)
                    {
                        padPressed = true;
                    }
                    Debug.WriteLine("padPressed=" + padPressed);
                    Def.ButtonGlygh buttonGlygh2 = ButtonDetect(tinyPoint2);
                    Debug.WriteLine("buttonGlyph2 =" + buttonGlygh2);
                    if (buttonGlygh2.ordinal() != 0)
                    {
                        pressedGlyphs.Add(buttonGlygh2);
                    }
                    if (keyboardPressed)
                    {
                        switch (keyboardPress)
                        {
                            case LeftControl: buttonGlygh2 = Def.ButtonGlygh.PlayJump; pressedGlyphs.Add(buttonGlygh2); break;
                            case Space: buttonGlygh2 = Def.ButtonGlygh.PlayAction; pressedGlyphs.Add(buttonGlygh2); break;
                        }
                    }

                    if ((Phase == Def.Phase.MainSetup || Phase == Def.Phase.PlaySetup) && accelSlider.Move(tinyPoint2))
                    {
                        gameData.setAccelSensitivity(accelSlider.Value);
                    }
                    switch (buttonGlygh2)
                    {
                        case PlayJump:
                            Debug.WriteLine("Jumping detected");
                            accelWaitZero = false;
                            num3 |= 1;
                            break;
                        case PlayDown:
                            accelWaitZero = false;
                            num3 |= 4;
                            break;
                        case InitGamerA:
                        case InitGamerB:
                        case InitGamerC:
                        case InitSetup:
                        case InitPlay:
                        case InitBuy:
                        case InitRanking:
                        case WinLostReturn:
                        case TrialBuy:
                        case TrialCancel:
                        case SetupSounds:
                        case SetupJump:
                        case SetupZoom:
                        case SetupAccel:
                        case SetupReset:
                        case SetupReturn:
                        case PauseMenu:
                        case PauseBack:
                        case PauseSetup:
                        case PauseRestart:
                        case PauseContinue:
                        case PlayPause:
                        case PlayAction:
                        case ResumeMenu:
                        case ResumeContinue:
                        case RankingContinue:
                        case Cheat11:
                        case Cheat12:
                        case Cheat21:
                        case Cheat22:
                        case Cheat31:
                        case Cheat32:
                        case Cheat1:
                        case Cheat2:
                        case Cheat3:
                        case Cheat4:
                        case Cheat5:
                        case Cheat6:
                        case Cheat7:
                        case Cheat8:
                        case Cheat9:
                            accelWaitZero = false;
                            buttonGlygh = buttonGlygh2;
                            showCheatMenu = false;
                            break;
                    }
                }
            }
            if (buttonGlygh.ordinal() != 0 && buttonGlygh != Def.ButtonGlygh.PlayAction && buttonGlygh != Def.ButtonGlygh.Cheat11 && buttonGlygh != Def.ButtonGlygh.Cheat12 && buttonGlygh != Def.ButtonGlygh.Cheat21 && buttonGlygh != Def.ButtonGlygh.Cheat22 && buttonGlygh != Def.ButtonGlygh.Cheat31 && buttonGlygh != Def.ButtonGlygh.Cheat32 && lastButtonDown == Def.ButtonGlygh.None)
            {
                 TinyPoint tinyPoint3 = default_(new TinyPoint());
                tinyPoint3.X = 320;
                tinyPoint3.Y = 240;
                TinyPoint pos = tinyPoint3;
                sound.PlayImage(0, pos);
            }
            if (buttonGlygh == Def.ButtonGlygh.None && lastButtonDown.ordinal() != 0)
            {
                buttonPressed = lastButtonDown;
            }
            lastButtonDown = buttonGlygh;
            if (padPressed)
            {
                Debug.WriteLine("PadCenter.X=" + PadCenter().X);
                Debug.WriteLine("PadCenter.Y=" + PadCenter().Y);
                Debug.WriteLine("padTouchPos.X=" + padTouchPos.X);
                Debug.WriteLine("padTouchPos.Y=" + padTouchPos.Y);
                Debug.WriteLine("keyPressedUp=" + keyPressedUp);
                Debug.WriteLine("keyPressedDown=" + keyPressedDown);
                Debug.WriteLine("keyPressedLeft=" + keyPressedLeft);
                Debug.WriteLine(" keyPressedRight=" + keyPressedRight);
                {
                    if (keyPressedUp)
                    {
                        padTouchPos.Y = PadCenter().Y - 30;
                        padTouchPos.X = PadCenter().X;
                        if (keyPressedLeft) padTouchPos.X = PadCenter().X - 30;
                        if (keyPressedRight) padTouchPos.X = PadCenter().X + 30;
                    }
                    if (keyPressedDown) { 
                        padTouchPos.Y = PadCenter().Y + 30;
                        padTouchPos.X = PadCenter().X;
                        if (keyPressedLeft) padTouchPos.X = PadCenter().X - 30;
                        if (keyPressedRight) padTouchPos.X = PadCenter().X + 30;
                    }
                    if (keyPressedLeft) { 
                        padTouchPos.X = PadCenter().X - 30;
                        padTouchPos.Y = PadCenter().Y;
                        if (keyPressedUp) padTouchPos.Y = PadCenter().Y - 30;
                        if (keyPressedDown) padTouchPos.Y = PadCenter().Y + 30;
                    }
                    if (keyPressedRight) { 
                        padTouchPos.X = PadCenter().X + 30;
                        padTouchPos.Y = PadCenter().Y;
                        if (keyPressedUp) padTouchPos.Y = PadCenter().Y - 30;
                        if (keyPressedDown) padTouchPos.Y = PadCenter().Y + 30;
                    }
                }
                double horizontalPosition = padTouchPos.X - PadCenter().X;
                double verticalPosition = padTouchPos.Y - PadCenter().Y;

                if (horizontalPosition > 20.0)
                {
                    horizontalChange += 1.0;
                    Debug.WriteLine(" horizontalChange += 1.0;");
                }
                if (horizontalPosition < -20.0)
                {
                    horizontalChange -= 1.0;
                    Debug.WriteLine(" horizontalChange -= 1.0;");

                }
                if (verticalPosition > 20.0)
                {
                    verticalChange += 1.0;
                    Debug.WriteLine(" verticalPosition += 1.0;");

                }
                if (verticalPosition < -20.0)
                {
                    verticalChange -= 1.0;
                    Debug.WriteLine(" verticalPosition -= 1.0;");
                }

            }
            if (accelStarted)
            {
                horizontalChange = accelSpeedX;
                verticalChange = 0.0;
                if ((num3 & 4) != 0)
                {
                    verticalChange = 1.0;
                }
            }
            decor.SetSpeedX(horizontalChange);
            decor.SetSpeedY(verticalChange);
            decor.KeyChange(num3);
        }

        private Def.ButtonGlygh ButtonDetect(TinyPoint pos)
        {
            for (Def.ButtonGlygh item : ButtonGlyphs().Reverse())
            {
                int value = 0;
                if (item == Def.ButtonGlygh.PlayJump || item == Def.ButtonGlygh.PlayAction || item == Def.ButtonGlygh.PlayDown || item == Def.ButtonGlygh.PlayPause)
                {
                    value = 20;
                }
                TinyRect rect = Misc.Inflate(GetButtonRect(item), value);
                if (Misc.IsInside(rect, pos))
                {
                    return item;
                }
            }
            return Def.ButtonGlygh.None;
        }

        public void Draw()
        {
            if (!accelStarted && Phase == Def.Phase.Play)
            {
                pixmap.DrawIcon(14, 0, GetPadBounds(PadCenter(), padSize / 2), 1.0, false);
                TinyPoint center = (padPressed ? padTouchPos : PadCenter());
                pixmap.DrawIcon(14, 1, GetPadBounds(center, padSize / 2), 1.0, false);
            }
            for (Def.ButtonGlygh buttonGlyph : ButtonGlyphs())
            {
                boolean pressed = pressedGlyphs.Contains(buttonGlyph);
                boolean selected = false;
                if (buttonGlyph.ordinal() >= Def.ButtonGlygh.InitGamerA.ordinal() && buttonGlyph.ordinal() <= Def.ButtonGlygh.InitGamerC.ordinal())
                {
                    int num = (int)(buttonGlyph.ordinal() - 1);
                    selected = num == gameData.SelectedGamer();
                }
                if (buttonGlyph == Def.ButtonGlygh.SetupSounds)
                {
                    selected = gameData.Sounds();
                }
                if (buttonGlyph == Def.ButtonGlygh.SetupJump)
                {
                    selected = gameData.JumpRight();
                }
                if (buttonGlyph == Def.ButtonGlygh.SetupZoom)
                {
                    selected = gameData.AutoZoom();
                }
                if (buttonGlyph == Def.ButtonGlygh.SetupAccel)
                {
                    selected = gameData.AccelActive();
                }
                pixmap.DrawInputButton(GetButtonRect(buttonGlyph), buttonGlyph, pressed, selected);
            }
            if ((Phase == Def.Phase.MainSetup || Phase == Def.Phase.PlaySetup) && gameData.AccelActive())
            {
                accelSlider.Draw(pixmap);
            }
        }

        private TinyRect GetPadBounds(TinyPoint center, int radius)
        {
             TinyRect result = default_(new TinyRect());
            result.Left = center.X - radius;
            result.Right = center.X + radius;
            result.Top = center.Y - radius;
            result.Bottom = center.Y + radius;
            return result;
        }

        public TinyRect GetButtonRect(Def.ButtonGlygh glyph)
        {
            TinyRect drawBounds = pixmap.DrawBounds();
            double num = drawBounds.Width();
            double num2 = drawBounds.Height();
            double num3 = num2 / 5.0;
            double num4 = num2 * 140.0 / 480.0;
            double num5 = num2 / 3.5;
            if (glyph.ordinal() >= Def.ButtonGlygh.Cheat1.ordinal()&& glyph.ordinal() <= Def.ButtonGlygh.Cheat9.ordinal())
            {
                int num6 = (int)(glyph.ordinal() - 35);
                TinyRect result = default_(new TinyRect());
                result.Left = 80 * num6;
                result.Right = 80 * (num6 + 1);
                result.Top = 0;
                result.Bottom = 80;
                return result;
            }
            switch (glyph)
            {
                case InitGamerA:
                    {
                         TinyRect result19 = default_(new TinyRect());
                        result19.Left = (int)(20.0 + num4 * 0.0);
                        result19.Right = (int)(20.0 + num4 * 0.5);
                        result19.Top = (int)(num2 - 20.0 - num4 * 2.1);
                        result19.Bottom = (int)(num2 - 20.0 - num4 * 1.6);
                        return result19;
                    }
                case InitGamerB:
                    {
                         TinyRect result18 = default_(new TinyRect());
                        result18.Left = (int)(20.0 + num4 * 0.0);
                        result18.Right = (int)(20.0 + num4 * 0.5);
                        result18.Top = (int)(num2 - 20.0 - num4 * 1.6);
                        result18.Bottom = (int)(num2 - 20.0 - num4 * 1.1);
                        return result18;
                    }
                case InitGamerC:
                    {
                         TinyRect result15 = default_(new TinyRect());
                        result15.Left = (int)(20.0 + num4 * 0.0);
                        result15.Right = (int)(20.0 + num4 * 0.5);
                        result15.Top = (int)(num2 - 20.0 - num4 * 1.1);
                        result15.Bottom = (int)(num2 - 20.0 - num4 * 0.6);
                        return result15;
                    }
                case InitSetup:
                    {
                         TinyRect result14 = default_(new TinyRect());
                        result14.Left = (int)(20.0 + num4 * 0.0);
                        result14.Right = (int)(20.0 + num4 * 0.5);
                        result14.Top = (int)(num2 - 20.0 - num4 * 0.5);
                        result14.Bottom = (int)(num2 - 20.0 - num4 * 0.0);
                        return result14;
                    }
                case InitPlay:
                    {
                         TinyRect result11 = default_(new TinyRect());
                        result11.Left = (int)(num - 20.0 - num4 * 1.0);
                        result11.Right = (int)(num - 20.0 - num4 * 0.0);
                        result11.Top = (int)(num2 - 40.0 - num4 * 1.0);
                        result11.Bottom = (int)(num2 - 40.0 - num4 * 0.0);
                        return result11;
                    }
                case InitBuy:
                case InitRanking:
                    {
                         TinyRect result10 = default_(new TinyRect());
                        result10.Left = (int)(num - 20.0 - num4 * 0.75);
                        result10.Right = (int)(num - 20.0 - num4 * 0.25);
                        result10.Top = (int)(num2 - 20.0 - num4 * 2.1);
                        result10.Bottom = (int)(num2 - 20.0 - num4 * 1.6);
                        return result10;
                    }
                case PauseMenu:
                    {
                         TinyRect result37 = default_(new TinyRect());
                        result37.Left = (int)((double)PixmapOrigin.X + num4 * -0.21);
                        result37.Right = (int)((double)PixmapOrigin.X + num4 * 0.79);
                        result37.Top = (int)((double)PixmapOrigin.Y + num4 * 2.2);
                        result37.Bottom = (int)((double)PixmapOrigin.Y + num4 * 3.2);
                        return result37;
                    }
                case PauseBack:
                    {
                         TinyRect result36 = default_(new TinyRect());
                        result36.Left = (int)((double)PixmapOrigin.X + num4 * 0.79);
                        result36.Right = (int)((double)PixmapOrigin.X + num4 * 1.79);
                        result36.Top = (int)((double)PixmapOrigin.Y + num4 * 2.2);
                        result36.Bottom = (int)((double)PixmapOrigin.Y + num4 * 3.2);
                        return result36;
                    }
                case PauseSetup:
                    {
                         TinyRect result35 = default_(new TinyRect());
                        result35.Left = (int)((double)PixmapOrigin.X + num4 * 1.79);
                        result35.Right = (int)((double)PixmapOrigin.X + num4 * 2.79);
                        result35.Top = (int)((double)PixmapOrigin.Y + num4 * 2.2);
                        result35.Bottom = (int)((double)PixmapOrigin.Y + num4 * 3.2);
                        return result35;
                    }
                case PauseRestart:
                    {
                         TinyRect result34 = default_(new TinyRect());
                        result34.Left = (int)((double)PixmapOrigin.X + num4 * 2.79);
                        result34.Right = (int)((double)PixmapOrigin.X + num4 * 3.79);
                        result34.Top = (int)((double)PixmapOrigin.Y + num4 * 2.2);
                        result34.Bottom = (int)((double)PixmapOrigin.Y + num4 * 3.2);
                        return result34;
                    }
                case PauseContinue:
                    {
                         TinyRect result33 = default_(new TinyRect());
                        result33.Left = (int)((double)PixmapOrigin.X + num4 * 3.79);
                        result33.Right = (int)((double)PixmapOrigin.X + num4 * 4.79);
                        result33.Top = (int)((double)PixmapOrigin.Y + num4 * 2.2);
                        result33.Bottom = (int)((double)PixmapOrigin.Y + num4 * 3.2);
                        return result33;
                    }
                case ResumeMenu:
                    {
                         TinyRect result32 = default_(new TinyRect());
                        result32.Left = (int)((double)PixmapOrigin.X + num4 * 1.29);
                        result32.Right = (int)((double)PixmapOrigin.X + num4 * 2.29);
                        result32.Top = (int)((double)PixmapOrigin.Y + num4 * 2.2);
                        result32.Bottom = (int)((double)PixmapOrigin.Y + num4 * 3.2);
                        return result32;
                    }
                case ResumeContinue:
                    {
                         TinyRect result31 = default_(new TinyRect());
                        result31.Left = (int)((double)PixmapOrigin.X + num4 * 2.29);
                        result31.Right = (int)((double)PixmapOrigin.X + num4 * 3.29);
                        result31.Top = (int)((double)PixmapOrigin.Y + num4 * 2.2);
                        result31.Bottom = (int)((double)PixmapOrigin.Y + num4 * 3.2);
                        return result31;
                    }
                case WinLostReturn:
                    {
                         TinyRect result30 = default_(new TinyRect());
                        result30.Left = (int)((double)PixmapOrigin.X + num - num3 * 2.2);
                        result30.Right = (int)((double)PixmapOrigin.X + num - num3 * 1.2);
                        result30.Top = (int)((double)PixmapOrigin.Y + num3 * 0.2);
                        result30.Bottom = (int)((double)PixmapOrigin.Y + num3 * 1.2);
                        return result30;
                    }
                case TrialBuy:
                    {
                         TinyRect result29 = default_(new TinyRect());
                        result29.Left = (int)((double)PixmapOrigin.X + num4 * 2.5);
                        result29.Right = (int)((double)PixmapOrigin.X + num4 * 3.5);
                        result29.Top = (int)((double)PixmapOrigin.Y + num4 * 2.1);
                        result29.Bottom = (int)((double)PixmapOrigin.Y + num4 * 3.1);
                        return result29;
                    }
                case TrialCancel:
                    {
                         TinyRect result28 = default_(new TinyRect());
                        result28.Left = (int)((double)PixmapOrigin.X + num4 * 3.5);
                        result28.Right = (int)((double)PixmapOrigin.X + num4 * 4.5);
                        result28.Top = (int)((double)PixmapOrigin.Y + num4 * 2.1);
                        result28.Bottom = (int)((double)PixmapOrigin.Y + num4 * 3.1);
                        return result28;
                    }
                case RankingContinue:
                    {
                         TinyRect result27 = default_(new TinyRect());
                        result27.Left = (int)((double)PixmapOrigin.X + num4 * 3.5);
                        result27.Right = (int)((double)PixmapOrigin.X + num4 * 4.5);
                        result27.Top = (int)((double)PixmapOrigin.Y + num4 * 2.1);
                        result27.Bottom = (int)((double)PixmapOrigin.Y + num4 * 3.1);
                        return result27;
                    }
                case SetupSounds:
                    {
                         TinyRect result26 = default_(new TinyRect());
                        result26.Left = (int)(20.0 + num4 * 0.0);
                        result26.Right = (int)(20.0 + num4 * 0.5);
                        result26.Top = (int)(num2 - 20.0 - num4 * 2.0);
                        result26.Bottom = (int)(num2 - 20.0 - num4 * 1.5);
                        return result26;
                    }
                case SetupJump:
                    {
                         TinyRect result25 = default_(new TinyRect());
                        result25.Left = (int)(20.0 + num4 * 0.0);
                        result25.Right = (int)(20.0 + num4 * 0.5);
                        result25.Top = (int)(num2 - 20.0 - num4 * 1.5);
                        result25.Bottom = (int)(num2 - 20.0 - num4 * 1.0);
                        return result25;
                    }
                case SetupZoom:
                    {
                         TinyRect result24 = default_(new TinyRect());
                        result24.Left = (int)(20.0 + num4 * 0.0);
                        result24.Right = (int)(20.0 + num4 * 0.5);
                        result24.Top = (int)(num2 - 20.0 - num4 * 1.0);
                        result24.Bottom = (int)(num2 - 20.0 - num4 * 0.5);
                        return result24;
                    }
                case SetupAccel:
                    {
                         TinyRect result23 = default_(new TinyRect());
                        result23.Left = (int)(20.0 + num4 * 0.0);
                        result23.Right = (int)(20.0 + num4 * 0.5);
                        result23.Top = (int)(num2 - 20.0 - num4 * 0.5);
                        result23.Bottom = (int)(num2 - 20.0 - num4 * 0.0);
                        return result23;
                    }
                case SetupReset:
                    {
                         TinyRect result22 = default_(new TinyRect());
                        result22.Left = (int)(450.0 + num4 * 0.0);
                        result22.Right = (int)(450.0 + num4 * 0.5);
                        result22.Top = (int)(num2 - 20.0 - num4 * 2.0);
                        result22.Bottom = (int)(num2 - 20.0 - num4 * 1.5);
                        return result22;
                    }
                case SetupReturn:
                    {
                         TinyRect result21 = default_(new TinyRect());
                        result21.Left = (int)(num - 20.0 - num4 * 0.8);
                        result21.Right = (int)(num - 20.0 - num4 * 0.0);
                        result21.Top = (int)(num2 - 20.0 - num4 * 0.8);
                        result21.Bottom = (int)(num2 - 20.0 - num4 * 0.0);
                        return result21;
                    }
                case PlayPause:
                    {
                         TinyRect result20 = default_(new TinyRect());
                        result20.Left = (int)(num - num3 * 0.7);
                        result20.Right = (int)(num - num3 * 0.2);
                        result20.Top = (int)(num3 * 0.2);
                        result20.Bottom = (int)(num3 * 0.7);
                        return result20;
                    }
                case PlayAction:
                    {
                        if (gameData.JumpRight())
                        {
                             TinyRect result16 = default_(new TinyRect());
                            result16.Left = (int)((double)drawBounds.Width() - num3 * 1.2);
                            result16.Right = (int)((double)drawBounds.Width() - num3 * 0.2);
                            result16.Top = (int)(num2 - num3 * 2.6);
                            result16.Bottom = (int)(num2 - num3 * 1.6);
                            return result16;
                        }
                         TinyRect result17 = default_(new TinyRect());
                        result17.Left = (int)(num3 * 0.2);
                        result17.Right = (int)(num3 * 1.2);
                        result17.Top = (int)(num2 - num3 * 2.6);
                        result17.Bottom = (int)(num2 - num3 * 1.6);
                        return result17;
                    }
                case PlayJump:
                    {
                        if (gameData.JumpRight())
                        {
                             TinyRect result12 = default_(new TinyRect());
                            result12.Left = (int)((double)drawBounds.Width() - num3 * 1.2);
                            result12.Right = (int)((double)drawBounds.Width() - num3 * 0.2);
                            result12.Top = (int)(num2 - num3 * 1.2);
                            result12.Bottom = (int)(num2 - num3 * 0.2);
                            return result12;
                        }
                         TinyRect result13 = default_(new TinyRect());
                        result13.Left = (int)(num3 * 0.2);
                        result13.Right = (int)(num3 * 1.2);
                        result13.Top = (int)(num2 - num3 * 1.2);
                        result13.Bottom = (int)(num2 - num3 * 0.2);
                        return result13;
                    }
                case PlayDown:
                    {
                        if (gameData.JumpRight())
                        {
                             TinyRect result8 = default_(new TinyRect());
                            result8.Left = (int)(num3 * 0.2);
                            result8.Right = (int)(num3 * 1.2);
                            result8.Top = (int)(num2 - num3 * 1.2);
                            result8.Bottom = (int)(num2 - num3 * 0.2);
                            return result8;
                        }
                         TinyRect result9 = default_(new TinyRect());
                        result9.Left = (int)((double)drawBounds.Width() - num3 * 1.2);
                        result9.Right = (int)((double)drawBounds.Width() - num3 * 0.2);
                        result9.Top = (int)(num2 - num3 * 1.2);
                        result9.Bottom = (int)(num2 - num3 * 0.2);
                        return result9;
                    }
                case Cheat11:
                    {
                         TinyRect result7 = default_(new TinyRect());
                        result7.Left = (int)(num5 * 0.0);
                        result7.Right = (int)(num5 * 1.0);
                        result7.Top = (int)(num5 * 0.0);
                        result7.Bottom = (int)(num5 * 1.0);
                        return result7;
                    }
                case Cheat12:
                    {
                         TinyRect result6 = default_(new TinyRect());
                        result6.Left = (int)(num5 * 0.0);
                        result6.Right = (int)(num5 * 1.0);
                        result6.Top = (int)(num5 * 1.0);
                        result6.Bottom = (int)(num5 * 2.0);
                        return result6;
                    }
                case Cheat21:
                    {
                         TinyRect result5 = default_(new TinyRect());
                        result5.Left = (int)(num5 * 1.0);
                        result5.Right = (int)(num5 * 2.0);
                        result5.Top = (int)(num5 * 0.0);
                        result5.Bottom = (int)(num5 * 1.0);
                        return result5;
                    }
                case Cheat22:
                    {
                         TinyRect result4 = default_(new TinyRect());
                        result4.Left = (int)(num5 * 1.0);
                        result4.Right = (int)(num5 * 2.0);
                        result4.Top = (int)(num5 * 1.0);
                        result4.Bottom = (int)(num5 * 2.0);
                        return result4;
                    }
                case Cheat31:
                    {
                         TinyRect result3 = default_(new TinyRect());
                        result3.Left = (int)(num5 * 2.0);
                        result3.Right = (int)(num5 * 3.0);
                        result3.Top = (int)(num5 * 0.0);
                        result3.Bottom = (int)(num5 * 1.0);
                        return result3;
                    }
                case Cheat32:
                    {
                         TinyRect result2 = default_(new TinyRect());
                        result2.Left = (int)(num5 * 2.0);
                        result2.Right = (int)(num5 * 3.0);
                        result2.Top = (int)(num5 * 1.0);
                        result2.Bottom = (int)(num5 * 2.0);
                        return result2;
                    }
                default:
                    return default_(new TinyRect());
            }
        }

        private void StartAccel()
        {
            try
            {
                accelSensor.Start();
                accelStarted = true;
            }
            catch (AccelerometerFailedException e)
            {
                accelStarted = false;
            }
            catch (UnauthorizedAccessException e)
            {
                accelStarted = false;
            }
        }

        private void StopAccel()
        {
            if (accelStarted)
            {
                try
                {
                    accelSensor.Stop();
                }
                catch (AccelerometerFailedException e)
                {
                }
                accelStarted = false;
            }
        }


        private void HandleAccelSensorCurrentValueChanged(Object sender, AccelerometerEventArgs e)
        {
            //IL_0001: Unknown result type (might be due to invalid IL or missing references)
            //IL_0006: Unknown result type (might be due to invalid IL or missing references)

            float y = e.Y();
            float num = (1f - (float)gameData.AccelSensitivity()) * 0.06f + 0.04f;
            float num2 = (accelLastState ? (num * 0.6f) : num);
            if (y > num2)
            {
                accelSpeedX = 0.0 - Math_.Min((double)y * 0.25 / (double)num + 0.25, 1.0);
            }
            else if (y < 0f - num2)
            {
                accelSpeedX = Math_.Min((double)(0f - y) * 0.25 / (double)num + 0.25, 1.0);
            }
            else
            {
                accelSpeedX = 0.0;
            }
            accelLastState = accelSpeedX != 0.0;
            if (accelWaitZero)
            {
                if (accelSpeedX == 0.0)
                {
                    accelWaitZero = false;
                }
                else
                {
                    accelSpeedX = 0.0;
                }
            }
        }
    }

