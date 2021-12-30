package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class JsonEconomyHandler extends EconomyHandler {
    private final File file;
    private final JSONObject jsonObject;
    private final AtomicBoolean needSaving = new AtomicBoolean();
    private final BukkitTask task;

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

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!needSaving.get()) {
                    return;
                }
                needSaving.set(false);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try (Writer writer = new FileWriter(file)) {
                            jsonObject.writeJSONString(writer);
                        } catch (IOException e) {
                            instance.getLogger().log(Level.WARNING, "Error when saving file", e);
                        }
                    }
                }.runTask(instance);
            }
        }.runTaskTimerAsynchronously(instance, instance.getMainConfig().getSaveFilePeriod(), instance.getMainConfig().getSaveFilePeriod());
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return jsonObject.containsKey(player.getUniqueId().toString());
    }

    @Override
    public double get(OfflinePlayer player) {
        return Optional.ofNullable(jsonObject.get(player.getUniqueId().toString()))
                .map(String::valueOf)
                .map(Double::parseDouble)
                .orElse(0D);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return get(player) >= amount;
    }

    @Override
    public boolean set(OfflinePlayer player, double amount) {
        if (amount < 0) {
            return false;
        }
        // noinspection unchecked
        jsonObject.put(player.getUniqueId().toString(), amount);
        needSaving.lazySet(true);
        return true;
    }

    @Override
    public boolean createAccount(OfflinePlayer player, double startAmount) {
        if (hasAccount(player)) {
            return false;
        }
        // noinspection unchecked
        jsonObject.put(player.getUniqueId().toString(), startAmount);
        needSaving.lazySet(true);
        return true;
    }

    @Override
    public void disable() {
        task.cancel();
        try (Writer writer = new FileWriter(file)) {
            jsonObject.writeJSONString(writer);
        } catch (IOException e) {
            instance.getLogger().log(Level.WARNING, "Error when saving file", e);
        }
    }
}
