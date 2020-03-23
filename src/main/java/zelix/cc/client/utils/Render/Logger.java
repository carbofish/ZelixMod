package zelix.cc.client.utils.Render;

import zelix.cc.client.Zelix;
import zelix.cc.client.utils.Util;
import net.minecraft.util.*;

public class Logger
extends Util {
    public static void sendMessage(String msg){
        mc.thePlayer.addChatMessage(
                new ChatComponentText(
                        EnumChatFormatting.YELLOW+"["+EnumChatFormatting.RED+Zelix.clientName+EnumChatFormatting.YELLOW+"]"+EnumChatFormatting.GOLD+msg
                )
        );
    }
    public static void sendMessageNoPrefix(String msg){
        mc.thePlayer.addChatMessage(
                new ChatComponentText(
                        EnumChatFormatting.GOLD+msg
                )
        );
    }
}
