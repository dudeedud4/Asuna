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

package com.sasha.adorufu.module.modules;

import com.sasha.adorufu.AdorufuMod;
import com.sasha.adorufu.events.client.ClientEnderPearlSpawnEvent;
import com.sasha.adorufu.events.server.ServerGenerateChunkEvent;
import com.sasha.adorufu.module.AdorufuCategory;
import com.sasha.adorufu.module.AdorufuModule;
import com.sasha.adorufu.module.ModuleInfo;
import com.sasha.eventsys.SimpleEventHandler;
import com.sasha.eventsys.SimpleListener;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.sasha.adorufu.misc.AdorufuRender.chunkESP;

/**
 * Created by Sasha on 11/08/2018 at 8:34 PM
 **/
@ModuleInfo(description = "Highlights chunks that have never been generated by the server. Useful for base hunting.")
public class ModuleChunkTrace extends AdorufuModule implements SimpleListener {

    public static ArrayList<Chunk> chks = new ArrayList<>();

    public ModuleChunkTrace() {
        super("ChunkTrace", AdorufuCategory.RENDER, false, true);
        this.addOption("ChunkESP", true);
        this.addOption("PearlNotify", false);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTick() {
        LinkedHashMap<String, Boolean> suffixMap = new LinkedHashMap<>();
        suffixMap.put("Chunks", this.getOption("ChunkESP"));
        suffixMap.put("Pearls", this.getOption("PearlNotify"));
        this.setSuffix(suffixMap);
    }

    @Override
    public void onRender() {
        if (this.isEnabled() && this.getOption("ChunkESP")) {
                for (Chunk chunk : chks) {
                    int x, z;
                    x = chunk.x * 16;
                    z = chunk.z * 16;
                    double maxY = AdorufuMod.minecraft.player.posY + 25;
                    int y = 0;
                    chunkESP(x, y, z, 1.0f, 0.0f, 0.0f, 0.5f, maxY);
                }
        }
    }

    @SimpleEventHandler
    public void onEnderPearlSpawn(ClientEnderPearlSpawnEvent e) {
        if (!this.isEnabled() || !this.getOption("PearlNotify")) return;
        AdorufuMod.logMsg(false, "\2478(\247bChunkTrace\2478) \2477Ender pearl loaded @ XYZ *x *y *z".replace("*x", e.getCoordinate()[0] + "")
                .replace("*y", e.getCoordinate()[1] + "").replace("*z", e.getCoordinate()[2] + ""));
    }

    @SimpleEventHandler
    public void onNewChunk(ServerGenerateChunkEvent e) {
        if (!this.isEnabled() || !this.getOption("ChunkESP")) return;
        if (e.getPacketIn().isFullChunk()) return;
        if (!chks.contains(e.getChunk())) {
            chks.add(e.getChunk());
        }
    }
}
