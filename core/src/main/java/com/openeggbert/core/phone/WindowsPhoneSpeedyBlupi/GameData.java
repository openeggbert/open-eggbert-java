package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

// WindowsPhoneSpeedyBlupi, Version=1.0.0.5, Culture=neutral, PublicKeyToken=6db12cd62dbec439
// WindowsPhoneSpeedyBlupi.GameData
import com.openeggbert.jdotnet.System.*;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.readonly;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.namespace;

    @namespace(name = "WindowsPhoneSpeedyBlupi")

    public class GameData
    {
        private static @readonly final int HeaderLength = 10;

        private static @readonly final int DoorsLength = 200;

        private static @readonly final int GamerLength = 10 + DoorsLength;

        private static @readonly final int MaxGamer = 3;

        private static @readonly final int TotalLength = HeaderLength + GamerLength * MaxGamer;

        private @readonly final byte[] data;

        public int SelectedGamer() {
            return getSelectedGamer();
        }
        public int getSelectedGamer() {
            return data[2];
        }

        public void setSelectedGamer(int value) {
            data[2] = (byte) value;
        }

        
        public boolean Sounds() {
            return isSounds();
        }
        public boolean isSounds() {
            return data[3] == 1;
        }

        public void setSounds(boolean value) {
            data[3] = (byte) (value ? 1 : 0);
        }  
        public boolean JumpRight() {
            return isJumpRight();
        }
        public boolean isJumpRight() {
            return data[4] == 1;
        }

        public void setJumpRight(boolean value) {
            data[4] = (byte) (value ? 1 : 0);
        }

        public boolean AutoZoom() {
            return isAutoZoom();
        }
        public boolean isAutoZoom() {
            return data[5] == 1;
        }

        public void setAutoZoom(boolean value) {
            data[5] = (byte) (value ? 1 : 0);
        }
        public boolean AccelActive() {
            return isAccelActive();
        }
        public boolean isAccelActive() {
            return data[6] == 1;
        }

        public void setAccelActive(boolean value) {
            data[6] = (byte) (value ? 1 : 0);
        }

        public double AccelSensitivity() {
            return getAccelSensitivity();
        }
        public double getAccelSensitivity() {
            return (double) (int) data[7] / 100.0;
        }

        public void setAccelSensitivity(double value) {
            value = Math_.Max(value, 0.0);
            value = Math_.Min(value, 1.0);
            data[7] = (byte) (value * 100.0);
        }

        public int NbVies() {
            return getNbVies();
        }
        public int getNbVies() {
            return data[getGamerOffset()];
        }

        public void setNbVies(int value) {
            data[getGamerOffset()] = (byte) value;
        }
        public int LastWorld() {
            return getLastWorld();
        }
        public int getLastWorld() {
            return data[getGamerOffset() + 1];
        }

        public void setLastWorld(int value) {
            data[getGamerOffset() + 1] = (byte) value;
        }

        private int getGamerOffset() {
            return GetGamerOffset(getSelectedGamer());
        }
        private int getGamerOffset(int gamer) {
            return GetGamerOffset(gamer);
        }


        
        
        public GameData()
        {
            data = new byte[TotalLength];
            Initialize();
        }

        public void Read()
        {
            Worlds.ReadGameData(data);
        }

        public void Write()
        {
            Worlds.WriteGameData(data);
        }

        public void Reset()
        {
            Initialize(getSelectedGamer());
        }

        public void GetDoors(int[] doors)
        {
            for (int i = 0; i < DoorsLength; i++)
            {
                doors[i] = data[getGamerOffset() + 10 + i];
            }
        }

        public void SetDoors(int[] doors)
        {
            for (int i = 0; i < DoorsLength; i++)
            {
                data[getGamerOffset() + 10 + i] = (byte)doors[i];
            }
        }

        static class GamerInfo {

            public int nbVies;
            public int mainDoors;
            public int secondaryDoors;

            public GamerInfo(int nbVies, int mainDoors, int secondaryDoors) {
                this.nbVies = nbVies;
                this.mainDoors = mainDoors;
                this.secondaryDoors = secondaryDoors;
            }
        }
        public GamerInfo GetGamerInfo(int gamer/*, @Out int nbVies, @Out int mainDoors, @Out int secondaryDoors*/) {
            int nbVies = data[getGamerOffset(gamer)];
            int secondaryDoors = 0;

            for (int i = 0; i < 180; i++) {
                if (data[getGamerOffset(gamer) + 10 + i] == 1) {
                    secondaryDoors++;
                }
            }

            int mainDoors = 0;
            for (int j = 180; j < 200; j++) {
                if (data[getGamerOffset(gamer) + 10 + j] == 1) {
                    mainDoors++;
                }
            }

            return new GamerInfo(nbVies, mainDoors, secondaryDoors);

        }

        private void Initialize()
        {
            data[0] = 1;
            data[1] = 1;
            data[2] = 0;
            data[3] = 1;
            data[4] = 1;
            data[5] = 1;
            data[6] = 0;
            data[7] = 50;
            setSelectedGamer(0);
            for (int i = 0; i < MaxGamer; i++)
            {
                Initialize(i);
            }
        }

        private void Initialize(int gamer)
        {
            data[GetGamerOffset(gamer)] = 3;
            data[GetGamerOffset(gamer) + 1] = 1;
            for (int i = 0; i < DoorsLength; i++)
            {
                data[GetGamerOffset(gamer) + 10 + i] = 0;
            }
        }

        private int GetGamerOffset(int gamer)
        {
            return HeaderLength + GamerLength * gamer;
        }
    }

