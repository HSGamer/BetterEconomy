package me.hsgamer.bettereconomy.handler;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.api.EconomyHandler;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicBoolean;

public class FlatFileEconomyHandler extends EconomyHandler {
    private final BukkitConfig storageFile;
    private final AtomicBoolean needSaving = new AtomicBoolean();
    private final BukkitTask task;

    public FlatFileEconomyHandler(BetterEconomy instance) {
        super(instance);
        storageFile = new BukkitConfig(instance, "balances.yml");
        storageFile.setup();
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (needSaving.get()) {
                    storageFile.save();
                    needSaving.set(false);
                }
            }
        }.runTaskTimerAsynchronously(instance, instance.getMainConfig().getSaveFilePeriod(), instance.getMainConfig().getSaveFilePeriod());
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return storageFile.contains(player.getUniqueId().toString());
    }

    @Override
    public double get(OfflinePlayer player) {
        return storageFile.getInstance(player.getUniqueId().toString(), 0, Number.class).doubleValue();
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
        storageFile.set(player.getUniqueId().toString(), amount);
        needSaving.lazySet(true);
        return true;
    }

    @Override
    public boolean createAccount(OfflinePlayer player) {
        if (hasAccount(player)) {
            return false;
        }
        storageFile.set(player.getUniqueId().toString(), instance.getMainConfig().getStartAmount());
        needSaving.lazySet(true);
        return true;
    }

    @Override
    public void disable() {
        task.cancel();
        storageFile.save();
    }
}
