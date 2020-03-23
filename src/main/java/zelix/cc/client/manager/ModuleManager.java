package zelix.cc.client.manager;

import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.EventController;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.api.Option;
import zelix.cc.client.eventAPI.api.Value;
import zelix.cc.client.eventAPI.events.misc.EventKey;
import zelix.cc.client.eventAPI.events.render.Event2D;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.combat.*;
import zelix.cc.client.modules.player.AntiFall;
import zelix.cc.client.modules.motion.*;
import zelix.cc.client.modules.player.*;
import zelix.cc.client.modules.render.*;
import zelix.cc.client.modules.world.ChestStealer;
import zelix.cc.client.modules.world.Scaffold;
import zelix.cc.client.modules.world.SpeedMine;
import org.lwjgl.input.Keyboard;
import zelix.cc.client.modules.world.Teams;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static List<Module> modules = new ArrayList<>();
    public void init(){
        //Combat
        addModule(new AntiBot());
        addModule(new AutoWeapon());
        addModule(new Aura());
        addModule(new Criticals());
        addModule(new AutoPotion());
        //Motion
        addModule(new Flight());
        addModule(new Speed());
        addModule(new TargetStrafe());
        addModule(new Sprint());
        //Player
        addModule(new AntiKB());
        addModule(new NoFall());
        addModule(new AntiFall());
        addModule(new NoSlowdown());
        addModule(new AutoArmor());
        addModule(new InvMove());
        addModule(new InvCleaner());
        addModule(new Command());
        //World
        addModule(new ChestStealer());
        addModule(new Scaffold());
        addModule(new Teams());
        addModule(new SpeedMine());
        //Render
        addModule(new ClickGui());
        addModule(new HUD());
        addModule(new DMG());
        addModule(new Nametags());
        addModule(new BlockEveryThing());
        addModule(new Animations());
        addModule(new Fullbright());
        //Exploit
        //#any other categorys...
        //Checking list:#removed
        loadSettings();
        EventController.getEventController().register(this);
    }
    @Runnable
    private void onKeyPress(EventKey e) {
        System.out.println(e.getKey());
        for (Module m : modules) {
            if (m.boundKey != e.getKey()) {
                continue;
            }
            m.setState(!m.isEnabled());
        }
    }

    private boolean enabledNeededMod = true;
    @Runnable
    private void on2D(Event2D event2D){
        if (this.enabledNeededMod) {
            this.enabledNeededMod = false;
            for (Module m : modules) {
                if (!m.toEnable) {
                    continue;
                }
                m.setState(true);
            }
        }
    }
    void addModule(Module module){
        modules.add(module);
    }
    public Module getModuleByClass(Class<? extends Module> clazz){
        for(Module module : modules){
            if(module.getClass().getName().equals(clazz.getName())) {
                return module;
            }
        }
        return null;
    }
    public static Module getModuleByName(String name) {
        for(Module module : modules){
            if(name.toLowerCase().contains(module.name.toLowerCase())) {
                return module;
            }
        }
        return null;
    }

    public List<Module> getModules() {
        return modules;
    }
    private void loadSettings() {
        this.getModuleByClass(Command.class).setState(true);
        List<String> binds = Zelix.getInstance().settingsManager.read("Binds.json");
        for (String v : binds) {
            String name = v.split(":")[0];
            String bind = v.split(":")[1];
            Module m = getModuleByName(name);
            if (m == null) {
                continue;
            }
            if(Keyboard.getKeyIndex(bind.toUpperCase()) == 47) {
                m.setBoundKey(47);
            }
            m.setBoundKey(Keyboard.getKeyIndex(bind.toUpperCase()));
        }
        List<String> enabled = Zelix.getInstance().settingsManager.read("Enabled.json");
        for (String v : enabled) {
            Module m = ModuleManager.getModuleByName(v);
            if (m == null) {
                continue;
            }
            m.toEnable = true;
        }
        List<String> vals = Zelix.getInstance().settingsManager.read("Values.json");
        for (String v : vals) {
            String name = v.split(":")[0];
            String values = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null) {
                continue;
            }
            for (Value value : m.value) {
                if (!value.configlnName.equalsIgnoreCase(values)) {
                    continue;
                }
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                    continue;
                }
                if (value instanceof zelix.cc.client.eventAPI.api.Amount) {
                    value.setValue(Double.parseDouble(v.split(":")[2]));
                    continue;
                }
                ((Mode)value).setMode(v.split(":")[2]);
            }
        }
    }
}
