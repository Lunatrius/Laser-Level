package com.github.lunatrius.laserlevel.proxy;

import com.github.lunatrius.laserlevel.client.renderer.RenderMarkers;
import com.github.lunatrius.laserlevel.handler.KeyInputHandler;
import com.github.lunatrius.laserlevel.json.ConfigurationHandler;
import com.github.lunatrius.laserlevel.marker.Marker;
import com.github.lunatrius.laserlevel.reference.Reference;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClientProxy extends CommonProxy {
    public static final List<Marker> MARKERS = new ArrayList<Marker>();

    @Override
    public void preInit(final FMLPreInitializationEvent event) {
        super.preInit(event);

        for (final KeyBinding keyBinding : KeyInputHandler.KEY_BINDINGS) {
            ClientRegistry.registerKeyBinding(keyBinding);
        }

        final File directory = event.getModConfigurationDirectory();
        ConfigurationHandler.init(new File(directory, Reference.MODID + ".json"));
    }

    @Override
    public void init(final FMLInitializationEvent event) {
        super.init(event);

        FMLCommonHandler.instance().bus().register(KeyInputHandler.INSTANCE);

        MinecraftForge.EVENT_BUS.register(RenderMarkers.INSTANCE);
    }

    @Override
    public void addMarker(final Marker marker) {
        MARKERS.add(marker);
        RenderMarkers.INSTANCE.markDirty();
    }

    @Override
    public void removeMarker(final int index) {
        MARKERS.remove(index);
        RenderMarkers.INSTANCE.markDirty();
    }

    @Override
    public Marker getMarker(final int index) {
        return MARKERS.get(index);
    }
}
