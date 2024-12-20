package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

// WindowsPhoneSpeedyBlupi, Version=1.0.0.5, Culture=neutral, PublicKeyToken=6db12cd62dbec439
// WindowsPhoneSpeedyBlupi.Game1
import com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi.GameData.GamerInfo;
import static com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.default_.default_;
import com.openeggbert.jdotnet.System.*;
import com.openeggbert.jxna.Microsoft.Xna.Framework.*;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Content.ContentManager;
//import com.openeggbert.jxna.Microsoft.Xna.Framework.GamerServices;//todo remove me
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.*;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Input.Touch.*;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Media.*;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Game;
//import static com.openeggbert.jdotnet.System.Net.Mime.MediaTypeNames.*;
import lombok.Getter;
import lombok.Setter;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.readonly;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.namespace;

@namespace(name = "WindowsPhoneSpeedyBlupi")

    public class Game1 extends Game
    {
        private static @readonly final double[] waitTable = new double[]
        {
        0.1, 7.0, 0.2, 20.0, 0.25, 22.0, 0.45, 50.0, 0.6, 53.0,
        0.65, 58.0, 0.68, 60.0, 0.8, 70.0, 0.84, 75.0, 0.9, 84.0,
        0.94, 91.0, 1.0, 100.0
        };

        private static @readonly final Def.ButtonGlygh[] cheatGeste = new Def.ButtonGlygh[]
        {
        Def.ButtonGlygh.Cheat12,
        Def.ButtonGlygh.Cheat22,
        Def.ButtonGlygh.Cheat32,
        Def.ButtonGlygh.Cheat12,
        Def.ButtonGlygh.Cheat11,
        Def.ButtonGlygh.Cheat21,
        Def.ButtonGlygh.Cheat22,
        Def.ButtonGlygh.Cheat21,
        Def.ButtonGlygh.Cheat31,
        Def.ButtonGlygh.Cheat32
        };

        private @readonly final GraphicsDeviceManager graphics;

        private @readonly final Pixmap pixmap;

        private @readonly final Sound sound;

        private @readonly final Decor decor;

        private @readonly final InputPad inputPad;

        private @readonly final GameData gameData;

        private Def.Phase phase;

        private TimeSpan startTime;

        private int missionToStart1;

        private int missionToStart2;

        private int mission;

        private int cheatGesteIndex;

        private int continueMission;

        private Jauge waitJauge;

        private double waitProgress;

        private boolean isTrialMode;

        private boolean simulateTrialMode;

        private boolean playSetup;

        private int phaseTime;

        private Def.Phase fadeOutPhase;

        private int fadeOutMission;
                        
        public boolean IsRankingMode() {return false;}

        public boolean IsTrialMode()
        {
            
                if (!simulateTrialMode)
                {
                    return isTrialMode;
                }
                return true;
            
        }

        public Game1()
        {
            
            Exiting().addEventListener((e)-> this.OnExiting(this, new ExitingEventArgs()));
            if(!TouchPanel.GetCapabilities().IsConnected())
            {
                this.setMouseVisible(true);
                Mouse.SetCursor(MouseCursor.Arrow);
            }
            graphics = new GraphicsDeviceManager(this);
            graphics.setFullScreen(false);
            super.getContent().setRootDirectory("Content");
            super.setTargetElapsedTime(TimeSpan.FromTicks(500000L));
            super.setInactiveSleepTime(TimeSpan.FromSeconds(1.0d));
            missionToStart1 = -1;
            missionToStart2 = -1;
            gameData = new GameData();
            pixmap = new Pixmap(this, graphics);
            sound = new Sound(this, gameData);
            decor = new Decor();
            decor.Create(sound, pixmap, gameData);
            TinyPoint pos = new TinyPoint(196,426);
            waitJauge = new Jauge();
            waitJauge.Create(pixmap, sound, pos, 3, false);
            waitJauge.SetHide(false);
            waitJauge.setZoom(2.0);
            phase = Def.Phase.None;
            fadeOutPhase = Def.Phase.None;
            inputPad = new InputPad(this, decor, pixmap, sound, gameData);
            SetPhase(Def.Phase.First);
        }

        @Override
        protected void Initialize() {
            super.Initialize();
        }

        @Override
        protected void LoadContent() {
            pixmap.BackgroundCache("wait");
        }

        @Override
        protected void UnloadContent() {
        }

        @Override
        protected void OnDeactivated(Object sender, EventArgs args)
        {
            if (phase == Def.Phase.Play)
            {
                decor.CurrentWrite();
            }
            else
            {
                decor.CurrentDelete();
            }
            super.OnDeactivated(sender, args);
        }

        @Override
        protected void OnActivated(Object sender, EventArgs args)
        {
            continueMission = 1;
            super.OnActivated(sender, args);
        }

        protected void OnExiting(Object sender, EventArgs args)
        {
            decor.CurrentDelete();
        }

        @Override
        protected void Update(GameTime gameTime)
        {
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed)
            {
                if (phase == Def.Phase.Play)
                {
                    SetPhase(Def.Phase.Pause);
                }
                else if (phase == Def.Phase.PlaySetup)
                {
                    SetPhase(Def.Phase.Play, -1);
                }
                else if (phase != Def.Phase.Init)
                {
                    SetPhase(Def.Phase.Init);
                }
                else
                {
                    Exit();
                }
                return;
            }
            phaseTime++;
            if (fadeOutPhase != Def.Phase.None)
            {
                if (phaseTime >= 20)
                {
                    SetPhase(fadeOutPhase);
                }
                return;
            }
            if (missionToStart2 != -1)
            {
                SetPhase(Def.Phase.Play, missionToStart2);
                return;
            }
            if (phase == Def.Phase.First)
            {
                startTime = gameTime.TotalGameTime;
                pixmap.LoadContent();
                sound.LoadContent();
                gameData.Read();
                inputPad.PixmapOrigin = pixmap.Origin();
                SetPhase(Def.Phase.Wait);
                return;
            }
            if (phase == Def.Phase.Wait)
            {
                if (continueMission == 2)
                {
                    continueMission = 0;
                    if (decor.CurrentRead())
                    {
                        SetPhase(Def.Phase.Resume);
                        return;
                    }
                }
                long num = gameTime.TotalGameTime.Ticks() - startTime.Ticks();
                waitProgress = (double)num / 50000000.0;
                if (waitProgress > 1.0)
                {
                    SetPhase(Def.Phase.Init);
                }
                return;
            }
            inputPad.Update();
            Def.ButtonGlygh buttonPressed = inputPad.ButtonPressed();
            if (buttonPressed.ordinal()>= Def.ButtonGlygh.InitGamerA.ordinal() && buttonPressed.ordinal() <= Def.ButtonGlygh.InitGamerC.ordinal())
            {
                SetGamer((int)(buttonPressed.ordinal() - 1));
                return;
            }
            switch (buttonPressed)
            {
                case InitSetup:
                    SetPhase(Def.Phase.MainSetup);
                    return;
                case PauseSetup:
                    SetPhase(Def.Phase.PlaySetup);
                    return;
                case SetupSounds:
                    gameData.setSounds(!gameData.Sounds());
                    gameData.Write();
                    return;
                case SetupJump:
                    gameData.setJumpRight(!gameData.JumpRight());
                    gameData.Write();
                    return;
                case SetupZoom:
                    gameData.setAutoZoom(!gameData.AutoZoom());
                    gameData.Write();
                    return;
                case SetupAccel:
                    gameData.setAccelActive(!gameData.AccelActive());
                    gameData.Write();
                    return;
                case SetupReset:
                    gameData.Reset();
                    gameData.Write();
                    return;
                case SetupReturn:
                    if (playSetup)
                    {
                        SetPhase(Def.Phase.Play, -1);
                    }
                    else
                    {
                        SetPhase(Def.Phase.Init);
                    }
                    return;
                case InitPlay:
                    SetPhase(Def.Phase.Play, 1);
                    return;
                case PlayPause:
                    SetPhase(Def.Phase.Pause);
                    return;
                case WinLostReturn:
                case PauseMenu:
                case ResumeMenu:
                    SetPhase(Def.Phase.Init);
                    break;
            }
            switch (buttonPressed)
            {
                case ResumeContinue:
                    ContinueMission();
                    return;
                case InitBuy:
                case TrialBuy:
                    MarketPlace.Show(PlayerIndex.One);
                    SetPhase(Def.Phase.Init);
                    return;
                case InitRanking:
                    SetPhase(Def.Phase.Ranking);
                    return;
                case TrialCancel:
                case RankingContinue:
                    SetPhase(Def.Phase.Init);
                    return;
                case PauseBack:
                    MissionBack();
                    return;
                case PauseRestart:
                    SetPhase(Def.Phase.Play, mission);
                    return;
                case PauseContinue:
                    SetPhase(Def.Phase.Play, -1);
                    return;
                case Cheat11:
                case Cheat12:
                case Cheat21:
                case Cheat22:
                case Cheat31:
                case Cheat32:
                    if (buttonPressed == cheatGeste[cheatGesteIndex])
                    {
                        cheatGesteIndex++;
                        if (cheatGesteIndex == cheatGeste.length)
                        {
                            cheatGesteIndex = 0;
                            inputPad.setShowCheatMenu(true);
                        }
                    }
                    else
                    {
                        cheatGesteIndex = 0;
                    }
                    break;
                default:
                    if (buttonPressed.ordinal() != 0)
                    {
                        cheatGesteIndex = 0;
                    }
                    break;
            }
            if (buttonPressed.ordinal() >= Def.ButtonGlygh.Cheat1.ordinal() && buttonPressed.ordinal() <= Def.ButtonGlygh.Cheat9.ordinal())
            {
                CheatAction(buttonPressed);
            }
            if (phase == Def.Phase.Play)
            {
                decor.ButtonPressed = buttonPressed;
                decor.MoveStep();
                int num2 = decor.IsTerminated();
                if (num2 == -1)
                {
                    MemorizeGamerProgress();
                    SetPhase(Def.Phase.Lost);
                }
                else if (num2 == -2)
                {
                    MemorizeGamerProgress();
                    SetPhase(Def.Phase.Win);
                }
                else if (num2 >= 1)
                {
                    MemorizeGamerProgress();
                    StartMission(num2);
                }
            }
            super.Update(gameTime);
        }

        private void MissionBack()
        {
            int num = mission;
            if (num == 1)
            {
                SetPhase(Def.Phase.Init);
                return;
            }
            num = ((num % 10 == 0) ? 1 : (num / 10 * 10));
            SetPhase(Def.Phase.Play, num);
        }

        private void StartMission(int mission)
        {
            if (mission > 20 && mission % 10 > 1 && IsTrialMode())
            {
                SetPhase(Def.Phase.Trial);
                return;
            }
            this.mission = mission;
            if (this.mission != 1)
            {
                gameData.setLastWorld(this.mission / 10);
            }
            decor.Read(0, this.mission, false);
            decor.LoadImages();
            decor.SetMission(this.mission);
            decor.SetNbVies(gameData.NbVies());
            decor.InitializeDoors(gameData);
            decor.AdaptDoors(false);
            decor.MainSwitchInitialize(gameData.LastWorld());
            decor.PlayPrepare(false);
            decor.StartSound();
            inputPad.StartMission(this.mission);
        }

        private void ContinueMission()
        {
            SetPhase(Def.Phase.Play, -2);
            mission = decor.GetMission();
            if (mission != 1)
            {
                gameData.setLastWorld(mission / 10);
            }
            decor.LoadImages();
            decor.StartSound();
            inputPad.StartMission(mission);
        }

        private void CheatAction(Def.ButtonGlygh glyph)
        {
            switch (glyph)
            {
                case Cheat1:
                    decor.CheatAction(Tables.CheatCodes.OpenDoors);
                    break;
                case Cheat2:
                    decor.CheatAction(Tables.CheatCodes.SuperBlupi);
                    break;
                case Cheat3:
                    decor.CheatAction(Tables.CheatCodes.ShowSecret);
                    break;
                case Cheat4:
                    decor.CheatAction(Tables.CheatCodes.LayEgg);
                    break;
                case Cheat5:
                    gameData.Reset();
                    break;
                case Cheat6:
                    simulateTrialMode = !simulateTrialMode;
                    break;
                case Cheat7:
                    decor.CheatAction(Tables.CheatCodes.CleanAll);
                    break;
                case Cheat8:
                    decor.CheatAction(Tables.CheatCodes.AllTreasure);
                    break;
                case Cheat9:
                    decor.CheatAction(Tables.CheatCodes.EndGoal);
                    break;
            }
        }

        @Override
        protected void Draw(GameTime gameTime)
        {
            if (continueMission == 1)
            {
                continueMission = 2;
            }
            if (phase == Def.Phase.Wait || phase == Def.Phase.Init || phase == Def.Phase.Pause || phase == Def.Phase.Resume || phase == Def.Phase.Lost || phase == Def.Phase.Win || phase == Def.Phase.MainSetup || phase == Def.Phase.PlaySetup || phase == Def.Phase.Trial || phase == Def.Phase.Ranking)
            {
                pixmap.DrawBackground();
                if (fadeOutPhase == Def.Phase.None && missionToStart1 != -1)
                {
                    missionToStart2 = missionToStart1;
                    missionToStart1 = -1;
                }
                else
                {
                    DrawBackgroundFade();
                    if (fadeOutPhase == Def.Phase.None)
                    {
                        DrawButtonsBackground();
                        inputPad.Draw();
                        DrawButtonsText();
                    }
                }
            }
            else if (phase == Def.Phase.Play)
            {
                decor.Build();
                inputPad.Draw();
            }
            if (phase == Def.Phase.Wait)
            {
                DrawWaitProgress();
            }
            super.Draw(gameTime);
        }

        private void DrawBackgroundFade()
        {
            if (phase == Def.Phase.Init)
            {
                double num = Math_.Min((double)phaseTime / 20.0, 1.0);
                TinyRect rect;
                double opacity;
                if (fadeOutPhase == Def.Phase.MainSetup)
                {
                    num = (1.0 - num) * (1.0 - num);
                     TinyRect tinyRect = default_(new TinyRect());
                    tinyRect.Left = (int)(720.0 - 640.0 * num);
                    tinyRect.Right = (int)(1360.0 - 640.0 * num);
                    tinyRect.Top = 0;
                    tinyRect.Bottom = 160;
                    rect = tinyRect;
                    opacity = num * num;
                }
                else
                {
                    num = ((fadeOutPhase.ordinal() != 0) ? (1.0 - num * 2.0) : (1.0 - (1.0 - num) * (1.0 - num)));
                     TinyRect tinyRect2 = default_(new TinyRect());
                    tinyRect2.Left = 80;
                    tinyRect2.Right = 720;
                    tinyRect2.Top = (int)(-160.0 + num * 160.0);
                    tinyRect2.Bottom = (int)(0.0 + num * 160.0);
                    rect = tinyRect2;
                    opacity = 1.0;
                }
                pixmap.DrawIcon(15, 0, rect, opacity, false);
            }
            if (phase == Def.Phase.Init)
            {
                double num = Math_.Min((double)phaseTime / 20.0, 1.0);
                double opacity;
                if (fadeOutPhase == Def.Phase.MainSetup)
                {
                    opacity = (1.0 - num) * (1.0 - num);
                    num = 1.0;
                }
                else if (fadeOutPhase == Def.Phase.None)
                {

                    num = 0.5 + num / 2.0;
                    opacity = Math_.Min(num * num, 1.0);
                }
                else
                {
                    opacity = 1.0 - num;
                    num = 1.0 + num * 10.0;
                }
                 TinyRect tinyRect3 = default_(new TinyRect());
                tinyRect3.Left = (int)(468.0 - 205.0 * num);
                tinyRect3.Right = (int)(468.0 + 205.0 * num);
                tinyRect3.Top = (int)(280.0 - 190.0 * num);
                tinyRect3.Bottom = (int)(280.0 + 190.0 * num);
                TinyRect rect = tinyRect3;
                pixmap.DrawIcon(16, 0, rect, opacity, 0.0, false);
            }
            if (phase == Def.Phase.Pause || phase == Def.Phase.Resume)
            {
                if (fadeOutPhase == Def.Phase.Play)
                {
                    double num = Math_.Min((double)phaseTime / 20.0, 1.0);
                    double opacity = 1.0 - num;
                    num = 1.0 + num * 10.0;
                     TinyRect tinyRect4 = default_(new TinyRect());
                    tinyRect4.Left = (int)(418.0 - 205.0 * num);
                    tinyRect4.Right = (int)(418.0 + 205.0 * num);
                    tinyRect4.Top = (int)(190.0 - 190.0 * num);
                    tinyRect4.Bottom = (int)(190.0 + 190.0 * num);
                    TinyRect rect = tinyRect4;
                    pixmap.DrawIcon(16, 0, rect, opacity, 0.0, false);
                }
                else if (fadeOutPhase == Def.Phase.PlaySetup)
                {
                    double num = Math_.Min((double)phaseTime / 20.0, 1.0);
                    num *= num;
                     TinyRect tinyRect5 = default_(new TinyRect());
                    tinyRect5.Left = (int)(213.0 + 800.0 * num);
                    tinyRect5.Right = (int)(623.0 + 800.0 * num);
                    tinyRect5.Top = 0;
                    tinyRect5.Bottom = 0;
                    TinyRect rect = tinyRect5;
                    pixmap.DrawIcon(16, 0, rect, 1.0, 0.0, false);
                }
                else
                {
                    double num;
                    if (fadeOutPhase == Def.Phase.None)
                    {
                        num = Math_.Min((double)phaseTime / 15.0, 1.0);
                    }
                    else
                    {
                        num = Math_.Min((double)phaseTime / 15.0, 1.0);
                        num = 1.0 - num;
                    }
                     TinyRect tinyRect6 = default_(new TinyRect());
                    tinyRect6.Left = (int)(418.0 - 205.0 * num);
                    tinyRect6.Right = (int)(418.0 + 205.0 * num);
                    tinyRect6.Top = (int)(190.0 - 190.0 * num);
                    tinyRect6.Bottom = (int)(190.0 + 190.0 * num);
                    TinyRect rect = tinyRect6;
                    double rotation = 0.0;
                    if (num < 1.0)
                    {
                        rotation = (1.0 - num) * (1.0 - num) * 360.0 * 1.0;
                    }
                    if (rect.Width() > 0 && rect.Height() > 0)
                    {
                        pixmap.DrawIcon(16, 0, rect, 1.0, rotation, false);
                    }
                }
            }
            if (phase == Def.Phase.MainSetup || phase == Def.Phase.PlaySetup)
            {
                double num = Math_.Min((double)phaseTime / 20.0, 1.0);
                num = 1.0 - (1.0 - num) * (1.0 - num);
                double num2;
                if (phaseTime < 20)
                {
                    num2 = (double)phaseTime / 20.0;
                    num2 = 1.0 - (1.0 - num2) * (1.0 - num2);
                }
                else
                {
                    num2 = 1.0 + ((double)phaseTime - 20.0) / 400.0;
                }
                if (fadeOutPhase.ordinal() != 0)
                {
                    num = 1.0 - num;
                    num2 = 1.0 - num2;
                }
                 TinyRect tinyRect7 = default_(new TinyRect());
                tinyRect7.Left = (int)(720.0 - 640.0 * num);
                tinyRect7.Right = (int)(1360.0 - 640.0 * num);
                tinyRect7.Top = 0;
                tinyRect7.Bottom = 160;
                TinyRect rect = tinyRect7;
                pixmap.DrawIcon(15, 0, rect, num * num, false);
                 TinyRect tinyRect8 = default_(new TinyRect());
                tinyRect8.Left = 487;
                tinyRect8.Right = 713;
                tinyRect8.Top = 148;
                tinyRect8.Bottom = 374;
                TinyRect rect2 = tinyRect8;
                 TinyRect tinyRect9 = default_(new TinyRect());
                tinyRect9.Left = 118;
                tinyRect9.Right = 570;
                tinyRect9.Top = 268;
                tinyRect9.Bottom = 720;
                TinyRect rect3 = tinyRect9;
                double opacity = 0.5 - num * 0.4;
                double rotation = (0.0 - num2) * 100.0 * 2.5;
                pixmap.DrawIcon(17, 0, rect2, opacity, rotation, false);
                pixmap.DrawIcon(17, 0, rect3, opacity, (0.0 - rotation) * 0.5, false);
            }
            if (phase == Def.Phase.Lost)
            {
                double num = Math_.Min((double)phaseTime / 100.0, 1.0);
                 TinyRect tinyRect10 = default_(new TinyRect());
                tinyRect10.Left = (int)(418.0 - 205.0 * num);
                tinyRect10.Right = (int)(418.0 + 205.0 * num);
                tinyRect10.Top = (int)(238.0 - 190.0 * num);
                tinyRect10.Bottom = (int)(238.0 + 190.0 * num);
                TinyRect rect = tinyRect10;
                double rotation = 0.0;
                if (num < 1.0)
                {
                    rotation = (1.0 - num) * (1.0 - num) * 360.0 * 6.0;
                }
                if (rect.Width() > 0 && rect.Height() > 0)
                {
                    pixmap.DrawIcon(16, 0, rect, 1.0, rotation, false);
                }
            }
            if (phase == Def.Phase.Win)
            {
                double num = Math_.Sin((double)phaseTime / 3.0) / 2.0 + 1.0;
                 TinyRect tinyRect11 = default_(new TinyRect());
                tinyRect11.Left = (int)(418.0 - 205.0 * num);
                tinyRect11.Right = (int)(418.0 + 205.0 * num);
                tinyRect11.Top = (int)(238.0 - 190.0 * num);
                tinyRect11.Bottom = (int)(238.0 + 190.0 * num);
                TinyRect rect = tinyRect11;
                pixmap.DrawIcon(16, 0, rect, 1.0, 0.0, false);
            }
        }

        private void DrawButtonsBackground()
        {
            if (phase == Def.Phase.Init)
            {
                TinyRect drawBounds = pixmap.DrawBounds();
                int width = drawBounds.Width();
                int height = drawBounds.Height();
                 TinyRect tinyRect = default_(new TinyRect());
                tinyRect.Left = 10;
                tinyRect.Right = 260;
                tinyRect.Top = height - 325;
                tinyRect.Bottom = height - 10;
                TinyRect rect = tinyRect;
                pixmap.DrawIcon(14, 15, rect, 0.3, false);
                 TinyRect tinyRect2 = default_(new TinyRect());
                tinyRect2.Left = width - 170;
                tinyRect2.Right = width - 10;
                tinyRect2.Top = height - ((IsTrialMode() || IsRankingMode()) ? 325 : 195);
                tinyRect2.Bottom = height - 10;
                rect = tinyRect2;
                pixmap.DrawIcon(14, 15, rect, 0.3, false);
            }
        }

        private void DrawButtonsText()
        {
            if (phase == Def.Phase.Init)
            {
                DrawButtonGamerText(Def.ButtonGlygh.InitGamerA, 0);
                DrawButtonGamerText(Def.ButtonGlygh.InitGamerB, 1);
                DrawButtonGamerText(Def.ButtonGlygh.InitGamerC, 2);
                DrawTextUnderButton(Def.ButtonGlygh.InitPlay, MyResource.TX_BUTTON_PLAY);
                DrawTextRightButton(Def.ButtonGlygh.InitSetup, MyResource.TX_BUTTON_SETUP);
                if (IsTrialMode())
                {
                    DrawTextUnderButton(Def.ButtonGlygh.InitBuy, MyResource.TX_BUTTON_BUY);
                }
                if (IsRankingMode())
                {
                    DrawTextUnderButton(Def.ButtonGlygh.InitRanking, MyResource.TX_BUTTON_RANKING);
                }
            }
            if (phase == Def.Phase.Pause)
            {
                DrawTextUnderButton(Def.ButtonGlygh.PauseMenu, MyResource.TX_BUTTON_MENU);
                if (mission != 1)
                {
                    DrawTextUnderButton(Def.ButtonGlygh.PauseBack, MyResource.TX_BUTTON_BACK);
                }
                DrawTextUnderButton(Def.ButtonGlygh.PauseSetup, MyResource.TX_BUTTON_SETUP);
                if (mission != 1 && mission % 10 != 0)
                {
                    DrawTextUnderButton(Def.ButtonGlygh.PauseRestart, MyResource.TX_BUTTON_RESTART);
                }
                DrawTextUnderButton(Def.ButtonGlygh.PauseContinue, MyResource.TX_BUTTON_CONTINUE);
            }
            if (phase == Def.Phase.Resume)
            {
                DrawTextUnderButton(Def.ButtonGlygh.ResumeMenu, MyResource.TX_BUTTON_MENU);
                DrawTextUnderButton(Def.ButtonGlygh.ResumeContinue, MyResource.TX_BUTTON_CONTINUE);
            }
            if (phase == Def.Phase.MainSetup || phase == Def.Phase.PlaySetup)
            {
                DrawTextRightButton(Def.ButtonGlygh.SetupSounds, MyResource.TX_BUTTON_SETUP_SOUNDS);
                DrawTextRightButton(Def.ButtonGlygh.SetupJump, MyResource.TX_BUTTON_SETUP_JUMP);
                DrawTextRightButton(Def.ButtonGlygh.SetupZoom, MyResource.TX_BUTTON_SETUP_ZOOM);
                DrawTextRightButton(Def.ButtonGlygh.SetupAccel, MyResource.TX_BUTTON_SETUP_ACCEL);
                if (phase == Def.Phase.MainSetup)
                {
                    String text = string.Format(MyResource.LoadString(MyResource.TX_BUTTON_SETUP_RESET), new string((char)(65 + gameData.SelectedGamer()), 1));
                    DrawTextRightButton(Def.ButtonGlygh.SetupReset, text);
                }
            }
            if (phase == Def.Phase.Trial)
            {
                 TinyPoint tinyPoint = default_(new TinyPoint());
                tinyPoint.X = 360;
                tinyPoint.Y = 50;
                TinyPoint pos = tinyPoint;
                Text.DrawText(pixmap, pos, MyResource.LoadString(MyResource.TX_TRIAL1), 0.9);
                pos.Y += 40;
                Text.DrawText(pixmap, pos, MyResource.LoadString(MyResource.TX_TRIAL2), 0.7);
                pos.Y += 25;
                Text.DrawText(pixmap, pos, MyResource.LoadString(MyResource.TX_TRIAL3), 0.7);
                pos.Y += 25;
                Text.DrawText(pixmap, pos, MyResource.LoadString(MyResource.TX_TRIAL4), 0.7);
                pos.Y += 25;
                Text.DrawText(pixmap, pos, MyResource.LoadString(MyResource.TX_TRIAL5), 0.7);
                pos.Y += 25;
                Text.DrawText(pixmap, pos, MyResource.LoadString(MyResource.TX_TRIAL6), 0.7);
                DrawTextUnderButton(Def.ButtonGlygh.TrialBuy, MyResource.TX_BUTTON_BUY);
                DrawTextUnderButton(Def.ButtonGlygh.TrialCancel, MyResource.TX_BUTTON_BACK);
            }
            if (phase == Def.Phase.Ranking)
            {
                DrawTextUnderButton(Def.ButtonGlygh.RankingContinue, MyResource.TX_BUTTON_BACK);
            }
        }

        private void DrawButtonGamerText(Def.ButtonGlygh glyph, int gamer)
        {
            TinyRect buttonRect = inputPad.GetButtonRect(glyph);
            
            GamerInfo gamerInfo = gameData.GetGamerInfo(gamer);
            int nbVies = gamerInfo.nbVies;
            int mainDoors = gamerInfo.mainDoors;
            int secondaryDoors = gamerInfo.secondaryDoors;
             TinyPoint tinyPoint = default_(new TinyPoint());
            tinyPoint.X = buttonRect.Right + 5 - pixmap.Origin().X;
            tinyPoint.Y = buttonRect.Top + 3 - pixmap.Origin().Y;
            TinyPoint pos = tinyPoint;
            String text = string.Format(MyResource.LoadString(MyResource.TX_GAMER_TITLE), new string((char)(65 + gamer), 1));
            Text.DrawText(pixmap, pos, text, 0.7);
             TinyPoint tinyPoint2 = default_(new TinyPoint());
            tinyPoint2.X = buttonRect.Right + 5 - pixmap.Origin().X;
            tinyPoint2.Y = buttonRect.Top + 25 - pixmap.Origin().Y;
            pos = tinyPoint2;
            text = string.Format(MyResource.LoadString(MyResource.TX_GAMER_MDOORS), mainDoors);
            Text.DrawText(pixmap, pos, text, 0.45);
             TinyPoint tinyPoint3 = default_(new TinyPoint());
            tinyPoint3.X = buttonRect.Right + 5 - pixmap.Origin().X;
            tinyPoint3.Y = buttonRect.Top + 39 - pixmap.Origin().Y;
            pos = tinyPoint3;
            text = string.Format(MyResource.LoadString(MyResource.TX_GAMER_SDOORS), secondaryDoors);
            Text.DrawText(pixmap, pos, text, 0.45);
             TinyPoint tinyPoint4 = default_(new TinyPoint());
            tinyPoint4.X = buttonRect.Right + 5 - pixmap.Origin().X;
            tinyPoint4.Y = buttonRect.Top + 53 - pixmap.Origin().Y;
            pos = tinyPoint4;
            text = string.Format(MyResource.LoadString(MyResource.TX_GAMER_LIFES), nbVies);
            Text.DrawText(pixmap, pos, text, 0.45);
        }

        private void DrawTextRightButton(Def.ButtonGlygh glyph, int res)
        {
            DrawTextRightButton(glyph, MyResource.LoadString(res));
        }

        private void DrawTextRightButton(Def.ButtonGlygh glyph, String text)
        {
            TinyRect buttonRect = inputPad.GetButtonRect(glyph);
            String[] array = text.split("\n");
            if (array.length == 2)
            {
                 TinyPoint tinyPoint = default_(new TinyPoint());
                tinyPoint.X = buttonRect.Right + 10 - pixmap.Origin().X;
                tinyPoint.Y = (buttonRect.Top + buttonRect.Bottom) / 2 - 20 - pixmap.Origin().Y;
                TinyPoint pos = tinyPoint;
                Text.DrawText(pixmap, pos, array[0], 0.7);
                pos.Y += 24;
                Text.DrawText(pixmap, pos, array[1], 0.7);
            }
            else
            {
                 TinyPoint tinyPoint2 = default_(new TinyPoint());
                tinyPoint2.X = buttonRect.Right + 10 - pixmap.Origin().X;
                tinyPoint2.Y = (buttonRect.Top + buttonRect.Bottom) / 2 - 8 - pixmap.Origin().Y;
                TinyPoint pos2 = tinyPoint2;
                Text.DrawText(pixmap, pos2, text, 0.7);
            }
        }

        private void DrawTextUnderButton(Def.ButtonGlygh glyph, int res)
        {
            TinyRect buttonRect = inputPad.GetButtonRect(glyph);
             TinyPoint tinyPoint = default_(new TinyPoint());
            tinyPoint.X = (buttonRect.Left + buttonRect.Right) / 2 - pixmap.Origin().X;
            tinyPoint.Y = buttonRect.Bottom + 2 - pixmap.Origin().Y;
            TinyPoint pos = tinyPoint;
            String text = MyResource.LoadString(res);
            Text.DrawTextCenter(pixmap, pos, text, 0.7);
        }

        private void DrawWaitProgress()
        {
            if (continueMission != 0)
            {
                return;
            }
            for (int i = 0; i < waitTable.length; i++)
            {
                if (waitProgress <= waitTable[i * 2])
                {
                    waitJauge.SetLevel((int)waitTable[i * 2 + 1]);
                    break;
                }
            }
            waitJauge.Draw();
        }

        private void DrawDebug()
        {
             TinyPoint tinyPoint = default_(new TinyPoint());
            tinyPoint.X = 10;
            tinyPoint.Y = 20;
            TinyPoint pos = tinyPoint;
            Text.DrawText(pixmap, pos, Integer.valueOf(inputPad.TotalTouch()).toString(), 1.0);
        }

        private void SetGamer(int gamer)
        {
            gameData.setSelectedGamer(gamer);
            gameData.Write();
        }

        private void SetPhase(Def.Phase phase)
        {
            SetPhase(phase, 0);
        }

        private void SetPhase(Def.Phase phase, int mission)
        {
            if (mission != -2)
            {
                if (missionToStart2 == -1)
                {
                    if ((this.phase == Def.Phase.Init || this.phase == Def.Phase.MainSetup || this.phase == Def.Phase.PlaySetup || this.phase == Def.Phase.Pause || this.phase == Def.Phase.Resume) && fadeOutPhase == Def.Phase.None)
                    {
                        fadeOutPhase = phase;
                        fadeOutMission = mission;
                        phaseTime = 0;
                        return;
                    }
                    if (phase == Def.Phase.Play)
                    {
                        fadeOutPhase = Def.Phase.None;
                        if (fadeOutMission != -1)
                        {
                            missionToStart1 = fadeOutMission;
                            return;
                        }
                        mission = fadeOutMission;
                        decor.LoadImages();
                    }
                }
                else
                {
                    mission = missionToStart2;
                }
            }
            this.phase = phase;
            fadeOutPhase = Def.Phase.None;
            inputPad.Phase = this.phase;
            playSetup = this.phase == Def.Phase.PlaySetup;
            isTrialMode = TrialMode.IsTrialModeEnabled();
            phaseTime = 0;
            missionToStart2 = -1;
            decor.StopSound();
            switch (this.phase)
            {
                case Init:
                    pixmap.BackgroundCache("init");
                    break;
                case Pause:
                case Resume:
                    pixmap.BackgroundCache("pause");
                    break;
                case Lost:
                    pixmap.BackgroundCache("lost");
                    break;
                case Win:
                    pixmap.BackgroundCache("win");
                    break;
                case MainSetup:
                case PlaySetup:
                    pixmap.BackgroundCache("setup");
                    break;
                case Trial:
                    pixmap.BackgroundCache("trial");
                    break;
                case Ranking:
                    pixmap.BackgroundCache("pause");
                    break;
                case Play:
                    decor.setDrawBounds(pixmap.DrawBounds());
                    break;
            }
            if (this.phase == Def.Phase.Play && mission > 0)
            {
                StartMission(mission);
            }
        }

        private void MemorizeGamerProgress()
        {
            gameData.setNbVies(decor.GetNbVies());
            decor.MemorizeDoors(gameData);
            gameData.Write();
        }
        //public void SetFullScreen(bool isFullScreen)
        //{
        //    this.graphics.IsFullScreen = isFullScreen;
        //    graphics.ToggleFullScreen();
        //}
        public void ToggleFullScreen()
        {
            this.graphics.ToggleFullScreen();
        }
        public boolean IsFullScreen() { return this.graphics.IsFullScreen(); }
    }
