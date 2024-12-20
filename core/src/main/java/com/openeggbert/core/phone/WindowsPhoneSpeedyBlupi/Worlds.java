package com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi;

// WindowsPhoneSpeedyBlupi, Version=1.0.0.5, Culture=neutral, PublicKeyToken=6db12cd62dbec439
// WindowsPhoneSpeedyBlupi.Worlds
import com.openeggbert.jdotnet.System.*;
import com.openeggbert.jdotnet.System.Diagnostics.*;
import com.openeggbert.jdotnet.System.Globalization.*;
import com.openeggbert.jdotnet.System.IO.*;
import com.openeggbert.jdotnet.System.IO.IsolatedStorage.*;
import com.openeggbert.jdotnet.System.Text.*;
import com.openeggbert.jxna.Microsoft.Xna.Framework.*;
import com.openeggbert.core.phone.WindowsPhoneSpeedyBlupi.*;
import static com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.default_.default_;
import java.util.Locale;
import lombok.Getter;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.namespace;
import com.openeggbert.jdotnet.JDotNet.CSharpKeyWords.static_;



@namespace(name = "WindowsPhoneSpeedyBlupi")
@static_
    public class Worlds
    {
    private Worlds() {
        //Not meant to be instantiated.
    }
        private static StringBuilder output = new StringBuilder();

        @Getter
        private static final String GameDataFilename = "SpeedyBlupi";

        @Getter
        private static final String CurrentGameFilename = "CurrentGame";

        public static String[] ReadWorld(int gamer, int rank)
        {
            String worldFilename = GetWorldFilename(gamer, rank);
            String text = null;
            try
            {
                Stream stream = TitleContainer.OpenStream(worldFilename);
                StreamReader streamReader = new StreamReader(stream);
                text = streamReader.ReadToEnd();
                stream.Close();
            }
            catch (Exception_ e)
            {
                Debug.Write("Fatal error. Loading world failed: " + worldFilename + "\n");
                Environment.Exit(1);
            }
            if (text == null)
            {
                return null;
            }
            return text.split("\n");
        }

        private static String GetWorldFilename(int gamer, int rank)
        {
            return string.Format("worlds/world{0}.txt", int_.of(rank).ToString("d3"));
        }

        public static boolean ReadGameData(byte[] data)
        {
            IsolatedStorageFile userStoreForApplication = IsolatedStorageFile.GetUserStoreForApplication();
            if (userStoreForApplication.FileExists(GameDataFilename))
            {
                IsolatedStorageFileStream isolatedStorageFileStream = null;
                try
                {
                    isolatedStorageFileStream = userStoreForApplication.OpenFile(GameDataFilename, FileMode.Open);
                }
                catch (IsolatedStorageException e)
                {
                    return false;
                }
                if (isolatedStorageFileStream != null)
                {
                    int count = Math_.Min(data.length, (int)isolatedStorageFileStream.Length);
                    isolatedStorageFileStream.Read(data, 0, count);
                    isolatedStorageFileStream.Close();
                    return true;
                }
            }
            return false;
        }

        public static void WriteGameData(byte[] data)
        {
            IsolatedStorageFile userStoreForApplication = IsolatedStorageFile.GetUserStoreForApplication();
            IsolatedStorageFileStream isolatedStorageFileStream = userStoreForApplication.OpenFile(GameDataFilename, FileMode.Create);
            if (isolatedStorageFileStream != null)
            {
                isolatedStorageFileStream.Write(data, 0, data.length);
                isolatedStorageFileStream.Close();
            }
        }

        public static void DeleteCurrentGame()
        {
            IsolatedStorageFile userStoreForApplication = IsolatedStorageFile.GetUserStoreForApplication();
            try
            {
                userStoreForApplication.DeleteFile(CurrentGameFilename);
            }
            catch (Exception_ e)
            {
            }
        }

        public static String ReadCurrentGame()
        {
            IsolatedStorageFile userStoreForApplication = IsolatedStorageFile.GetUserStoreForApplication();
            if (userStoreForApplication.FileExists(CurrentGameFilename))
            {
                IsolatedStorageFileStream isolatedStorageFileStream = null;
                try
                {
                    isolatedStorageFileStream = userStoreForApplication.OpenFile(CurrentGameFilename, FileMode.Open);
                }
                catch (IsolatedStorageException e)
                {
                    return null;
                }
                if (isolatedStorageFileStream != null)
                {
                    byte[] array = new byte[isolatedStorageFileStream.Length];
                    isolatedStorageFileStream.Read(array, 0, array.length);
                    isolatedStorageFileStream.Close();
                    return Encoding.UTF8.GetString(array, 0, array.length);
                }
            }
            return null;
        }

        public static void WriteCurrentGame(String data)
        {
            IsolatedStorageFile userStoreForApplication = IsolatedStorageFile.GetUserStoreForApplication();
            IsolatedStorageFileStream isolatedStorageFileStream = userStoreForApplication.OpenFile(CurrentGameFilename, FileMode.Create);
            if (isolatedStorageFileStream != null)
            {
                isolatedStorageFileStream.Write(Encoding.UTF8.GetBytes(data), 0, data.length());
                isolatedStorageFileStream.Close();
            }
        }

        public static void GetIntArrayField(String[] lines, String section, int rank, String name, int[] array)
        {
            for (String text : lines)
            {
                if (!text.startsWith(section + ":") || rank-- != 0)
                {
                    continue;
                }
                int num = text.indexOf(name + "=");
                if (num == -1)
                {
                    break;
                }
                num += name.length() + 1;
                int num2 = text.indexOf(" ", num);
                if (num2 == -1)
                {
                    break;
                }
                String[] array2 = text.substring(num, num2 - num).split(",");
                for (int j = 0; j < array2.length; j++)
                {
                 
                    try {
                        int result = Integer.parseInt(array2[j]);
                        array[j] = result;
                    } catch (NumberFormatException e) {
                        array[j] = 0;

                    }

                }
            }
        }

        public static boolean GetBoolField(String[] lines, String section, int rank, String name)
        {
            for (String text : lines)
            {
                if (text.startsWith(section + ":") && rank-- == 0)
                {
                    int num = text.indexOf(name + "=");
                    if (num == -1)
                    {
                        return false;
                    }
                    num += name.length() + 1;
                    int num2 = text.indexOf(" ", num);
                    if (num2 == -1)
                    {
                        return false;
                    }
                    String value = text.substring(num, num2 - num);

                    boolean result;
                    try {
                        result = Boolean.parseBoolean(value);
                        return result;
                    } catch (Exception_ e) {
                        
                    }
                    return false;

                }
            }
            return false;
        }

        public static int GetIntField(String[] lines, String section, int rank, String name)
        {
            for (String text : lines)
            {
                if (text.startsWith(section + ":") && rank-- == 0)
                {
                    int num = text.indexOf(name + "=");
                    if (num == -1)
                    {
                        return 0;
                    }
                    num += name.length() + 1;
                    int num2 = text.indexOf(" ", num);
                    if (num2 == -1)
                    {
                        return 0;
                    }
                    String s = text.substring(num, num2 - num);
                    try {
                        return Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        return 0;
                    }

                }
            }
            return 0;
        }

        public static double GetDoubleField(String[] lines, String section, int rank, String name)
        {
            for (String text : lines)
            {
                if (text.startsWith(section + ":") && rank-- == 0)
                {
                    int num = text.indexOf(name + "=");
                    if (num == -1)
                    {
                        return 0.0;
                    }
                    num += name.length() + 1;
                    int num2 = text.indexOf(" ", num);
                    if (num2 == -1)
                    {
                        return 0.0;
                    }
                    String s = text.substring(num, num2 - num);
                    try {
                        return Double.parseDouble(s);
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }

                }
            }
            return 0.0;
        }

        public static TinyPoint GetPointField(String[] lines, String section, int rank, String name)
        {
            for (String text : lines)
            {
                if (text.startsWith(section + ":") && rank-- == 0)
                {
                    int num = text.indexOf(name + "=");
                    if (num == -1)
                    {
                        return default_(new TinyPoint());
                    }
                    num += name.length() + 1;
                    int num2 = text.indexOf(";", num);
                    if (num2 == -1)
                    {
                        return default_(new TinyPoint());
                    }
                    int num3 = text.indexOf(" ", num);
                    if (num3 == -1)
                    {
                        return default_(new TinyPoint());
                    }
                    String s = text.substring(num, num2 - num);
                    String s2 = text.substring(num2 + 1, num3 - num2 - 1);
                    int result;
                    try {
                        result = Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        return new TinyPoint();
                    }
                    int result2;
                    try {
                        result2 = Integer.parseInt(s2);
                    } catch (NumberFormatException e) {
                        return new TinyPoint();
                    }

                     TinyPoint result3 = default_(new TinyPoint());
                    result3.X = result;
                    result3.Y = result2;
                    return result3;
                }
            }
            /**/
            return default_(new TinyPoint());
        }

    public static Integer GetDecorField(String[] lines, String section, int x, int y) {
        for (int i = 0; i < lines.length; i++) {
            String text = lines[i];
            if (text.startsWith(section + ":")) {
                text = lines[i + 1 + x];
                String[] array = text.split(",");
                if (string.IsNullOrEmpty(array[y])) {
                    return -1;
                }
                try {
                    return Integer.parseInt(array[y]);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

        public static void GetDoorsField(String[] lines, String section, int[] doors)
        {
            for (String text : lines)
            {
                if (!text.startsWith(section + ":"))
                {
                    continue;
                }
                String[] array = text.substring(section.length() + 2).split(",");
                for (int j = 0; j < array.length; j++)
                {
                    int result;
                    if (string.IsNullOrEmpty(array[j]))
                    {
                        doors[j] = 1;
                    }
                    else {
                        try {
                            doors[j] = Integer.parseInt(array[j]);
                        } catch (NumberFormatException e) {
                        }
                    }
                    
                }
            }
        }

        public static void WriteClear()
        {
            output.setLength(0);
        }

        public static void WriteSection(String section)
        {
            output.append(section);
            output.append(": ");
        }

        public static void WriteIntArrayField(String name, int[] array)
        {
            output.append(name);
            output.append("=");
            for (int i = 0; i < array.length; i++)
            {
                if (array[i] != 0)
                {
                    output.append(string.Format(Locale.US, "%s", array[i]));
                }
                if (i < array.length- 1)
                {
                    output.append(",");
                }
            }
            output.append(" ");
        }

        public static void WriteBoolField(String name, boolean n)
        {
            output.append(name);
            output.append("=");
            output.append(string.Format(Locale.US, "%f", n));
            output.append(" ");
        }

        public static void WriteIntField(String name, int n)
        {
            output.append(name);
            output.append("=");
            output.append(string.Format(Locale.US, "%f", n));
            output.append(" ");
        }

        public static void WriteDoubleField(String name, double n)
        {
            output.append(name);
            output.append("=");
            output.append(string.Format(Locale.US, "%f", n));
            output.append(" ");
        }

        public static void WritePointField(String name, TinyPoint p)
        {
            output.append(name);
            output.append("=");
            output.append(string.Format(Locale.US, "%f", p.X));
            output.append(";");
            output.append(string.Format(Locale.US, "%f", p.Y));
            output.append(" ");
        }

        public static void WriteDecorField(int[] line)
        {
            for (int i = 0; i < line.length; i++)
            {
                if (line[i] != -1)
                {
                    output.append(string.Format(Locale.US, "%d", line[i]));

                }
                if (i < line.length - 1)
                {
                    output.append(",");
                }
            }
            output.append("\n");
        }

        public static void WriteDoorsField(int[] doors)
        {
            for (int i = 0; i < doors.length; i++)
            {
                if (doors[i] != 1)
                {
                    output.append(string.Format(Locale.US, "%d", doors[i]));
                }
                if (i < doors.length - 1)
                {
                    output.append(",");
                }
            }
            output.append("\n");
        }

        public static void WriteEndSection()
        {
            output.append("\n");
        }

        public static String GetWriteString()
        {
            return output.toString();
        }
    }
