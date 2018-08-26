package com.sasha.adorufu.remote;

import com.sasha.adorufu.AdorufuMod;
import com.sasha.adorufu.gui.remotedatafilegui.GuiCloudControl;
import com.sasha.adorufu.gui.remotedatafilegui.GuiCloudLogin;
import com.sasha.adorufu.remote.packet.events.LoginResponseEvent;
import com.sasha.eventsys.SimpleEventHandler;
import com.sasha.eventsys.SimpleEventManager;
import com.sasha.eventsys.SimpleListener;
import net.minecraft.client.gui.GuiMainMenu;

import java.net.InetAddress;

/**
 * This will (eventually) be used to allow the user to save their keybinds to the cloud and retrieve their
 * settings on other machines.
 *
 * it'll be based around a text-based TCP system to a remote server. It will feature authentication.
 *
 * This will be completely optional for the user, whether they want to use this service or not.
 */
public class RemoteDataManager implements SimpleListener {

    //public static RemoteDataManager INSTANCE;
    public SimpleEventManager EVENT_MANAGER = new SimpleEventManager();
    public boolean loggedIn = false;
    public String adorufuSessionId;
    public String username;

    @SimpleEventHandler
    public void onLoginResponse(LoginResponseEvent e) {
        GuiCloudLogin.message = e.getPck().getResponse();
        if (e.getPck().isLoginSuccessful()) {
            AdorufuMod.minecraft.displayGuiScreen(new GuiCloudControl(new GuiMainMenu()));
        }
    }

    public void connect() {
        AdorufuMod.EVENT_MANAGER.registerListener(this);
        try {
            AdorufuDataClient client = new AdorufuDataClient(InetAddress.getByName(/*todo*/"127.0.0.1"), Integer.parseInt("42069"));
            AdorufuMod.logMsg(true, "\r\nConnected to data server: " + client.socket.getInetAddress() + ":" + client.socket.getPort());
            client.start();
        }catch (Exception ex) {

        }
    }
}