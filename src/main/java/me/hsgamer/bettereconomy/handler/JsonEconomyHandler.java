package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.AutoSaveEconomyHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public class JsonEconomyHandler extends AutoSaveEconomyHandler {
    private final File file;
    private final JSONObject jsonObject;

    public JsonEconomyHandler(BetterEconomy instance) {
        super(instance);
        file = new File(instance.getDataFolder(), "balances.json");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    instance.getLogger().info("Created " + file.getName());
                    try (Writer writer = new FileWriter(file)) {
                        new JSONObject().writeJSONString(writer);
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException("Error when creating file", e);
            }
        }

        JSONObject tempJson;
        try (Reader reader = new FileReader(file)) {
            Object object = new JSONParser().parse(reader);
            if (object instanceof JSONObject) {
                tempJson = (JSONObject) object;
            } else {
                throw new ClassCastException("The parsed object is not an JSONObject");
            }
        } catch (IOException | ParseException e) {
            instance.getLogger().log(Level.WARNING, "Error when reading file", e);
            tempJson = new JSONObject();
        }
        jsonObject = tempJson;
    }

    @Override
    protected void save() {
        try (Writer writer = new FileWriter(file)) {
            jsonObject.writeJSONString(writer);
        } catch (IOException e) {
            instance.getLogger().log(Level.WARNING, "Error when saving file", e);
        }
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return jsonObject.containsKey(uuid.toString());
    }

    @Override
    public double get(UUID uuid) {
        return Optional.ofNullable(jsonObject.get(uuid.toString()))
                .map(String::valueOf)
                .map(Double::parseDouble)
                .orElse(0D);
    }

    @Override
    public boolean set(UUID uuid, double amount) {
        if (amount < 0) {
            return false;
        }
        // noinspection unchecked
        jsonObject.put(uuid.toString(), amount);
        enableSave();
        return true;
    }

    @Override
    public boolean createAccount(UUID uuid, double startAmount) {
        if (hasAccount(uuid)) {
            return false;
        }
        // noinspection unchecked
        jsonObject.put(uuid.toString(), startAmount);
        enableSave();
        return true;
    }
}
