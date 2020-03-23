package zelix.cc.client.modules.motion;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.events.execute.EventPPUpdate;
import zelix.cc.client.modules.Module;
import zelix.cc.client.modules.ModuleType;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import zelix.cc.injection.interfaces.IMixinKeyBinding;

import java.util.Objects;

public class InvMove
extends Module {
    public InvMove() {
        super("InvMove", ModuleType.Player);
    }
    @Runnable
    private void onPre(EventPPUpdate eventPPUpdate){
        if(eventPPUpdate.isPre()) {
            if (Objects.nonNull(mc.currentScreen) && !(mc.currentScreen instanceof GuiChat)) {
                for (KeyBinding keyBinding : new KeyBinding[]{mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump}) {
                    ((IMixinKeyBinding)keyBinding).setPressed(Keyboard.isKeyDown(keyBinding.getKeyCode()));
                }
            }
        }
    }
}
