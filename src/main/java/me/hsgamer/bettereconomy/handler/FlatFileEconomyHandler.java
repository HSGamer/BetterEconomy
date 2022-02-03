package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.AutoSaveEconomyHandler;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;

import java.util.UUID;

public class FlatFileEconomyHandler extends AutoSaveEconomyHandler {
    private final BukkitConfig storageFile;

    public FlatFileEconomyHandler(BetterEconomy instance) {
        super(instance);
        storageFile = new BukkitConfig(instance, "balances.yml");
        storageFile.setup();
    }

    @Override
    protected void save() {
        storageFile.save();
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return storageFile.contains(uuid.toString());
    }

    @Override
    public double get(UUID uuid) {
        return storageFile.getInstance(uuid.toString(), 0, Number.class).doubleValue();
    }

    @Override
    public boolean set(UUID uuid, double amount) {
        if (amount < 0) {
            return false;
        }
        storageFile.set(uuid.toString(), amount);
        enableSave();
        return true;
    }

    @Override
    public boolean createAccount(UUID uuid, double startAmount) {
        if (hasAccount(uuid)) {
            return false;
        }
        storageFile.set(uuid.toString(), startAmount);
        enableSave();
        return true;
    }
}
