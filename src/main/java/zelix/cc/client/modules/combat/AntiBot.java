package zelix.cc.client.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import zelix.cc.client.Zelix;
import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.api.Mode;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.eventAPI.events.misc.EventChat;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import zelix.cc.client.utils.Math.TimerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import zelix.cc.client.utils.Render.Logger;

import java.util.ArrayList;
import java.util.List;

public class AntiBot
extends Module {
    public Mode mode = new Mode("Mode","Mode", AntiBotMode.values(),AntiBotMode.Hypixel);
    enum AntiBotMode {
        Hypixel,
        Mineplex
    }
    TimerUtil timerUtils = new TimerUtil();
    TimerUtil timerUtil = new TimerUtil();
    public AntiBot() {
        super("AntiBot", ModuleType.Combat);
        addSettings(mode);
    }
    public static List<String> chatTextList = new ArrayList<>();
    public static List<Entity> invalidEntitys = new ArrayList<>();
    public static List<Entity> entityToRemove = new ArrayList<>();
    public static boolean isBlackListed(Entity entity){
        AntiBot antiBot = (AntiBot) Zelix.getInstance().getModuleManager().getModuleByClass(AntiBot.class);
        if(antiBot.isEnabled()){
            return invalidEntitys.contains(entity);
        }
        return false;
    }

    @Override
    public void onEnable(){
        invalidEntitys.clear();
        super.onEnable();
    }

    @Override
    public void onDisable(){
        invalidEntitys.clear();
        super.onDisable();
    }
    @Runnable
    public void onChat(EventChat eventChat){
        if(mode.getValue().equals(AntiBotMode.Hypixel)) {
            chatTextList.add(eventChat.message);
        }
    }
    public List<EntityPlayer> getTabPlayerList() {
        final NetHandlerPlayClient var4 = mc.thePlayer.sendQueue;
        final List<EntityPlayer> list = new ArrayList<>();
        final List players = new ArrayList();
        for (final Object o : players) {
            final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
            if (info == null) {
                continue;
            }
            list.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }
    public static boolean contains(Entity entity){
        return invalidEntitys.contains(entity);
    }
    @Runnable
    private void onUpdate(EventPPUpdate eventPPUpdate){
        setSuffix(mode.getValue().toString());
        if(mode.getValue().equals(AntiBotMode.Hypixel)){
            /*for (Entity entity : mc.theWorld.loadedEntityList){
                if(entity instanceof EntityPlayer){
                    if(!contains(entity))
                        invalidEntitys.add(entity);
                    if(entity.ticksExisted >= 100 && contains(entity)
                            && getTabPlayerList().contains(entity))
                        invalidEntitys.remove(entity);
                    if(entity.getDistanceToEntity(mc.thePlayer) <= 10){
                        for (Entity entity1 : mc.theWorld.loadedEntityList){
                            if(!entity1.equals(entity) && entity1.getName().equals(entity.getName()))
                            {
                                Entity entity2 = entity.getDistanceToEntity(mc.thePlayer) > entity1.getDistanceToEntity(mc.thePlayer) ? entity1 : entity;
                                if(!contains(entity2))
                                    invalidEntitys.add(entity2);
                            }
                        }
                    }
                    String custom = entity.getCustomNameTag();
                    if(!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("§c") && custom.toLowerCase().contains("§r")){
                        if(!contains(entity))
                            invalidEntitys.add(entity);
                    }
                }
            }*/
            if(!invalidEntitys.isEmpty() && timerUtil.hasReached(1500))
            {
                invalidEntitys.clear();
                timerUtil.reset();
            }
            if(timerUtils.hasReached(1000) && !entityToRemove.isEmpty())
            {
                Logger.sendMessage("> Deleted " + entityToRemove.size() + " bot" + (entityToRemove.size() > 1 ? "s" : ""));
                timerUtils.reset();
            }
            for (Object list : this.mc.theWorld.playerEntities) {
                EntityPlayer entity = (EntityPlayer)list;
                final String formated = entity.getDisplayName().getFormattedText();
                final String custom = entity.getCustomNameTag();

                String name = entity.getName();
                if (formated.contains("[NPC]")) {
                    if(!invalidEntitys.contains(entity)) {
                        invalidEntitys.add(entity);
                    }
                    continue;
                }
                if(entity.isInvisible() && !formated.startsWith("§c") && formated.endsWith("§r") && custom.equals(entity.getName())){
                    double diffX = Math.abs(entity.posX - mc.thePlayer.posX);
                    double diffY = Math.abs(entity.posY - mc.thePlayer.posY);
                    double diffZ = Math.abs(entity.posZ - mc.thePlayer.posZ);
                    double diffH = Math.sqrt(diffX * diffX + diffZ * diffZ);
                    if(diffY < 13 && diffY > 10 && diffH < 3){
                        List<EntityPlayer> list1 = getTabPlayerList();
                        if(!list1.contains(entity)){
                            timerUtils.reset();
                            entityToRemove.add(entity);
                            mc.theWorld.removeEntity(entity);
                            if(!invalidEntitys.contains(entity)) {
                                invalidEntitys.add(entity);
                            }
                            continue;
                        }

                    }

                }
                //SHOP BEDWARS
                if(!formated.startsWith("§") && formated.endsWith("§r")){
                    if(!invalidEntitys.contains(entity)) {
                        invalidEntitys.add(entity);
                    }
                    continue;
                }
                if(entity.isInvisible()){
                    //BOT INVISIBLES IN GAME
                    if(!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("§c§c") && name.contains("§c")){
                        timerUtils.reset();
                        invalidEntitys.add(entity);
                        mc.theWorld.removeEntity(entity);
                        if(!invalidEntitys.contains(entity)) {
                            invalidEntitys.add(entity);
                        }
                        continue;
                    }
                }
                //WATCHDOG BOT
                if(!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("§c") && custom.toLowerCase().contains("§r")){
                    timerUtils.reset();
                    invalidEntitys.add(entity);
                    mc.theWorld.removeEntity(entity);
                    if(!invalidEntitys.contains(entity)) {
                        invalidEntitys.add(entity);
                    }
                    continue;
                }

                //BOT LOBBY
                if(formated.contains("§8[NPC]")){
                    if(!invalidEntitys.contains(entity)) {
                        invalidEntitys.add(entity);
                    }
                    continue;
                }
                if(!formated.contains("§c") && !custom.equalsIgnoreCase("")){
                    if(!invalidEntitys.contains(entity)) {
                        invalidEntitys.add(entity);
                    }
                    continue;
                }
                if (list == mc.thePlayer || entity.isDead || !entity.isInvisible() || !this.getTabPlayerList().contains(entity) || entity.getCustomNameTag().length() < 2) {
                    continue;
                }
                this.mc.theWorld.removeEntity(entity);
            }


        }
        if(timerUtil.hasReached(1500)&&mode.getValue().equals(AntiBotMode.Mineplex)){
            timerUtil.reset();
            invalidEntitys.clear();
        }
        if(mode.getValue().equals(AntiBotMode.Mineplex)){
            for(Object o : mc.theWorld.loadedEntityList) {
                Entity en = (Entity) o;
                if (en instanceof EntityPlayer && !(en instanceof EntityPlayerSP)) {
                    String customname = en.getCustomNameTag();
                    if (customname == "" && !invalidEntitys.contains(en)) {
                        invalidEntitys.add(en);
                    }
                }
            }
        }
    }
}
