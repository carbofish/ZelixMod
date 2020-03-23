package zelix.cc.client;

import zelix.cc.client.command.CommandManager;
import zelix.cc.client.eventAPI.EventController;
import zelix.cc.client.eventAPI.api.Value;
import zelix.cc.client.inGameGui.altLogin.source.account.AccountManager;
import zelix.cc.client.inGameGui.altLogin.source.account.AltSaving;
import zelix.cc.client.inGameGui.newclickgui.Font.FontLoaders;
import zelix.cc.client.inGameGui.newclickgui.draw.TextureManager;
import zelix.cc.client.manager.FontManager;
import zelix.cc.client.manager.ModuleManager;
import zelix.cc.client.manager.SettingsManager;
import zelix.cc.client.modules.Module;
import zelix.cc.client.utils.Account.AltService;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.IOException;

public class Zelix {
    public static Zelix instance = new Zelix();
    public static EventController eventController = new EventController();
    public static ModuleManager moduleManager = new ModuleManager();
    public static CommandManager commandManager = new CommandManager();
    public static SettingsManager settingsManager = new SettingsManager();
    public static FontManager fontManager;
    public static AltSaving altSaving ;
    public static AccountManager accountManager;
    public static FontLoaders fontloader = new FontLoaders();
    public static TextureManager tm = new TextureManager();
    public static String version = "v1.0.1";
    public static String clientName = "ZelixClient";
    public void init(){
        fontManager = new FontManager();
        moduleManager.init();
        fontloader.init();
        try {
            tm.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        eventController.init(moduleManager.modules);
        commandManager.init();
        settingsManager.init();
        accountManager = new AccountManager(new File(Minecraft.getMinecraft().mcDataDir,"Zelix"));
        altSaving = new AltSaving(new File(Minecraft.getMinecraft().mcDataDir,"Zelix"));
        Display.setTitle(clientName+" "+version);
    }
    public void saveSettings(){
        String values = "";
        for (Module m : moduleManager.modules) {
            for (Value v : m.value) {
                values = String.valueOf(values) + String.format("%s:%s:%s%s", m.getName(), v.configlnName, v.getValue(), System.lineSeparator());
            }
        }
        settingsManager.save("Values.json", values, false);
        String enabled = "";
        for (Module m : ModuleManager.modules) {
            if (!m.isEnabled()) {
                continue;
            }
            enabled = String.valueOf(enabled) + String.format("%s%s", m.getName(), System.lineSeparator());
        }
        settingsManager.save("Enabled.json", enabled, false);
        String bind = "";
        for (Module m : moduleManager.modules) {
            bind = (bind) + String.format("%s:%s%s", m.getName() , m.boundKey == -999 ? "NONE" : Keyboard.getKeyName(m.boundKey) , System.lineSeparator());
        }
        settingsManager.save("Binds.json", bind, false);
    }
    public void unInit(){
        altSaving.saveFile();
        saveSettings();
    }
    public static Zelix getInstance()
    {
        return instance;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public AccountManager getAltManager() {
        return accountManager;
    }
    private AltService altService = new AltService();
    public void switchToMojang() {
        try {
            altService.switchService(AltService.EnumAltService.MOJANG);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("Couldnt switch to MoJANG altservice");
        }
    }
    public void switchToTheAltening() {
        try {
            altService.switchService(AltService.EnumAltService.THEALTENING);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("Couldnt switch to altening altservice");
        }
    }
}
