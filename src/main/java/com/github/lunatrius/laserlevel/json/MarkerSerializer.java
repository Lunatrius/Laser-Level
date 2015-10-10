package com.github.lunatrius.laserlevel.json;

import com.github.lunatrius.laserlevel.marker.Constants;
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
        object.addProperty("markerlength", src.markerLength);
        object.addProperty("x", src.pos.x);
        object.addProperty("y", src.pos.y);
        object.addProperty("z", src.pos.z);
        object.addProperty("dimension", src.dimension);
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
                final int markerLength = getAsInt(object, "markerlength", Constants.Rendering.DEFAULT_LENGTH);
                final int x = getAsInt(object, "x", 0);
                final int y = getAsInt(object, "y", 0);
                final int z = getAsInt(object, "z", 0);
                final int dimension = getAsInt(object, "dimension", 0);
                final boolean enabled = getAsBoolean(object, "enabled", false);
                final int spacing = getAsInt(object, "spacing", 1);
                final int red = getAsInt(object, "red", 0);
                final int green = getAsInt(object, "green", 0);
                final int blue = getAsInt(object, "blue", 0);
                final JsonObject sides = object.getAsJsonObject("sides");

                final Marker marker = new Marker(new BlockPos(x, y, z), dimension, spacing, 0x000000);
                marker.markerLength = markerLength;
                marker.enabled = enabled;
                marker.setRed(red);
                marker.setGreen(green);
                marker.setBlue(blue);

                for (final EnumFacing side : EnumFacing.VALUES) {
                    if (sides.has(side.getName())) {
                        marker.setEnabled(side, getAsBoolean(sides, side.getName(), false));
                    }
                }

                return marker;
            } catch (final Exception e) {
                Reference.logger.error("Could not read marker!", e);
            }
        }
        return null;
    }

    private int getAsInt(final JsonObject object, final String memberName, final int defaultValue) {
        if (object.has(memberName)) {
            return object.getAsJsonPrimitive(memberName).getAsInt();
        }

        return defaultValue;
    }

    private boolean getAsBoolean(final JsonObject object, final String memberName, final boolean defaultValue) {
        if (object.has(memberName)) {
            return object.getAsJsonPrimitive(memberName).getAsBoolean();
        }

        return defaultValue;
    }
}
