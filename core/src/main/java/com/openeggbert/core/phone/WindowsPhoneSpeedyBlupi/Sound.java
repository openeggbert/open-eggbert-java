package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

// WindowsPhoneSpeedyBlupi, Version=1.0.0.5, Culture=neutral, PublicKeyToken=6db12cd62dbec439
// WindowsPhoneSpeedyBlupi.Sound
import com.openeggbert.jdotnet.System.*;
import com.openeggbert.jdotnet.System.Collections.Generic.*;
import com.openeggbert.jdotnet.System.Linq.*;
import com.openeggbert.jxna.Microsoft.Xna.Framework.Audio.*;
import java.util.ArrayList;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.namespace;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.readonly;



    @namespace(name = "WindowsPhoneSpeedyBlupi")

    public class Sound
    {
        private class Play
        {
            private final SoundEffectInstance sei;

            private final int channel;
            
            public int Channel() { return channel;}

            public boolean IsFree()
            {
                return sei.State == SoundState.Stopped;
            }

            public Play(SoundEffect se, int channel, double volume, double balance, Double pitch, boolean isLooped)
            {
                this.channel = channel;
                int num = channel * 2;
                if (num >= 0 && num < tableVolumePitch.length)
                {
                    volume *= tableVolumePitch[num];
                    pitch = tableVolumePitch[num + 1];
                }
                sei = se.CreateInstance();
                sei.Volume = (float)volume;
                sei.Pan = (float)balance;
                sei.Pitch = (float)(pitch == null ? 0.0d : pitch);
                sei.IsLooped = isLooped;
                sei.Play();
            }

            public void Stop()
            {
                sei.Stop();
            }
        }

        private static double[] tableVolumePitch = new double[]
        {
        1.0, 0.0, 0.5, 1.0, 0.5, 1.0, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.1, 1.0, 0.3, 1.0, 0.2, 1.0, 0.3, 1.0, 0.5,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.1, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 0.7, 0.2, 1.0, 0.1, 1.0, 0.1,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.4, 1.0, 0.0, 1.0, 0.0,
        1.0, 0.0, 1.0, 0.0, 1.0, 0.2, 1.0, 0.2, 0.7, 0.4,
        1.0, 0.2, 1.0, 0.4, 1.0, 0.2, 0.5, 1.0, 0.5, 1.0,
        1.0, 0.4, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 0.6, 0.4, 0.8, 0.1,
        0.6, 0.5, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.0,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.0,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.0, 1.0, 0.0, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 0.6, 0.4,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2,
        1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2, 1.0, 0.2
        };

        public static final int MAXVOLUME = 20;

        private final Game1 game1;

        private final GameData gameData;

        private @readonly final List_<SoundEffect> soundEffects;

        private final List_<Play> plays;

        private double volume;

        public Sound(Game1 game1, GameData gameData)
        {
            this.game1 = game1;
            this.gameData = gameData;
            soundEffects = new List_<>();
            plays = new List_<>();
            volume = 1.0;
            SoundEffect.MasterVolume = 1f;
        }

        public void LoadContent()
        {
            if (Def.HasSound())
            {
                for (int i = 0; i <= 92; i++)
                {
                    String assetName = string.Format("sounds/sound{0}", int_.of(i).ToString("d3"));
                    SoundEffect item = game1.Content.Load(assetName, SoundEffect.class);
                    soundEffects.Add(item);
                }
            }
        }

        public boolean Create()
        {
            return true;
        }

        public void SetState(boolean bState)
        {
        }

        public void SetCDAudio(boolean bAudio)
        {
        }

        public boolean GetEnable()
        {
            return true;
        }

        public void SetAudioVolume(int volume)
        {
            this.volume = (double)volume / (double)MAXVOLUME;
        }

        public int GetAudioVolume()
        {
            return (int)(volume * (double)MAXVOLUME);
        }

        public void SetMidiVolume(int volume)
        {
        }

        public int GetMidiVolume()
        {
            return 0;
        }
        
        public void StopAll()
        {
            while (plays.Any())
            {
                plays.ElementAt(0).Stop();
                plays.RemoveAt(0);
            }
        }

        public boolean PlayImage(int channel, TinyPoint pos)
        {
            return PlayImage(channel, pos, -1, false);
        }

        public boolean PlayImage(int channel, TinyPoint pos, int rank, boolean bLoop)
        {
            if (!gameData.Sounds())
            {
                return true;
            }
            if (channel >= 0 && channel < soundEffects.Count())
            {

                if (channel != 10 && plays.stream()
                        .anyMatch(x -> x.Channel() == channel && !x.IsFree())) {
                    return true;
                }
                if (plays.Count() >= 10)
                {
                    int num = 0;
                    while (num < plays.Count())
                    {
                        if (plays.ElementAt(num).IsFree())
                        {
                            plays.RemoveAt(num);
                        }
                        else
                        {
                            num++;
                        }
                    }
                }
                Play item = new Play(soundEffects.ElementAt(channel), channel, (float)GetVolume(pos), (float)GetBalance(pos), null, bLoop);
                plays.Add(item);
            }
            return true;
        }

        public boolean PosImage(int channel, TinyPoint pos)
        {
            return true;
        }

        public boolean Stop(int channel)
        {
            int num = 0;
            while (num < plays.Count())
            {
                if (plays.ElementAt(num).Channel() == channel)
                {
                    plays.ElementAt(num).Stop();
                    plays.RemoveAt(num);
                }
                else
                {
                    num++;
                }
            }
            return true;
        }

        private double GetVolume(TinyPoint pos)
        {
            double val = 1.0;
            if (pos.X < 0)
            {
                val = 1.0 + (double)(pos.X / 640) * 2.0;
            }
            if (pos.X > 640)
            {
                pos.X -= 640;
                val = 1.0 - (double)(pos.X / 640) * 2.0;
            }
            val = Math_.Max(val, 0.0);
            val = Math_.Min(val, 1.0);
            double val2 = 1.0;
            if (pos.Y < 0)
            {
                val2 = 1.0 + (double)(pos.Y / 480) * 3.0;
            }
            if (pos.Y > 480)
            {
                pos.Y -= 480;
                val2 = 1.0 - (double)(pos.Y / 480) * 3.0;
            }
            val2 = Math_.Max(val2, 0.0);
            val2 = Math_.Min(val2, 1.0);
            return Math_.Min(val, val2) * volume;
        }

        private double GetBalance(TinyPoint pos)
        {
            double val = (double)pos.X * 2.0 / 640.0 - 1.0;
            val = Math_.Max(val, -1.0);
            return Math_.Min(val, 1.0);
        }
    }
