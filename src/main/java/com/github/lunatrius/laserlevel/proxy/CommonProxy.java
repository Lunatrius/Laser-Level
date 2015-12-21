package com.github.lunatrius.laserlevel.proxy;

import com.github.lunatrius.laserlevel.marker.Marker;
import com.github.lunatrius.laserlevel.reference.Reference;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public class CommonProxy {
    public void preInit(final FMLPreInitializationEvent event) {
        Reference.logger = event.getModLog();

        FMLInterModComms.sendMessage("LunatriusCore", "checkUpdate", Reference.FORGE);
    }

    public void init(final FMLInitializationEvent event) {
    }

    public void postInit(final FMLPostInitializationEvent event) {
    }

    public void serverStarting(final FMLServerStartingEvent event) {
    }

    public void serverStopping(final FMLServerStoppingEvent event) {
    }

    public void addMarker(final Marker marker) {
    }

    public void removeMarker(final int index) {
    }

    public Marker getMarker(final int index) {
        return null;
    }
}
