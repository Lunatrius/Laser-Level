package com.github.lunatrius.laserlevel.json;

import com.github.lunatrius.laserlevel.marker.Marker;
import com.github.lunatrius.laserlevel.reference.Reference;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.lang.reflect.Type;

public class MarkerSerializer implements JsonSerializer<Marker>, JsonDeserializer<Marker> {
    @Override
    public JsonElement serialize(final Marker src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject object = new JsonObject();
        object.addProperty("x", src.pos.x);
        object.addProperty("y", src.pos.y);
        object.addProperty("z", src.pos.z);
        object.addProperty("enabled", src.enabled);
        object.addProperty("spacing", src.spacing);
        object.addProperty("red", src.getRed());
        object.addProperty("green", src.getGreen());
        object.addProperty("blue", src.getBlue());

        final JsonObject sides = new JsonObject();
        for (final EnumFacing side : EnumFacing.VALUES) {
            sides.addProperty(side.getName(), src.isEnabled(side));
        }

        object.add("sides", sides);

        return object;
    }

    @Override
    public Marker deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            try {
                final JsonObject object = json.getAsJsonObject();
                final int x = object.getAsJsonPrimitive("x").getAsInt();
                final int y = object.getAsJsonPrimitive("y").getAsInt();
                final int z = object.getAsJsonPrimitive("z").getAsInt();
                final boolean enabled = object.getAsJsonPrimitive("enabled").getAsBoolean();
                final int spacing = object.getAsJsonPrimitive("spacing").getAsInt();
                final int red = object.getAsJsonPrimitive("red").getAsInt();
                final int green = object.getAsJsonPrimitive("green").getAsInt();
                final int blue = object.getAsJsonPrimitive("blue").getAsInt();
                final JsonObject sides = object.getAsJsonObject("sides");

                final Marker marker = new Marker(new BlockPos(x, y, z), spacing, 0x000000);
                marker.enabled = enabled;
                marker.setRed(red);
                marker.setGreen(green);
                marker.setBlue(blue);

                for (final EnumFacing side : EnumFacing.VALUES) {
                    if (sides.has(side.getName())) {
                        marker.setEnabled(side, sides.getAsJsonPrimitive(side.getName()).getAsBoolean());
                    }
                }

                return marker;
            } catch (final Exception e) {
                Reference.logger.error("Could not read marker!", e);
            }
        }
        return null;
    }
}
