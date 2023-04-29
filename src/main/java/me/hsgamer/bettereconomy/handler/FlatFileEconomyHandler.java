package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.AutoSaveEconomyHandler;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;

import java.util.UUID;

public class FlatFileEconomyHandler extends AutoSaveEconomyHandler {
    private final Config config;

    public FlatFileEconomyHandler(BetterEconomy instance, Config config) {
        super(instance);
        this.config = config;
        config.setup();
    }

    public FlatFileEconomyHandler(BetterEconomy instance) {
        this(instance, new BukkitConfig(instance, "balances.yml"));
    }

    @Override
    protected void save() {
        config.save();
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return config.contains(new PathString(uuid.toString()));
    }

    @Override
    public double get(UUID uuid) {
        return config.getInstance(new PathString(uuid.toString()), 0, Number.class).doubleValue();
    }

    @Override
    public boolean set(UUID uuid, double amount) {
        if (amount < 0) {
            return false;
        }
        config.set(new PathString(uuid.toString()), amount);
        enableSave();
        return true;
    }

    @Override
    public boolean createAccount(UUID uuid, double startAmount) {
        if (hasAccount(uuid)) {
            return false;
        }
        config.set(new PathString(uuid.toString()), startAmount);
        enableSave();
        return true;
    }
}
