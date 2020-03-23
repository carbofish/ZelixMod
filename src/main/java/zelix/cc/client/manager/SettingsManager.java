package zelix.cc.client.manager;

import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileWriter;

import net.minecraft.client.Minecraft;
import java.io.File;

public class SettingsManager {
    private static File dir;

    static {
        final File mcDataDir = Minecraft.getMinecraft().mcDataDir;
        SettingsManager.dir = new File(mcDataDir, "Zelix");
    }

    public SettingsManager() {
        super();
        final File mcDataDir = Minecraft.getMinecraft().mcDataDir;
        SettingsManager.dir = new File(mcDataDir, "Zelix");
    }

    public static File getConfigFile(final String name) {
        final File file = new File(SettingsManager.dir, String.format("%s.json", name));
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException ex) {}
        }
        return file;
    }

    public static void init() {
        if (!SettingsManager.dir.exists()) {
            SettingsManager.dir.mkdir();
        }
    }

    static void loadBinding(){
        List<String> bindingText = read("Bind");
    }

    public static List<String> read(final String file) {
        final List<String> out = new ArrayList<String>();
        try {
            if (!SettingsManager.dir.exists()) {
                SettingsManager.dir.mkdir();
            }
            final File f = new File(SettingsManager.dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            Throwable t = null;
            try {
                final FileInputStream fis = new FileInputStream(f);
                try {
                    final InputStreamReader isr = new InputStreamReader(fis);
                    try {
                        final BufferedReader br = new BufferedReader(isr);
                        try {
                            String line = "";
                            while ((line = br.readLine()) != null) {
                                out.add(line);
                            }
                        }
                        finally {
                            if (br != null) {
                                br.close();
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    finally {
                        if (t == null) {
                            final Throwable t2 = null;
                            t = t2;
                        }
                        else {
                            final Throwable t2 = null;
                            if (t != t2) {
                                t.addSuppressed(t2);
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    if (fis != null) {
                        fis.close();
                        return out;
                    }
                }
                finally {
                    if (t == null) {
                        final Throwable t3 = null;
                        t = t3;
                    }
                    else {
                        final Throwable t3 = null;
                        if (t != t3) {
                            t.addSuppressed(t3);
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t4 = null;
                    t = t4;
                }
                else {
                    final Throwable t4 = null;
                    if (t != t4) {
                        t.addSuppressed(t4);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static void save(final String file, final String content, final boolean append) {
        try {
            final File f = new File(SettingsManager.dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            Throwable t = null;
            try {
                final FileWriter writer = new FileWriter(f, append);
                try {
                    writer.write(content);
                }
                finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t2 = null;
                    t = t2;
                }
                else {
                    final Throwable t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
