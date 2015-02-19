package com.github.lunatrius.laserlevel.json;

import com.github.lunatrius.laserlevel.marker.Marker;
import com.github.lunatrius.laserlevel.proxy.ClientProxy;
import com.github.lunatrius.laserlevel.reference.Reference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigurationHandler {
    private static File file = null;
    private static Gson gson;

    public static void init(final File file) {
        ConfigurationHandler.file = file;

        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Marker.class, new MarkerSerializer());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();

        if (file.exists()) {
            load();
        }
    }

    public static void load() {
        Reference.logger.trace("Loading config...");
        BufferedReader reader = null;
        try {
            if (file.canRead()) {
                reader = new BufferedReader(new FileReader(file));
                final StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append('\n');
                }
                final ArrayList<Marker> markers = gson.fromJson(builder.toString(), new TypeToken<ArrayList<Marker>>() {}.getType());
                for (final Marker marker : markers) {
                    if (marker != null) {
                        ClientProxy.MARKERS.add(marker);
                    }
                }
            }
        } catch (final IOException e) {
            Reference.logger.error("IO failure!", e);
        } catch (final JsonSyntaxException e) {
            Reference.logger.error(String.format("Malformed JSON in %s!", file.getName()), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Reference.logger.error("IO failure!", e);
                }
            }
        }
    }

    public static void save() {
        Reference.logger.trace("Saving config...");
        FileWriter writer = null;
        try {
            if (!file.exists() || file.canWrite()) {
                writer = new FileWriter(file);
                writer.write(gson.toJson(ClientProxy.MARKERS));
            }
        } catch (final IOException e) {
            Reference.logger.error("IO failure!", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
