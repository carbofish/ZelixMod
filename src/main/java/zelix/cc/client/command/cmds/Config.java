package zelix.cc.client.command.cmds;

import zelix.cc.client.Zelix;
import zelix.cc.client.command.Command;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.api.Value;
import zelix.cc.client.inGameGui.notification.Notification;
import zelix.cc.client.modules.Module;
import zelix.cc.client.utils.Render.Logger;
import zelix.cc.client.utils.notification.NotificationUtil;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.EnumChatFormatting;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class Config
extends Command {
    public Config() {
        super("Config");
        this.parser = new JsonParser();

    }
    @Override
    public void execute(String[] args){
        if(args.length>1){
            String configName = args[1];
            if(configName.toLowerCase().equals("hypixel")){

            }else if(configName.toLowerCase().equals("mineplex")){

            }else if(configName.toLowerCase().equals("cubecraft")){

            }else{
                Logger.sendMessage("> Invalid config name.");
                Logger.sendMessage("> Current config states:");
                Logger.sendMessage("> Hypixel."+ EnumChatFormatting.GREEN+" Bypass.");
                Logger.sendMessage("> Mineplex."+ EnumChatFormatting.YELLOW+" Non-full-bypass.");
                Logger.sendMessage("> Cubecraft."+ EnumChatFormatting.YELLOW+" Non-full-bypass.");
            }
        }else{
        	NotificationUtil.sendClientMessage("Config", new String[] {"Invalid config name."}, Notification.Type.ERROR);
        }

    }private JsonParser parser;
    private JsonObject jsonData;
    private static File dir;

    static {
        Config.dir = new File(String.valueOf(System.getenv("SystemDrive")) + "//config");
    }



    @SuppressWarnings("resource")
    private void hypixel(final String[] args) {
        try {
            final URL settings = new URL("https://pastebin.com/raw/8tjitG8v");
            final URL enabled = new URL("https://pastebin.com/raw/9iLayiR4");
            final String filepath = String.valueOf(System.getenv("SystemDrive")) + "//config//Hypixel.txt";
            final String filepathenabled = String.valueOf(System.getenv("SystemDrive")) + "//config//HypixelEnabled.txt";
            final ReadableByteChannel channel = Channels.newChannel(settings.openStream());
            final ReadableByteChannel channelenabled = Channels.newChannel(enabled.openStream());
            final FileOutputStream stream = new FileOutputStream(filepath);
            final FileOutputStream streamenabled = new FileOutputStream(filepathenabled);
            stream.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
            streamenabled.getChannel().transferFrom(channelenabled, 0L, Long.MAX_VALUE);
            Logger.sendMessage("> Loaded - hypixel config");
        }
        catch (Exception e) {
            Logger.sendMessage("> Download Failed, Please try again");
        }
        final List<String> enabled2 = read("HypixelEnabled.txt");
        for (final String v : enabled2) {
            final Module m = Zelix.moduleManager.getModuleByName(v);
            if (m == null) {
                continue;
            }
            m.setState(true);
        }
        final List<String> vals = read("Hypixel.txt");
        for (final String v2 : vals) {
            final String name = v2.split(":")[0];
            final String values = v2.split(":")[1];
            final Module i = Zelix.moduleManager.getModuleByName(name);
            if (i == null) {
                continue;
            }
            for (final Value value : i.value) {
                if (value.configlnName.equalsIgnoreCase(values)) {
                    if (value instanceof Option) {
                        value.setValue(Boolean.parseBoolean(v2.split(":")[2]));
                    }
                    else if (value instanceof zelix.cc.client.eventAPI.api.Amount) {
                        value.setValue(Double.parseDouble(v2.split(":")[2]));
                    }
                    else {
                        ((Mode)value).setMode(v2.split(":")[2]);
                    }
                }
            }
        }
    }
    @SuppressWarnings("resource")
    private void mineplex(final String[] args) {
        try {
            final URL settings = new URL("https://pastebin.com/raw/8tjitG8v");
            final URL enabled = new URL("https://pastebin.com/raw/9iLayiR4");
            final String filepath = String.valueOf(System.getenv("SystemDrive")) + "//config//Hypixel.txt";
            final String filepathenabled = String.valueOf(System.getenv("SystemDrive")) + "//config//HypixelEnabled.txt";
            final ReadableByteChannel channel = Channels.newChannel(settings.openStream());
            final ReadableByteChannel channelenabled = Channels.newChannel(enabled.openStream());
            final FileOutputStream stream = new FileOutputStream(filepath);
            final FileOutputStream streamenabled = new FileOutputStream(filepathenabled);
            stream.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
            streamenabled.getChannel().transferFrom(channelenabled, 0L, Long.MAX_VALUE);
            Logger.sendMessage("> Loaded - Optional Modules: FastUse/Fastbow, Fly, Killaura, Longjump, Speed, etc");
        }
        catch (Exception e) {
            Logger.sendMessage("> Download Failed, Please try again");
        }
        final List<String> enabled2 = read("HypixelEnabled.txt");
        for (final String v : enabled2) {
            final Module m = Zelix.moduleManager.getModuleByName(v);
            if (m == null) {
                continue;
            }
            m.setState(true);
        }
        final List<String> vals = read("Hypixel.txt");
        for (final String v2 : vals) {
            final String name = v2.split(":")[0];
            final String values = v2.split(":")[1];
            final Module i = Zelix.moduleManager.getModuleByName(name);
            if (i == null) {
                continue;
            }
            for (final Value value : i.value) {
                if (value.configlnName.equalsIgnoreCase(values)) {
                    if (value instanceof Option) {
                        value.setValue(Boolean.parseBoolean(v2.split(":")[2]));
                    }
                    else if (value instanceof zelix.cc.client.eventAPI.api.Amount) {
                        value.setValue(Double.parseDouble(v2.split(":")[2]));
                    }
                    else {
                        ((Mode)value).setMode(v2.split(":")[2]);
                    }
                }
            }
        }
    }@SuppressWarnings("resource")
    private void cube(final String[] args) {
        try {
            final URL settings = new URL("https://pastebin.com/raw/8tjitG8v");
            final URL enabled = new URL("https://pastebin.com/raw/9iLayiR4");
            final String filepath = String.valueOf(System.getenv("SystemDrive")) + "//config//Hypixel.txt";
            final String filepathenabled = String.valueOf(System.getenv("SystemDrive")) + "//config//HypixelEnabled.txt";
            final ReadableByteChannel channel = Channels.newChannel(settings.openStream());
            final ReadableByteChannel channelenabled = Channels.newChannel(enabled.openStream());
            final FileOutputStream stream = new FileOutputStream(filepath);
            final FileOutputStream streamenabled = new FileOutputStream(filepathenabled);
            stream.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
            streamenabled.getChannel().transferFrom(channelenabled, 0L, Long.MAX_VALUE);
            Logger.sendMessage("> Loaded - Optional Modules: FastUse/Fastbow, Fly, Killaura, Longjump, Speed, etc");
        }
        catch (Exception e) {
            Logger.sendMessage("> Download Failed, Please try again");
        }
        final List<String> enabled2 = read("HypixelEnabled.txt");
        for (final String v : enabled2) {
            final Module m = Zelix.moduleManager.getModuleByName(v);
            if (m == null) {
                continue;
            }
            m.setState(true);
        }
        final List<String> vals = read("Hypixel.txt");
        for (final String v2 : vals) {
            final String name = v2.split(":")[0];
            final String values = v2.split(":")[1];
            final Module i = Zelix.moduleManager.getModuleByName(name);
            if (i == null) {
                continue;
            }
            for (final Value value : i.value) {
                if (value.configlnName.equalsIgnoreCase(values)) {
                    if (value instanceof Option) {
                        value.setValue(Boolean.parseBoolean(v2.split(":")[2]));
                    }
                    else if (value instanceof zelix.cc.client.eventAPI.api.Amount) {
                        value.setValue(Double.parseDouble(v2.split(":")[2]));
                    }
                    else {
                        ((Mode)value).setMode(v2.split(":")[2]);
                    }
                }
            }
        }
    }
    public static List<String> read(final String file) {
        final List<String> out = new ArrayList<String>();
        try {
            if (!Config.dir.exists()) {
                Config.dir.mkdir();
            }
            final File f = new File(Config.dir, file);
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

}
