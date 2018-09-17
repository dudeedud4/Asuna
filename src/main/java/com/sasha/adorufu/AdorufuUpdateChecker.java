/*
 * Copyright (c) Sasha Stevens 2018.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.sasha.adorufu;

import com.sasha.adorufu.events.client.ClientPacketRecieveEvent;
import com.sasha.eventsys.SimpleEventHandler;
import com.sasha.eventsys.SimpleListener;
import net.minecraft.network.play.server.SPacketServerDifficulty;

import java.io.*;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AdorufuUpdateChecker implements SimpleListener {

    public static String latestVer = AdorufuMod.VERSION;

    public static boolean checkForUpdates() {
        try {
            URL url = new URL("http://2b2tmuseum.com/xdolfver.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            if ((latestVer = br.readLine()).equals(AdorufuMod.VERSION)) {
                br.close();
                return false;
            }
            br.close();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    @SimpleEventHandler
    public void onPacketRx(ClientPacketRecieveEvent e) {
        if (e.getRecievedPacket() instanceof SPacketServerDifficulty) {
            if (!checkForUpdates()) {
                return;
            }
            AdorufuMod.scheduler.schedule(() -> {
                AdorufuMod.logMsg("\247eThere is an update available for Adorufu.");
                AdorufuMod.logMsg("http://sashadev.me/EmotionalLove/Adorufu");
                AdorufuMod.logMsg("Run \247l-update");
                AdorufuMod.logMsg("Current Version \247l" + AdorufuMod.VERSION);
                AdorufuMod.logMsg("Latest Version \247l" + latestVer);
            }, 10, TimeUnit.SECONDS);
        }
    }
}
