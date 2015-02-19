package com.github.lunatrius.laserlevel.handler;

import com.github.lunatrius.laserlevel.client.gui.marker.GuiMarkers;
import com.github.lunatrius.laserlevel.reference.Names;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyInputHandler {
    public static final KeyInputHandler INSTANCE = new KeyInputHandler();

    private static final KeyBinding KEY_BINDING_GUI_MARKERS = new KeyBinding(Names.Keys.OPEN_GUI, Keyboard.KEY_F12, Names.Keys.CATEGORY);

    public static final KeyBinding[] KEY_BINDINGS = new KeyBinding[] {
            KEY_BINDING_GUI_MARKERS
    };

    private final Minecraft minecraft = Minecraft.getMinecraft();

    private KeyInputHandler() {}

    @SubscribeEvent
    public void onKeyInput(final InputEvent event) {
        if (this.minecraft.currentScreen == null) {
            if (KEY_BINDING_GUI_MARKERS.isPressed()) {
                this.minecraft.displayGuiScreen(new GuiMarkers(this.minecraft.currentScreen));
            }
        }
    }
}
