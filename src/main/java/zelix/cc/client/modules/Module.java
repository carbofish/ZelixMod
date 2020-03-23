package zelix.cc.client.modules;

import zelix.cc.client.eventAPI.EventController;
import zelix.cc.client.eventAPI.EventController;
import zelix.cc.client.eventAPI.api.Value;
import zelix.cc.client.inGameGui.notification.Notification;
import zelix.cc.client.utils.Color.ColorUtil;
import zelix.cc.client.utils.notification.NotificationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class Module {
    public boolean state;
    public String name;
    public boolean toEnable = false;
    public Minecraft mc = Minecraft.getMinecraft();
    public int boundKey = -999;
    public boolean isEnabled(){
        return state;
    }
    public String getName(){
        return name;
    }
    public ModuleType moduleType;
    public List<Value> value;
    private int color;
    public Module(String name,ModuleType type){
        this.name = name;
        this.moduleType = type;
        value = new ArrayList<>();
        color = ColorUtil.getColor((int) (155 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random()), 255);
    }
    public int getColor() {
        return color;
    }
    public Value getValueByName(String name){
        for (Value value : this.value){
            if(value.displayName.toLowerCase().contains(name.toLowerCase())) {
                return value;
            }
        }
        return null;
    }
    public List<Value> getValues(){
        return this.value;
    }
    public boolean hasBind() {
        if(boundKey == -999 || boundKey == 0) {
            return false;
        }else {
            return true;
        }
    }
    public String getBindName() {
        if(boundKey == -999 || boundKey == 0) {
            return "";
        }else {
            return EnumChatFormatting.WHITE +" ["+org.lwjgl.input.Keyboard.getKeyName(this.boundKey)+"] ";
        }
    }
    public String suffix;
    public String getSuffix(){
        if(suffix != null) {
            return suffix;
        } else {
            return "";
        }
    }
    public void setSuffix(Object obj) {
        String suffix = obj.toString();
        if(suffix.isEmpty()) {
            this.suffix = suffix;
        } else {
            this.suffix = String.format("\u00a77 \u00a7f%s\u00a77", new Object[]{EnumChatFormatting.WHITE+"" +EnumChatFormatting.GRAY + suffix+EnumChatFormatting.WHITE+""});
        }

    }
    private float anim;
    public float getAnim() {
        return anim;
    }
    public void setAnim(float anim) {
        this.anim = anim;
    }
    public void setBoundKey(int i){
        boundKey = i;
    }
    public void addSettings(Value... value){
        for(int i = 0;i<value.length;i++){
            value[i].setValue(value[i].defaultValue);
            this.value.add(value[i]);
        }
    }
    public void onEnable(){
        EventController.getEventController().register(this);
    }
    public void onDisable() {
        EventController.getEventController().unregister(this);
    }
    public void setState(boolean forJudge){
        if(forJudge)
        {
            onEnable();
            state = true;
            NotificationUtil.sendClientMessage("Toggle", new String[] {"> Set " + name + " to "+(state?(EnumChatFormatting.GREEN+"Enabled"):(EnumChatFormatting.RED+"Disabled"))}, Notification.Type.SUCCESS);
        }
        else
        {
            onDisable();
            state = false;
            NotificationUtil.sendClientMessage("Toggle", new String[] {"> Set " + name + " to "+(state?(EnumChatFormatting.GREEN+"Enabled"):(EnumChatFormatting.RED+"Disabled"))}, Notification.Type.SUCCESS);
        }
    }
}
