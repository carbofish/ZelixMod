package zelix.cc.client.inGameGui.proxy;

import java.net.ServerSocket;
import java.util.Random;

import zelix.cc.client.eventAPI.Runnable;
import zelix.cc.client.eventAPI.events.execute.EventTick;
import zelix.cc.client.utils.Instances;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ChatComponentText;

public class ProxyConfig {
    public static int listenerPort = ProxyConfig.findFreePort(29999);
    public static TransparentProxy proxy = null;
    public static ConnectionInfo proxyAddr = null;

    public static ConnectionInfo connect(String ip, int port) {
        if (proxyAddr != null) {
            if (proxy == null) {
                proxy = new TransparentProxy(listenerPort);
            }
            if (proxy.isRunning()) {
                proxy.stop();
            }
            try {
                proxy.start(ProxyConfig.proxyAddr.ip, ProxyConfig.proxyAddr.port, ip, port);
                while (!proxy.isReady() && !proxy.hasFailed()) {
                    Thread.sleep(50L);
                }
                if (proxy.hasFailed()) {
                    throw proxy.getFailReason();
                }
                ConnectionInfo returnment = new ConnectionInfo();
                returnment.ip = "127.0.0.1";
                returnment.port = listenerPort;
                return returnment;
            }
            catch (Exception e) {
                e.printStackTrace();
                ConnectionInfo returnment = new ConnectionInfo();
                returnment.ip = ip;
                returnment.port = port;
                return returnment;
            }
        }
        ConnectionInfo returnment = new ConnectionInfo();
        returnment.ip = ip;
        returnment.port = port;
        return returnment;
    }

    @Runnable
    public void onGlobalGameLoop(EventTick e) {
        if (proxy != null && proxy.isRunning() && proxy.hasFailed()) {
            boolean flag = Instances.mc.isIntegratedServerRunning();
            boolean flag1 = Instances.mc.isSingleplayer();
            Instances.mc.theWorld.sendQuittingDisconnectingPacket();
            Instances.mc.loadWorld(null);
            if (flag) {
                Instances.mc.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.closed", new ChatComponentText("[AC-Proxy] Lost Connection to server!")));
            } else if (flag1) {
                RealmsBridge realmsbridge = new RealmsBridge();
                realmsbridge.switchToRealms(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.closed", new ChatComponentText("[AC-Proxy] Lost Connection to server!")));
            } else {
                Instances.mc.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.closed", new ChatComponentText("[AC-Proxy] Lost Connection to server!")));
            }
        }
    }

    public static void stop() {
        if (proxy != null) {
            proxy.stop();
        }
    }

    private static int findFreePort(int start) {
        int cur = new Random().nextInt(10000) + start - 1;
        do {
            try {
                ServerSocket sock = new ServerSocket(cur, 1);
                sock.close();
                return cur;
            }
            catch (Exception var3) {
                cur = new Random().nextInt(10000) + start - 1;
                continue;
            }
        } while (true);
    }
}

